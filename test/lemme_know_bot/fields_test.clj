(ns lemme-know-bot.fields-test
  (:require [clojure.test :refer [deftest is testing]]
            [lemme-know-bot.fields :as fields]))

(def test-group-msg
  "A sample group message with text contents."
  {:update_id 970209698
   :message
   {:message_id 19
    :from {:id 123, :is_bot false, :first_name "Bill", :last_name "Howe", :username "myuser"}
    :chat {:id -456, :title "MyGroup", :type "group", :all_members_are_administrators true}
    :date 1606101165
    :text "/start"}})

(def test-group-system
  "A sample group system message with no text contents."
  {:update_id 862905780
   :message
   {:message_id 161
    :from {:id 123, :is_bot false, :first_name "Bill", :last_name "Howe", :username "myuser", :language_code "en"}
    :chat {:id -456, :title "MyGroup", :type "group", :all_members_are_administrators true}
    :date 1606020445
    :group_chat_created true}})

(def test-updates-msg
  "Sample data from a get-updates call to the Telegram API.
   Group chat example."
  {:ok true
   :result [{:update_id 862905848
             :message {:message_id 286
                       :from {:id 123, :is_bot false
                              :first_name "Bill", :last_name "Howe"
                              :username "myuser", :language_code "en"}
                       :chat {:id -456, :title "MyGroup"
                              :type "group", :all_members_are_administrators true}
                       :date 1606085114, :text "hello"}}]})

(deftest chat-id-test
  (testing "Check the chat id in messages."
    (is (= -456 (fields/chat-id test-group-msg)))))

(deftest chat-results-test
  (testing "Check the get-updates response object for results."
    (let [resp (fields/chat-results test-updates-msg)]
      (is (= 1 (count resp)))
      (is (= "hello" (-> resp
                         (first)
                         :message
                         :text))))))

(deftest from-user-test
  (testing "Check for the from username in the message."
    (is (= "myuser" (fields/from-user test-group-msg)))))

(deftest text-test
  (testing "Check for the text in a test message."
    (is (= "/start" (fields/text test-group-msg)))
    (is (= nil (fields/text test-group-system)))))

(deftest update-id-test
  (testing "Check for the update_id in a test message."
    (is (= 970209698 (fields/update-id test-group-msg)))))
