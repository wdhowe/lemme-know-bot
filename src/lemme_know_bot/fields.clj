(ns lemme-know-bot.fields
  "Functions that retrieve message fields."
  (:gen-class))

(defn chat-id
  "Returns the chat-id from a message."
  [msg]
  (-> msg
      :message
      :chat
      :id))

(defn chat-results
  "Get results from the chat updates map.
   Returns a list of maps."
  [msg]
  (-> msg
      :result))

(defn from-user
  "Returns the username of the user that the message is from."
  [msg]
  (-> msg
      :message
      :from
      :username))

(defn text
  "Retrieve the text part of the message."
  [msg]
  (-> msg
      :message
      :text))

(defn update-id
  "Returns the update_id part of the message."
  [msg]
  (-> msg
      :update_id))

(comment
  (def test-group-message
    "A sample group chat message with text."
    {:update_id 970209698
     :message
     {:message_id 19
      :from {:id 123, :is_bot false, :first_name "Bill", :last_name "Howe", :username "myuser"}
      :chat {:id -456, :title "MyGroup", :type "group", :all_members_are_administrators true}
      :date 1606101165
      :text "/start"}})

  (def test-group-system
    "Test a system message in a group chat - no text field."
    {:update_id 862905780
     :message
     {:message_id 161
      :from {:id 123, :is_bot false, :first_name "Bill", :last_name "Howe", :username "myuser", :language_code "en"}
      :chat {:id -456, :title "MyGroup", :type "group", :all_members_are_administrators true}
      :date 1606020445
      :group_chat_created true}})

  (def test-updates-msg
    "Data back from a getUpdates call to the Telegram API."
    {:ok true
     :result [{:update_id 862905848
               :message {:message_id 286
                         :from {:id 123, :is_bot false
                                :first_name "Bill", :last_name "Howe"
                                :username "myuser", :language_code "en"}
                         :chat {:id -456, :title "MyGroup"
                                :type "group", :all_members_are_administrators true}
                         :date 1606085114, :text "hello"}}]})

  (chat-id test-group-message)
  (from-user test-group-message)
  (text test-group-message)
  (update-id test-group-message)

  (chat-id test-group-system)
  (from-user test-group-system)
  (text test-group-system)
  (update-id test-group-system)

  (chat-results test-updates-msg))
