(ns citadel.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]
            [citadel.check :as check])
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

(defn system-update
  "Take the path of a system.edn file and update the system based on this."
  [map-path]
  (let [system-map (read-string (slurp (io/file map-path)))
        deps       (map str (read-deps system-map))]
    (println "--> Reading from" map-path)
    (println deps)
    (when connected?
      (refresh-database)
      (doseq [dep deps]
        (ensure dep)))))

(def help-message
  "usage:  citadel <operation> [...]
  operations:
      citadel update <path to a system.edn file>
      citadel check
      citadel help")

(defn -main
  "Entrypoint for "
  [& args]
  (if-not (empty? args)
    (case (keyword (first args))
      :update (system-update (second args))
      :check  (check/all)
      :help (println help-message)
      (println "that is not a valid argument.")
      )
    (println (str "no operation specified\n\n" help-message)))
  (shutdown-agents)) ;; some processes hang around when using sudo
