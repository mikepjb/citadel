(ns citadel.check
  "Make sure the system is in ship shape."
  (:require [clojure.java.shell :refer [sh]]
            [cheshire.core :refer :all]
            [clojure.string :refer [includes?]]))

(def intel?
  "Does the machine have an Intel CPU?"
  (->> (parse-string (:out (sh "lscpu" "-J")) keyword)
       :lscpu
       (filter (fn [r] (= (:field r) "Model name:")))
       first
       :data
       ((fn [x] (includes? x "Intel")))))

(def includes-microcode?
  "Are we including microcode during boot?"
  (includes? (slurp "/boot/loader/entries/arch.conf") "ucode"))

(defn notify-error [msg]
  (println msg)
  (System/exit 1))

(defn all []
  (println "Checking for Intel processor..." intel?)
  (if intel?
    (if includes-microcode?
      (println "Checking for microcode in bootloader..." includes-microcode?)
      (notify-error "Microcode 'intel-ucode' is not included in boot configuration."))))
