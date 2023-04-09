# grabsky/commands
[![](https://github.com/Grabsky/commands/actions/workflows/gradle.yml/badge.svg)](https://github.com/Grabsky/commands/actions/workflows/gradle.yml)
[![](https://www.codefactor.io/repository/github/grabsky/commands/badge/main)](https://www.codefactor.io/repository/github/grabsky/commands/overview/main)  
Simple, no non-sense command framework for **[Paper](https://github.com/PaperMC/Paper)** servers.

> **Warning**  
> Breaking changes are likely to happen before a stable release. Use at your own risk.

<br />

## Requirements
Requires **Java 17** (or higher) and **Paper 1.19.4** (or higher).

<br />

## Getting Started
To use this project in your plugin, add following repository:
```groovy
repositories {
    // Snapshots repository. Nothing is published to the main repository until a stable release.
    maven { url = "https://repo.grabsky.cloud/snapshots" }
}
```
Then specify dependency:
```groovy
dependencies {
    // Snapshots use first seven (7) characters of commit hash as a version.
    // NOTE: Only pushed (built) commits are available in the repository.
    implementation("cloud.grabsky:commands:[_VERSION_]")
}
```
Consider **[relocating](https://imperceptiblethoughts.com/shadow/configuration/relocation/)** to prevent version mismatch issues. This can be ignored if your plugin is a **[Paper plugin](https://docs.papermc.io/paper/dev/getting-started/paper-plugins)** with **[isolated classloader](https://docs.papermc.io/paper/dev/getting-started/paper-plugins#classloading-isolation)**.

<br />

## Documentation
Documentation and examples are available **[here](https://github.com/Grabsky/commands/blob/main/DOCS.md)**.

<br />

## Building (Linux)
```bash
# Cloning repository
$ git clone https://github.com/Grabsky/commands.git
# Entering cloned repository
$ cd ./commands
# Compiling and publishing to maven local
$ ./gradlew clean publishToMavenLocal
```

<br />

## Contributing
This project is open for contributions. Help in regards of improving performance, adding new features or fixing bugs is greatly appreciated.
