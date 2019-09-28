(ns citadel.openbox
  "Responsible for creating openbox configuration from Clojure data structures."
  (:require [clojure.data.xml :refer :all]
            [clojure.walk :refer [postwalk]]
            [camel-snake-kebab.core :refer [->camelCase]]))

(defn config []
  (indent-str (element :foo {:foo-attr "foo value"})))

(defn has-underscore? [k] (.contains (name k) "_"))

(defn camel-unless-underscore [e]
  (if (has-underscore? e) e (->camelCase e)))

(defn convert-keywords
  "Openbox xml files use a mix of underscore and snake-cased keywords, we want
  hypenated keywords as is traditional in Clojure."
  [config]
  (postwalk (fn [e]
              (if (keyword? e)
                (->> e
                     ;; replace keyword or return if not found in lookup
                     ({:openbox-config :openbox_config
                       :screen-edge-strength :screen_edge_strength} e)
                     camel-unless-underscore)
                e))
            config))

(defn rc []
  (indent-str
   (sexp-as-element
    (convert-keywords 
     [:openbox-config {:xmlns "http://openbox.org/3.4/rc"
                       :xmlns:xi "http://www.w3.org/2001/XInclude"}
      [:resistance
       [:strength 10]
       [:screen-edge-strength 20]]
      [:focus
       [:focus-new "yes"]
       [:follow-mouse "no"]]]))))
