(ns citadel.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]
            [citadel.check :as check]
            [citadel.essential :as essential])
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

(defn read-official-deps
  "Returns a list of deps that are available in the official repositories."
  [system-map]
  (map first (remove (fn [[_ m]] (contains? m :aur/url)) (:deps system-map))))

(defn sync-projects
  "Updates projects under the :sync key"
  [system-map]
  (doseq [[pname pcmd] (:sync system-map)]
    (println "Updating" pname "then executing" pcmd)))

(defn system-update
  "Take the path of a system.edn file and update the system based on this."
  [map-path]
  (let [system-map    (read-string (slurp map-path))
        merged-map    (essential/with-deps system-map)
        official-deps (map str (read-official-deps merged-map))]
    (println "--> Reading from" map-path)
    (println official-deps)
    (when connected?
      (refresh-database)
      (doseq [dep official-deps]
        (ensure dep))
      (sync-projects system-map))))

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
