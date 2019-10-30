# Design

Citadel is a tool for setting up and configuring your Arch Linux installation.

At a high level it should take a map of configurations and keep your system up to date.
Specifically 'up to date' means packages are installed and configurations are applied according to a provided system.edn file.

Ideally this would also apply to a new live Arch system where you could just wget the binary.
Even more ideally, we can create custom boot images.

## Proposed tasks

- update (takes system.edn and ensures)
- check (runs a series of checks on the system to help secure/apply best practice.)

## Tasks

- [X] Check for internet connection
- [X] Conditional to receiving a varity of commands
- [X] Provide help when nothing is provided (find inspiration)
- Display output of external processes (out only)
- Display output of external processes (stream?)
- Display output of external processes (color code log like logrus for error?)
- Include set of default applications
- Keep installed packages lean (and clean package/notify current size?)
- Check secure password? (find something small to check security wise, perhaps the presence of a package?)
- Integration test (test cli input)

## Notes on suggested setup

- A swap partition is not needed, especially for systems with SSDs a swap file offers the same performance and a simpler partition table.

- linux-lts is a better default and nice to have as a backup. I've had problems with docker/virtualbox/displaylink previously on the latest kernel at various points in time.
