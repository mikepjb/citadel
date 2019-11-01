(ns citadel.core-test
  (:require [clojure.test :refer [testing deftest is]]
            [citadel.core :as citadel]
            [citadel.essential :as essential]
            [clojure.java.io :as io]))

(deftest pacman-interop
  (testing "detecting if a package is installed"
    (is (citadel/exists? "bash"))
    (is (not (citadel/exists? "not-a-program")))))

(deftest ingests-system-edn
  (testing "listing all dependencies"
    (is (= (citadel/read-official-deps (read-string (slurp (io/resource "test-system.edn"))))
           (list 'ripgrep 'telegram-desktop 'openjdk8-src)))))

(deftest contains-entries-from-essential-map
  (testing "contains sudo which is not in test-system.edn"
    (is (some #{'sudo}
              (citadel/read-official-deps (essential/with-deps (read-string (slurp (io/resource "test-system.edn")))))))))

(deftest retains-entries-from-given-map
  (testing "contains ripgrep which is in test-system.edn"
    (is (some #{'ripgrep}
         (citadel/read-official-deps (essential/with-deps (read-string (slurp (io/resource "test-system.edn")))))))))

