(ns citadel.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io])
  (:refer-clojure :exclude [ensure])
  (:gen-class))

;; Tasks for Citadel
;; 1. Install selection of packages
;; 2. Warn/Remove undeclared packages
;; 3. Link configuration files from this repo => their destinations

(def packages
  [{:name "clojure"}
   {:name "ripgrep"}
   {:name "telegram-desktop"}
   {:name "openjdk8-src" :comment "for Clojure source jumping"}
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

(defn read-deps [system-map]
  (map first (:deps system-map)))

;; XXX this is now pretty big, since -main isn't tested let's thin this out.
(defn -main
  "Entrypoint for "
  [& args]
  (if-not (empty? args)
    (let [map-path   (first args)
          system-map (read-string (slurp (io/file map-path)))
          deps       (map str (read-deps system-map))]
      (println "--> Reading from" map-path)
      (println deps)
      (doseq [dep deps]
        (ensure dep))))
  (shutdown-agents)) ;; some processes hang around when using sudo
