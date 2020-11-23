(ns lemme-know-bot.config
  (:gen-class)
  (:require [environ.core :as environ]))

(def config
  {:timeout (or (environ/env :lkb-timeout) 10) ; timeout in seconds to wait for long polling
   :sleep (or (environ/env :lkb-sleep) 10000); sleep in milliseconds between polls
   })

(comment
  (println "config is:" config))
