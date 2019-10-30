(ns citadel.essential
  "Contains essential configuration that each system needs.")

(def deps
  '{pacgraph {} ;; probably required by citadel in future for transient deps
    sudo {}
    xterm {}
    xorg-xmessage {}
    xorg-fonts-75dpi {}
    xorg-fonts-100dpi {}
    xorg-xbacklight {}
    xorg-xev {}
    xorg-xkbevd {}
    xorg-xkbutils {}
    xorg-xrandr {}
    xorg-server-xvfb {}
    xorg-xinput {}
    xf86-video-vesa {} ;; generic backup driver
    nvidia {}
    slock {}
    xautolock {}
    light {}
    xclip {}
    acpi {}
    rsync {}
    unzip {}
    zip {}
    unrar {}
    readline {}
    make {}
    cmake {}})

(defn with-deps [system-map]
  (merge-with into {:deps deps} system-map))
