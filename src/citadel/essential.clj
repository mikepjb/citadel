(ns citadel.essential
  "Contains essential configuration that each system needs.")

(def deps
  '{pacgraph {} ;; probably required by citadel in future for transient deps
    sudo {}
    xterm {}
    xorg-xinit {}
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
    xorg-xdpyinfo {} ;; X utility
    xorg-xkill {} ;; point and kill X windows
    man {}
    xf86-video-vesa {} ;; generic backup driver
    nvidia {}
    slock {}
    openssh {}
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
    cmake {}
    linux-firmware {} ;; required for loading ath10k wifi.
    })

(defn with-deps [system-map]
  (merge-with into {:deps deps} system-map))
