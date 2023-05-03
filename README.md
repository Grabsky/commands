# grabsky/commands
[![](https://img.shields.io/github/actions/workflow/status/Grabsky/commands/gradle.yml)](https://github.com/Grabsky/commands/actions/workflows/gradle.yml)
[![](https://img.shields.io/codefactor/grade/github/Grabsky/commands/main)](https://www.codefactor.io/repository/github/grabsky/commands/overview/main)
[![](https://img.shields.io/github/v/release/Grabsky/commands)](https://github.com/Grabsky/commands/releases/latest)  
Simple, no non-sense command framework for **[Paper](https://github.com/PaperMC/Paper)** servers. Project *should* be stable as of version `1.X` but expect breaking changes between future releases.

Brigadier is not currently supported, nor is it guaranteed to be supported in the future.

<br />

## Requirements
Requires **Java 17** (or higher) and **Paper 1.19.4** (or higher).

<br />

## Getting Started
To use this project in your plugin, add following repository(-ies):
```groovy
repositories {
    // Releases repository.
    maven { url = "https://repo.grabsky.cloud/releases" }
    // Snapshots repository. (unstable api)
    maven { url = "https://repo.grabsky.cloud/snapshots" }
}
```
Then specify dependency:
```groovy
dependencies {
    // Snapshots use first seven (7) characters of commit hash as a version.
    // NOTE: Only pushed (and successfully built) commits are available in the repository.
    implementation("cloud.grabsky:commands:[_VERSION_]")
}
```

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
