(ns lemme-know-bot.handlers
  "Functions that take action on various message text contents."
  (:gen-class)
  (:require [clojure.string :as string]
            [lemme-know-bot.config :as cfg]
            [lemme-know-bot.fields :as fields]
            [lemme-know-bot.notify :as notify]
            [taoensso.timbre :as log]
            [telegrambot-lib.core :as tbot]))

(defn send-msg
  "Send a message to the chat."
  [bot chat-id text]
  (tbot/send-message bot chat-id text))

(defn help
  "Send info about this bot and available commands."
  [bot msg]
  (log/debug "/help or /start command received.")

  (let [chat-id (fields/chat-id msg)]
    (send-msg bot chat-id
              (str "-- Lemme-Know-Bot: Help/Getting Started --\n"
                   "This bot can watch the current chat for keywords/phrases "
                   "and mention you when one has been seen.\n\n"
                   "The following commands are supported:\n"
                   "/add keyword - add a new keyword to watch for in this chat.\n"
                   "/list - list all the watched keywords.\n"
                   "/remove keyword - remove a watched keyword.\n"
                   "/help - show this help message with available commands.\n"
                   "/settings - show the bot configuration."))))

(defn settings
  "Implements the /settings command."
  [bot msg]
  (log/debug "/settings command received.")

  (let [chat-id (fields/chat-id msg)
        cfg-timeout (:timeout cfg/config)
        cfg-sleep (:sleep cfg/config)]
    (send-msg bot chat-id
              (str "-- Lemme-Know-Bot: Settings --\n"
                   "Timeout (secs) to wait during a long poll: " cfg-timeout "\n"
                   "Sleep (ms) between polls: " cfg-sleep))))

(defn list-search
  "List the current searches setup for the user."
  [bot msg]
  (log/debug "/list command received.")

  (let [chat-id (fields/chat-id msg)
        username (fields/from-user msg)]
    (send-msg bot chat-id
              (str username ", your searches are: "
                   (into [] (notify/list-searches username))))))

(defn extract-keyword
  "Extract only the keyword(s) from a text message.
   Convert to lowercase to avoid case sensitive searches."
  [msg]
  (-> msg
      (fields/text)
      (string/replace-first #"/(\w+)@*(\w+)" "")
      (string/trim)
      (string/lower-case)))

(comment
  (extract-keyword {:message {:text "/add"}}) ; no text
  (extract-keyword {:message {:text "/add "}}) ; spaces only
  (extract-keyword {:message {:text "/add hello"}}) ; text
  (extract-keyword {:message {:text "/add hello there"}}) ; two words
  )

(defn add-search
  "Add a new search for the user."
  [bot msg]
  (log/debug "/add command received.")

  (let [add-text (extract-keyword msg)
        chat-id (fields/chat-id msg)
        username (fields/from-user msg)]

    (if (not (string/blank? add-text))
      ;; not blank - add the text and message back
      (do
        (notify/add-search! username add-text)

        (send-msg bot chat-id
                  (str username ", search added for: '" add-text "'")))

      ;; blank text - do not add
      (send-msg bot chat-id
                (str username ", search NOT added. "
                     "Passed parameter text was blank.\n"
                     "Try sending something like: /add cheeseburger")))))

(defn remove-search
  "Remove a search for the user."
  [bot msg]
  (log/debug "/remove command received.")

  (let [rm-text (extract-keyword msg)
        chat-id (fields/chat-id msg)
        username (fields/from-user msg)]

    (if (not (string/blank? rm-text))
      ;; not blank - remove and message back
      (do
        (notify/remove-search! username rm-text)

        (send-msg bot chat-id
                  (str username ", search removed for: '" rm-text "'")))

      ;; blank text - do not remove
      (send-msg bot chat-id
                (str username ", search NOT removed. "
                     "Passed parameter text was blank.\n"
                     "Try sending someting like: /remove cheeseburger")))))

(defn search
  "Search for a match in the text.
   Mention the user if found."
  [bot msg]
  (let [msg-text (string/lower-case (fields/text msg))
        chat-id (fields/chat-id msg)]

    (log/debug "searching for keywords in:" msg-text)

    (doseq [entry @notify/searches]
      (when (string/includes? msg-text (:text entry))
        (send-msg bot chat-id
                  (str "@" (:user entry)
                       " search string '"
                       (:text entry)
                       "' was found in message:\n"
                       "'" msg-text "'"))))))
