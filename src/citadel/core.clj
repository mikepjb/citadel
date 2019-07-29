(ns citadel.core
  (:require [clojure.java.shell :refer [sh]]))

;; Tasks for Citadel
;; 1. Install selection of packages
;; 2. Warn/Remove undeclared packages
;; 3. Link configuration files from this repo => their destinations

(def packages
  [{:name "clojure"}
   {:name "ripgrep"}
   {:name "telegram-desktop"}
   {:name "openjdk10-src" :comment "for Clojure source jumping"}
   {:name "ttf-dejavu"}
   {:name "ttf-liberation"}
   {:name "noto-fonts"}])

;; sudo pacman -S ttf-dejavu ttf-liberation noto-fonts

(defn refresh-database
  "Loads fresh information about available Arch packages."
  []
  (sh "pacman" "-Sy"))

(defn exists?
  [package-name]
  (zero? (:exit (sh "pacman" "-Qk" package-name))))

(defn install
  [package-name]
  (zero? (:exit (sh "pacman" "-S" "--noconfirm" package-name))))

(defn ensure
  [package-name]
  (if-not (exists? package-name)
    (install package-name)))

(defn main
  "Entrypoint for "
  []
  (map #(exists? (:name %)) packages))
