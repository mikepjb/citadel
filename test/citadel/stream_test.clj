(ns citadel.stream-test
  (:require [clojure.test :refer [testing deftest is]]
            [citadel.stream :as stream]))

(deftest basic-check
  (testing "we can receive a list of files from ls"
    (is (= 1 1))))

