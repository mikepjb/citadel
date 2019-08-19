(ns citadel.core
  (:require [clojure.java.shell :refer [sh]])
  (:refer-clojure :exclude [ensure]))

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
   {:name "noto-fonts"}
   {:name "xautolock"}
   {:name "bolt"}
   {:name "inkscape"}])

(defn refresh-database
  "Loads fresh information about available Arch packages."
  []
  (sh "pacman" "-Sy"))

(defn exists?
  [package-name]
  (zero? (:exit (sh "pacman" "-Qk" package-name))))

(defn install
  [package-name]
  (let [process (sh "sudo" "pacman" "-S" "--noconfirm" package-name)]
    (println process)
    (zero? (:exit process))))

(defn ensure
  [package-name]
  (when-not (exists? package-name)
    (install package-name)))

(defn ensure-all [packages]
  (->> packages
       (map :name)
       (map ensure)
       println))

(defn -main
  "Entrypoint for "
  []
  (ensure-all packages)
  (shutdown-agents)) ;; some processes hang around when using sudo
