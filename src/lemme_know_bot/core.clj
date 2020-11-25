(ns lemme-know-bot.core
  "Core service and application logic."
  (:gen-class)
  (:require [clojure.string :as string]
            [lemme-know-bot.config :as cfg]
            [lemme-know-bot.fields :as fields]
            [lemme-know-bot.handlers :as handlers]
            [lemme-know-bot.notify :as notify]
            [lemme-know-bot.status :as status]
            [taoensso.timbre :as log]
            [telegrambot-lib.core :as tbot]))

;; Commands for use with the botfather
;; add - Add a new keyword or phrase to watch for in this chat. (parameter required)
;; list - List all watched for keywords or phrases. (no parameter)
;; remove - Remove a watched for keyword or phrase. (parameter required)
;; help - Show a help message with available commands.
;; settings - Show the bot configuration.

(defn create-bot
  "Create a Telegram bot instance."
  ([]
   (tbot/create))
  ([token]
   (tbot/create token)))

(comment (create-bot)
         (create-bot "123"))

(defn poll-updates
  "Long poll for recent chat messages from Telegram."
  ([bot]
   (poll-updates bot nil))

  ([bot offset]
   (tbot/get-updates bot {:offset offset
                          :timeout (:timeout cfg/config)})))

(defn handle-msg
  "Check the message text for command or string matches and handle the
   message appropriately."
  [bot msg]
  (log/debug "msg received:" msg)

  (let [msg-text (fields/text msg)]

    (when (not (= nil msg-text))
      (cond
        (string/starts-with? msg-text "/start") (handlers/help bot msg)
        (string/starts-with? msg-text "/help") (handlers/help bot msg)
        (string/starts-with? msg-text "/settings") (handlers/settings bot msg)
        (string/starts-with? msg-text "/list") (handlers/list-search bot msg)
        (string/starts-with? msg-text "/add") (handlers/add-search bot msg)
        (string/starts-with? msg-text "/remove") (handlers/remove-search bot msg)
        :else (handlers/search bot msg)))))

(defn app
  "Retrieve and process chat messages."
  [bot]
  (log/info "lemme-know-bot service started.")

  (loop []
    (log/debug "checking for chat updates.")
    (let [updates (poll-updates bot @status/update-id)
          messages (fields/chat-results updates)]

      ;; Check all messages, if any, for commands/keywords.
      (doseq [msg messages]
        (handle-msg bot msg)
        ;; Increment the next update-id to process.
        (-> msg
            (fields/update-id)
            (inc)
            (status/set-id!)))

      ;; Wait a while before checking for updates again.
      (Thread/sleep (:sleep cfg/config)))
    (recur)))

(defn edn->searches
  "Attempt to load an external edn file into the searches atom."
  []
  (let [loaded-search (cfg/load-edn (:searches cfg/config))]
    (when (some? loaded-search)
      (log/info "loading previous searches.")
      (notify/load-searches! loaded-search))))

(defn shutdown-service
  "Shutdown the service cleanly."
  []
  (shutdown-agents)

  (when (seq @notify/searches)
    (log/info "saving searches list to:" (:searches cfg/config))
    (cfg/save-file (into [] @notify/searches) (:searches cfg/config)))

  (log/info "lemme-know-bot service exited."))

(defn -main
  "Create the Telegram bot and run the application."
  []
  (log/info "starting lemme-know-bot service.")

  (edn->searches)

  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. ^Runnable shutdown-service))

  (let [lemme-bot (create-bot)]

    (if (some? (:bot-token lemme-bot))
      (app lemme-bot)
      (log/error "required bot-token not found! exiting."))))
