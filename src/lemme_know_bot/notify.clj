(ns lemme-know-bot.notify
  "Functions to add, list, remove, track the notify search list."
  (:gen-class))

(defonce searches (atom []))

(defn add-search!
  "Add a search to the searches atom vector."
  [user search]
  (swap! searches conj {:user user
                        :text search}))

(comment
  (add-search! "yoda" "yo")
  (add-search! "yoda" "ahoy")
  (add-search! "luke" "ahoy")
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
  [user]
  (->> @searches
       (filter (fn [entry] (= (:user entry) user)))
       (keep :text)))

(comment
  (println @searches)
  (list-searches "yoda"))

(defn remove-search!
  "Remove a search in the searches atom vector."
  [user search]
  (swap! searches #(remove
                    (fn [entry] (= entry {:user user :text search}))
                    %)))

(comment
  (println @searches)
  (remove-search! "yoda" "yo"))
