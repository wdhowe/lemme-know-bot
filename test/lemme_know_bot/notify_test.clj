(ns lemme-know-bot.notify-test
  (:require [clojure.test :refer [are deftest is testing]]
            [lemme-know-bot.notify :as notify]))

(deftest add-search!-test
  (testing "Test adding searches."
    (let [;test chat-id add by integer
          one-search (notify/add-search! 123 "yoda" "hello")
          ;add the rest of the chat-ids by string
          two-searches (notify/add-search! "123" "yoda" "hi there")
          three-searches (notify/add-search! "123" "luke" "ahoy")
          dupe-search (notify/add-search! "123" "luke" "ahoy")]

      ;; check text after adding 2 searches
      (is (= "hello" (:text (first one-search))))
      (is (= "hi there" (:text (second two-searches))))

      ;; count after 3rd search
      (is (= 3 (count three-searches)))

      ;; count after a duplicate
      (is (= 4 (count dupe-search))))))

(deftest clean-searches!-test
  (testing "Test cleaning up duplicates."
    (let [clean-search (notify/clean-searches!)]
    ;; count after removing duplicates
      (is (= 3 (count clean-search))))))

(deftest list-searches-test
  (testing "Test listing searches."
    (is (= 1 (count (notify/list-searches "123" "luke"))))))

(deftest load-searches!-test
  (testing "Testing load-searches! with different data structures."
    (are [func result] (= func result)

      ;; count is 5 due to previous test's manipulation of the atom
      (count (notify/load-searches! [{:chat-id "123" :user "user1", :text "hello"}
                                     {:chat-id "123" :user "user1", :text "hi there"}])) 5

      (count (notify/load-searches! {:user "user1", :text "invalid"})) 0

      (count (notify/load-searches! "invalid")) 0)))

(deftest remove-search!-test
  (testing "Test removing a search item."
    (let [rm-search (notify/remove-search! "123" "yoda" "hi there")]
      ;; count after removing another search
      (is (= 4 (count rm-search))))))
