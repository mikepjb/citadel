(ns citadel.core
  (:require [clojure.java.shell :refer [with-sh-dir sh]]
            [me.raynes.conch.low-level :as c]
            [clojure.java.io :as io]
            [citadel.check :as check]
            [citadel.essential :as essential]
            [clojure.string :refer [trim]])
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

(defn install-aur
  "Installs a package from the AUR, given it's name and git url.
  This does not support installing other AUR packages the target depends on."
  [package-name url]
  (let [temp-dir (trim (:out (sh "mktemp" "-d")))
        package-dir (str temp-dir "/" package-name)]
    (println package-dir)
    (sh "git" "clone" url package-dir)
    (when (.exists (io/as-file package-dir))
      (with-sh-dir package-dir
        (let [p (sh "makepkg" "-si" "--noconfirm")]
          (future (c/stream-to-out p :out))))
      (sh "rm" package-dir))))

(defn ensure
  [package-name]
  (when-not (exists? package-name)
    (install package-name)))

(defn ensure-aur
  [package-name url]
  (when-not (exists? package-name)
    (install-aur package-name url)))

(def aur-packages (fn [[_ m]] (contains? m :aur/url)))

(defn read-official-deps
  "Returns a list of deps that are available in the official repositories."
  [system-map]
  (map first (remove aur-packages (:deps system-map))))

(defn read-aur-deps
  "Returns a list of aur package names->urls."
  [system-map]
  (into {} (map (juxt (fn [x] (name (first x))) (fn [x] (:aur/url (second x))))
                (filter aur-packages (:deps system-map)))))

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
        official-deps (map str (read-official-deps merged-map))
        aur-deps      (read-aur-deps merged-map)]
    (println "--> Reading from" map-path)
    (println official-deps)
    (when connected?
      (refresh-database)
      (doseq [dep official-deps]
        (ensure dep))
      (doseq [[pname purl] aur-deps]
        (ensure-aur pname purl))
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
