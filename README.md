<img src="citadel-logo.svg" alt="Citadel Logo" width="128" height="128">

# Citadel

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
