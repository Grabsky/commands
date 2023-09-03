# commands
<span>
    <a href=""><img alt="Maven metadata URL" src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.grabsky.cloud%2Freleases%2Fcloud%2Fgrabsky%2Fcommands%2Fmaven-metadata.xml&style=for-the-badge&logo=gradle&label=%20"></a>
    <a href=""><img alt="GitHub Workflow Status (with event)" src="https://img.shields.io/github/actions/workflow/status/Grabsky/commands/gradle.yml?style=for-the-badge&logo=github&logoColor=white&label=%20"></a>
    <a href=""><img alt="CodeFactor Grade" src="https://img.shields.io/codefactor/grade/github/Grabsky/commands/main?style=for-the-badge&logo=codefactor&logoColor=white&label=%20"></a>
</span>
<p></p>

Simple, no non-sense command framework for **[Paper](https://github.com/PaperMC/Paper)** servers.

Brigadier is not currently supported, nor is it guaranteed to be supported in the future.

<br />

## Requirements
Requires **Java 17** (or higher) and **Paper 1.20.1** (or higher).

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
    implementation 'cloud.grabsky:commands:[_VERSION_]'
}
```
You can also use [GitHub Packages](https://github.com/Grabsky/commands/packages/) - read more about that [here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package).


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
