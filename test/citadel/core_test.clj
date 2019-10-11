(ns citadel.core-test
  (:require [clojure.test :refer [testing deftest is]]
            [citadel.core :as citadel]
            [clojure.java.io :as io]))

(deftest pacman-interop
  (testing "detecting if a package is installed"
    (is (citadel/exists? "bash"))
    (is (not (citadel/exists? "not-a-program")))))

(deftest ingests-system-edn
  (testing "listing all dependencies"
    (is (= (citadel/read-deps (read-string (slurp (io/resource "test-system.edn"))))
           (list 'ripgrep 'telegram-desktop 'openjdk8-src)))))
