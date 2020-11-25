(ns lemme-know-bot.notify-test
  (:require [clojure.test :refer [are deftest is testing]]
            [lemme-know-bot.notify :as notify]))

(deftest searches-test
  (testing "Testing searches atom."
    (let [one-search (notify/add-search! "yoda" "hello")
          two-searches (notify/add-search! "yoda" "hi there")
          three-searches (notify/add-search! "luke" "ahoy")
          dupe-search (notify/add-search! "luke" "ahoy")
          clean-search (notify/clean-searches!)
          rm-search (notify/remove-search! "yoda" "hi there")]

      ;; check text after adding 1 and 2 searches
      (is (= "hello" (:text (first one-search))))
      (is (= "hi there" (:text (second two-searches))))

      ;; check total and user specific count after 3rd search
      (is (= 3 (count three-searches)))
      (is (= 1 (count (notify/list-searches "luke"))))

      ;; count after a duplicate
      (is (= 4 (count dupe-search)))

      ;; count after removing duplicates
      (is (= 3 (count clean-search)))

      ;; count after removing another search
      (is (= 2 (count rm-search))))))

(deftest load-searches!-test
  (testing "Testing load-searches! with different data structures."
    (are [func result] (= func result)

      ;; count is 4 due to previous test's manipulation of the atom
      (count (notify/load-searches! [{:user "user1", :text "hello"}
                                     {:user "user1", :text "hi there"}])) 4

      (count (notify/load-searches! {:user "user1", :text "invalid"})) 0

      (count (notify/load-searches! "invalid")) 0)))
