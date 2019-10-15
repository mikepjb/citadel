# Design

Citadel is a tool for setting up and configuring your Arch Linux installation.

At a high level it should take a map of configurations and keep your system up to date.
Specifically 'up to date' means packages are installed and configurations are applied according to a provided system.edn file.

Ideally this would also apply to a new live Arch system where you could just wget the binary.
Even more ideally, we can create custom boot images.

## Proposed tasks

- update (takes system.edn and ensures)

## Tasks

- Check for internet connection
- Display output of external processes (out only)
- Display output of external processes (stream?)
- Display output of external processes (color code log like logrus for error?)
- Include set of default applications
- Keep installed packages lean (and clean package/notify current size?)
