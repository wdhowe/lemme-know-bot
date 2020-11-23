(ns lemme-know-bot.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [lemme-know-bot.core :as lemme]))

(deftest create-bot-test
  (testing "Test creating a bot."
    (let [mybot (lemme/create-bot "123")]
      (is (:bot-token mybot) "123"))))
