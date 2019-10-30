<img src="citadel-logo.svg" alt="Citadel Logo" width="128" height="128">

# Citadel

[![CircleCI](https://circleci.com/gh/mikepjb/citadel.svg?style=svg)](https://circleci.com/gh/mikepjb/citadel)

A tool for Arch Linux, it helps you with 3 tasks:

1. Prepare a bootable Arch Linux image with X11, i3 and a few nice to haves, making initial setup easier.
2. Install Arch Linux, leading you through some best practices along the way.
3. Update your system according to a single configuration `system.edn` file.

### Progress

This project is incomplete, here is the progress:

- [x] Install official packages via `system.edn`
- [x] Include essential system map to merge with user provided version
- [ ] Specify linux-lts (kernel choice) in `system.edn`
- [ ] Create `system.edn` on fresh install
- [ ] use repo url e.g toolkit to setup new machine, citadel create https://github.com/mikepjb/toolkit.git or pipe edn?
- [ ] BP: Check for encrypted root disk
- [ ] BP: Check for swap file
- [ ] BP: Check for automatic wifi connection (netctl-auto)
- [x] BP: ensure intel-ucode exists for processors that need it (any intel for now)
- [ ] Ensure Gimp font is set to a suitable size (gtkrc 2)
- [ ] write system font to all config files (e.g gtkrc 2, gtkrc 3.. whereever else? i3 config?)
- [ ] setting console font too.
- [ ] Mark versions for packages in `system.edn`
- [ ] Install AUR packages via `system.edn`

### Checks
- [ ] is using modesetting (better than intel)
- [ ] is a member of video group? (allows changing brightness using modesetting)
- [ ] is power button set to inactive (except for forcing) to prevent accidental shutdown?


---

## Fake News.. (old description)

A citadel is the core fortified area of a town, city or in this case a computer system.

This citadel is a collection of code that sets up an Arch Linux system as a development environment.

### Getting Started

You can with just `clj` but in most cases you will want to integrate with your editor of choice over a nREPL connection, this should do the trick:  
`clj -Sdeps '{:deps {nrepl/nrepl {:mvn/version "0.5.3"}}}' -m nrepl.cmdline`

Citadel can be run from the command line in this directory with:
`clj -m citadel.core ~/toolkit/system.edn`

### System Map Example

```
{:deps
 {ripgrep {}
  telegram-desktop {}
  openjdk8-src {:comment "for Clojure source jumping"}
  toolkit {:git "github.com/mikepjb/toolkit"}}}
```

### Development

You can build a new version of Citadel by running the make command.

#### References

[https://www.reddit.com/r/archlinux/comments/5r5ep8/make_your_arch_fonts_beautiful_easily/]
[https://wiki.archlinux.org/index.php/General_recommendations]
