(ns lemme-know-bot.status-test
  (:require [clojure.test :refer [deftest is testing]]
            [lemme-know-bot.status :as status]))

(deftest update-id-test
  (testing "Testing update-id atom."
    (let [start-id (status/set-id! 7)
          inc-id (status/inc-id!)
          next-id (status/next-id)]
      (is (= 7 start-id))
      (is (= 8 inc-id))
      (is (= 9 next-id)))))
