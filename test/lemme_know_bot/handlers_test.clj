(ns lemme-know-bot.handlers-test
  (:require [clojure.test :refer [are deftest testing]]
            [lemme-know-bot.handlers :as handlers]))

(deftest extract-keyword-test
  (testing "Test keyword extraction from text messages."
    (are [func result] (= func result)
      ;; no text
      (handlers/extract-keyword {:message {:text "/add@test_robot"}}) ""
      (handlers/extract-keyword {:message {:text "/add"}}) ""
      ;; spaces only
      (handlers/extract-keyword {:message {:text "/add@test_robot "}}) ""
      (handlers/extract-keyword {:message {:text "/add "}}) ""
      ;; one word
      (handlers/extract-keyword {:message {:text "/add@test_robot hello"}}) "hello"
      (handlers/extract-keyword {:message {:text "/add hello"}}) "hello"
      ;; two words
      (handlers/extract-keyword {:message {:text "/add@test_robot hello there"}}) "hello there"
      (handlers/extract-keyword {:message {:text "/add hello there"}}) "hello there")))
