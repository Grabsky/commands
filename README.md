# grabsky/commands
[![](https://github.com/Grabsky/commands/actions/workflows/gradle.yml/badge.svg)](https://github.com/Grabsky/commands/actions/workflows/gradle.yml)
[![](https://www.codefactor.io/repository/github/grabsky/commands/badge/main)](https://www.codefactor.io/repository/github/grabsky/commands/overview/main)  
Simple, no non-sense command framework for **[Paper](https://github.com/PaperMC/Paper)** servers. It is still under development and **should not** be used on production servers.

<br />

## Requirements
Requires **Java 17** (or higher) and **Paper 1.19.3** (or higher).

<br />

## Getting Started
To use this project in your plugin, add following repository:
```groovy
repositories {
    maven { url = 'https://repo.grabsky.cloud/releases' }
}
```
Then specify dependency:
```groovy
dependencies {
    implementation 'cloud.grabsky:commands:[version]'
}
```
Use **[relocation](https://imperceptiblethoughts.com/shadow/configuration/relocation/)** to prevent issues with plugins depending on different versions of the framework.

<br />

## Building (Linux)
```bash
# Cloning repository
$ git clone https://github.com/Grabsky/configuration.git
# Entering cloned repository
$ cd ./configuration
# Compiling and publishing to maven local
$ ./gradlew clean test publishToMavenLocal
```

<br />

## Contributing
This project is open for contributions. Help in regards of improving performance, adding new features or fixing bugs is greatly appreciated.
