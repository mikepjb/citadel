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

(defn font [place name size weight]
  [:font {:place place}
   [:name name]
   [:size size]
   [:weight weight]
   [:slant "normal"]])

(defn rc []
  (indent-str
   (sexp-as-element
    (convert-keywords 
     [:openbox-config {:xmlns "http://openbox.org/3.4/rc"
                       :xmlns:xi "http://www.w3.org/2001/XInclude"}
      [:resistance [:strength 10] [:screen-edge-strength 20]]

      [:focus
       [:focus-new "yes"]
       [:follow-mouse "no"]
       [:focus-last "yes"]
       [:under-mouse "no"]
       [:focus-delay 0]
       [:raise-on-focus "no"]]
      
      [:placement
       [:policy "Smart"]
       [:center "yes"]
       [:monitor "Primary"]
       [:primaryMonitor "Active"]]
      
      [:theme
       [:name "Clearlooks"]
       [:corner-radius 0]
       [:keep-border "no"]
       [:animate-iconify "yes"]
       (font "ActiveWindow" "Noto Sans" 12 "bold")
       (font "InactiveWindow" "Noto Sans" 12 "bold")
       (font "MenuHeader" "Noto Sans" 12 "bold")
       (font "MenuItem" "Noto Sans" 12 "normal")
       (font "ActiveOnScreenDisply" "Noto Sans" 12 "bold")
       (font "InactiveOnScreenDisply" "Noto Sans" 12 "bold")
       [:title-layout "NLIMC"]
       [:desktops
        [:number 1]
        [:firstdesk 1]
        [:popup-time 0]]
       [:resize
        [:draw-contents "yes"]
        [:popup-show "Nonpixel"]
        [:popup-position "Center"]
        [:popup-fixed-position [:x 10] [:y 10]]]
       [:margins [:top 0] [:bottom 0] [:left 0] [:right 0]]]

      [:keyboard
       [:chain-quit-key "C-g"]
       [:keybind {:key "A-F4"} [:action {:name "Close"}]]
       [:keybind {:key "A-Tab"} [:action {:name "NextWindow"}
                                 [:finalactions
                                  [:action {:name "Focus"}]
                                  [:action {:name "Raise"}]
                                  [:action {:name "Unshade"}]]]]
       [:keybind {:key "A-Tab"} [:action {:name "PreviousWindow"}
                                 [:finalactions
                                  [:action {:name "Focus"}]
                                  [:action {:name "Raise"}]
                                  [:action {:name "Unshade"}]]]]

       [:keybind {:key "A-S-E"} [:action {:name "Exit"} [:prompt "no"]]]
       [:keybind {:key "A-S-N"} [:action {:name "Execute"} [:command "xterm"]]]
       [:keybind {:key "A-S-P"} [:action {:name "Execute"} [:command "emacs"]]]]]))))
