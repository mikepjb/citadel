(ns citadel.openbox
  "Responsible for creating openbox configuration from Clojure data structures."
  (:require [clojure.data.xml :refer :all]))

(defn config []
  (indent-str (element :foo {:foo-attr "foo value"})))
