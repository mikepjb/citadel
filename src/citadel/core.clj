(ns citadel.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io])
  (:refer-clojure :exclude [ensure])
  (:gen-class))

(defn connected?
  "Are we connected to the internet?"
  []
  false)

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

(defn system-update [args]
  (let [map-path   (first args)
          system-map (read-string (slurp (io/file map-path)))
          deps       (map str (read-deps system-map))]
      (println "--> Reading from" map-path)
      (println deps)
      (when connected?
        (refresh-database)
        (doseq [dep deps]
          (ensure dep)))))

;; XXX this is now pretty big, since -main isn't tested let's thin this out.
(defn -main
  "Entrypoint for "
  [& args]
  (if-not (empty? args)
    (case (keyword (first args))
      :update (println "ok i will update now")
      :check  (println "ok i will make some basic checks")
      (println "that is not a valid argument.")
      )
    (println "No argument provided [print help]"))
  (shutdown-agents)) ;; some processes hang around when using sudo
