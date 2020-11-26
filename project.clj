(defproject lemme-know-bot "0.2.0"
  :description "A Telegram Bot that mentions you when the specified text in a chat matches."
  :url "https://github.com/wdhowe/lemme-know-bot"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[com.taoensso/timbre "5.1.0"]
                 [environ "1.2.0"]
                 [org.clojure/clojure "1.10.1"]
                 [telegrambot-lib "0.3.0"]]
  :main ^:skip-aot lemme-know-bot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev [:project/dev :profiles/dev]
             :test [:project/test :profiles/test]
             ;; only edit :profiles/* in profiles.clj
             :profiles/dev {}
             :profiles/test {}
             :project/dev {:env {:log-level "debug"}
                           :plugins [[lein-environ "1.1.0"]]}
             :project/test {:env {:log-level "info"}
                            :plugins [[lein-environ "1.1.0"]]}})
