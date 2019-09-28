(ns citadel.openbox-test
  (:require [clojure.test :refer [testing deftest is]]
            [citadel.openbox :as ob]))

(deftest creates-xml
  (testing "can we create xml from a clojure data structure"
    (is (= (ob/config)
           "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<foo foo-attr=\"foo value\"/>\n"))))
