FROM archlinux/base

RUN pacman -Sy --noconfirm clojure && pacman -Scc --noconfirm
