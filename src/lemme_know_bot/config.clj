(ns lemme-know-bot.config
  (:gen-class)
  (:require [clojure.edn :as edn]
            [environ.core :as environ]
            [clojure.java.io :as io]
            [taoensso.timbre :as log]))

(def config
  {:timeout (or (environ/env :lkb-timeout) 10) ; timeout in seconds to wait for long polling
   :sleep (or (environ/env :lkb-sleep) 10000); sleep in milliseconds between polls
   :searches (or (environ/env :lkb-searches) "/tmp/lemme-know-bot-searches.edn")})

(comment
  (println "config is:" config))

(defn load-edn
  "Load edn files from an io/reader `source` (filename or io/resource).
   Orig fn: https://clojuredocs.org/clojure.edn/read#example-5a68f384e4b09621d9f53a79"
  [source]
  (try
    (with-open [r (io/reader source)]
      (edn/read (java.io.PushbackReader. r)))

    (catch java.io.IOException e
      (log/errorf "couldn't open '%s': %s" source (.getMessage e)))
    (catch RuntimeException e
      (log/errorf "error parsing edn file '%s': %s" source (.getMessage e)))))

(defn save-file
  "Save a data structure to a file."
  [content dest]
  (try
    (spit dest content)

    (catch java.io.IOException e
      (log/errorf "couldn't write to '%s': %s" dest (.getMessage e)))))
