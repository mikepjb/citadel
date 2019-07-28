(ns citadel.core-test
  (:require [clojure.test :refer [testing deftest is]]
            [citadel.core :as citadel]))

(deftest pacman-interop
  (testing "detecting if a package is installed"
    (is (citadel/exists? "bash"))
    (is (not (citadel/exists? "not-a-program")))))
