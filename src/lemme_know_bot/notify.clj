(ns lemme-know-bot.notify
  "Functions to add, list, remove, track the notify search list."
  (:gen-class)
  (:require [taoensso.timbre :as log]))

(defonce searches (atom []))

(defprotocol search-data
  "Only load supported data types."
  (load-searches! [content]
    "Load a searches data structure at once, merging content with an existing."))

(extend-protocol search-data
  clojure.lang.PersistentVector
  (load-searches!
    [content]
    (swap! searches into content))

  Object
  (load-searches!
    [content]
    (log/warn "not loading searches. data is not a supported type.")))

(comment
  (load-searches! [{:chat-id "123", :user "user1", :text "my search1"}])
  (load-searches! [{:chat-id "123", :user "user2", :text "my search2"}])
  (load-searches! {:user "user1" :text "invalid"})
  (load-searches! "invalid"))

(defn add-search!
  "Add a search to the searches atom vector."
  [chat-id user search]
  (let [chat-str (str chat-id)]
    (swap! searches conj {:chat-id chat-str
                          :user user
                          :text search})))

(comment
  (add-search! 123 "yoda" "yo")
  (add-search! "123" "yoda" "ahoy")
  (add-search! "123" "luke" "ahoy")
  (add-search! "456" "luke" "cheeseburger")
  (println @searches))

(defn clean-searches!
  "Cleanup searches by ensuring only distinct user+text exists."
  []
  (swap! searches distinct))

(comment
  (clean-searches!))

(defn list-searches
  "List the tracked searches given a user.
   Returns a lazy sequence of search words."
  [chat-id user]
  (let [chat-str (str chat-id)]
    (->> @searches
         (filter (fn [entry] (and (= (:chat-id entry) chat-str)
                                  (= (:user entry) user))))
         (keep :text))))

(comment
  (println @searches)
  (list-searches "123" "yoda"))

(defn remove-search!
  "Remove a search in the searches atom vector."
  [chat-id user search]
  (let [chat-str (str chat-id)]
    (swap! searches #(remove
                      (fn [entry] (= entry {:chat-id chat-str
                                            :user user
                                            :text search}))
                      %))))

(comment
  (println @searches)
  (remove-search! 123 "yoda" "yo"))
