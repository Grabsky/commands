# commands

[![Latest Release](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fapi.github.com%2Frepos%2FGrabsky%2Fcommands%2Freleases%2Flatest&query=tag_name&logo=gradle&style=for-the-badge&label=%20&labelColor=%231C2128&color=%23454F5A)](https://github.com/Grabsky/commands/packages/1935522)
[![Build Status](https://img.shields.io/github/actions/workflow/status/Grabsky/commands/gradle.yml?style=for-the-badge&logo=github&logoColor=white&label=%20)](https://github.com/Grabsky/commands/actions)
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/Grabsky/commands/main?style=for-the-badge&logo=codefactor&logoColor=white&label=%20)](https://www.codefactor.io/repository/github/grabsky/commands)

Simple, no non-sense command framework for **[Paper](https://github.com/PaperMC/Paper)** servers.

Brigadier is not currently supported, nor is it guaranteed to be supported in the future. Contributions are welcome.

<br />

## Requirements
Requires **Java 17** (or higher) and **Paper 1.20.1** (or higher).

<br />

## Getting Started
Library is published to the **[GitHub Packages Registry](https://github.com/Grabsky/commands/packages/)** and may require additional configuration. You can find more details **[here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package)**.
```groovy
repositories {
    maven { url = "https://maven.pkg.github.com/grabsky/commands"
        credentials {
            username = findProperty("gpr.actor") ?: System.getenv("GITHUB_ACTOR")
            password = findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

```groovy
dependencies {
    implementation 'cloud.grabsky:commands:[_VERSION_]'
}
```

<br />

## Documentation
Documentation and examples are available **[here](https://github.com/Grabsky/commands/blob/main/DOCS.md)**.

<br />

## Building
```bash
# Cloning the repository.
$ git clone https://github.com/Grabsky/commands.git
# Entering the cloned repository.
$ cd commands
# Compiling and publishing to maven local.
$ gradlew clean publishToMavenLocal
```

<br />

## Contributing
This project is open for contributions. I'm currently looking for help in implementing presence and parameter flags for commands.