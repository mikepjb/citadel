(ns citadel.core
  (:require [clojure.java.shell :refer [sh]]))

;; Tasks for Citadel
;; 1. Install selection of packages
;; 2. Warn/Remove undeclared packages
;; 3. Link configuration files from this repo => their destinations

(def packages
  [{:name "clojure"}
   {:name "ripgrep"}
   {:name "openjdk10-src" :comment "for Clojure source jumping"}])

(defn exists?
  [package-name]
  (= 0 (:exit (sh "pacman" "-Qk" package-name))))

