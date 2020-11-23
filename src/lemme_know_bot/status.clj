(ns lemme-know-bot.status
  "Functions to keep track of the status of which update ids have been
   processed."
  (:gen-class))

(defonce update-id (atom nil))

(defn set-id!
  "Sets the update id to process next as the the passed in `id`."
  [id]
  (reset! update-id id))

(defn inc-id!
  "Increments the update id to process next."
  []
  (swap! update-id inc))

(defn next-id
  "Returns the next update id to process."
  []
  (inc @update-id))
