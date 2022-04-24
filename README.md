# YamlConfiguration

YamlConfiguration is a library for creating YAML files for configurations to be used in Java programs.<br/>
It is based off of [SpigotMC's Bukkit](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/configuration) configuration sub-project, which stems from the original [Bukkit Project](https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/).

## Download

You can download the latest version of the library from [here](https://github.com/bspfsystems/YamlConfiguration/releases/latest/).

The latest release is 1.0.11.<br />
The latest snapshot is 1.1.0-SNAPSHOT.

## Build from Source

YamlConfiguration uses [Apache Maven](https://maven.apache.org/) to build and handle dependencies.

### Requirements

- Java Development Kit (JDK) 8 or higher
- Git
- Apache Maven

### Compile / Build

Run the following commands to build the library `.jar` file:
```
git clone https://github.com/bspfsystems/YamlConfiguration.git
cd YamlConfiguration/
mvn clean install
```

The `.jar` file will be located in the `target/` folder.

## Installation

The main purpose of YamlConfiguration is to provide an easy method of converting Java Objects to a serialized YAML configuration file, and back again. It can be used as a library in any downstream projects to access the library's capabilities.

### Adding as a Dependency

To add YamlConfiguration as a dependency to your project, use one of the following common methods (you may use others that exist, these are the common ones):

**Maven:**<br />
Include the following in your `pom.xml` file:<br />
```
...
<repositories>
  <repository>
    <id>sonatype-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
  </repository>
  ...
</repositories>

...

<dependencies>
  <dependency>
    <groupId>org.bspfsystems</groupId>
    <artifactId>yamlconfiguration</artifactId>
    <version>1.0.12-SNAPSHOT</version>
    <scope>compile</scope>
  </dependency>
  ...
</dependencies>
...
```

**Gradle:**<br />
Include the following in your `build.gradle` file:<br />
```
...
repositories {
    mavenCentral()
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots/"
        ...
    }
    ...
}

...

dependencies {
    include implementation("org.bspfsystems:yamlconfiguration:${project.yamlconfiguration_version}")
    ...
}
```

Also include the following in your `gradle.properties` file:<br />
```
...
yamlconfiguration_version = 1.0.12-SNAPSHOT
...
```

### Install in a Library Folder

If your use of YamlConfiguration requires the `.jar` file in a specific library folder, you can obtain a copy (see Download or Build from Source) and simply place the file in the folder.

### Usage

To create a new YamlConfiguration in your project, please include the following line in your code:
- `YamlConfiguration config = new YamlConfiguration();`

### Javadocs

The API Javadocs can be found [here](https://bspfsystems.org/docs/yamlconfiguration/), hosted by [javadoc.io](https://javadoc.io/).

## Contributing

### Pull Requests

Contributions to the project are welcome. YamlConfiguration is a free and open source software project, created in the hopes that the community would find ways to improve it. If you make any improvements or other enhancements to YamlConfiguration, we ask that you submit a Pull Request to merge the changes back upstream. We would enjoy the opportunity to give those improvements back to the wider community.

Various types of contributions are welcome, including (but not limited to):
- Security updates / patches
- Bug fixes
- Feature enhancements

We reserve the right to not include a contribution in the project if the contribution does not add anything substantive or otherwise reduces the functionality of YamlConfiguration in a non-desirable way. That said, the idea of having free and open source software was that contributions would be accepted, and discussions over a potential contribution are welcome.

For licensing questions, please see the Licensing section.

### Project Layout

YamlConfiguration somewhat follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). This is not the definitive coding style of the project. Generally, it is best to try to copy the style of coding found in the class that you are editing.

## Support / Issues

Issues can be reported [here in GitHub](https://github.com/bspfsystems/YamlConfiguration/issues/).

### First Steps

Before creating an issue, please search to see if anyone else has reported the same issue. Don't forget to search the closed issues. It is much easier for us (and will get you a faster response) to handle a single issue that affects multiple users than it is to have to deal with duplicates.

There is also a chance that your issue has been resolved previously. In this case, you can (ideally) find the answer to your problem without having to ask (new version of YamlConfiguration, configuration update, etc).

### Creating an Issue

If no one has reported the issue previously, or the solution is not apparent, please open a new issue. When creating the issue, please give it a descriptive title (no "It's not working", please), and put as much detail into the description as possible. The more details you add, the easier it becomes for us to solve the issue. Helpful items may include:
- A descriptive title for the issue
- The version of YamlConfiguration you are using
- Logs and/or stack traces
- Any steps to reproducing the issue
- Anything else that might be helpful in solving your issue.

_Note:_ Please redact any Personally-Identifiable Information (PII) when you create your issue. These may appear in logs or stack traces. Examples include (but are not limited to):
- Real names of people / companies
- Usernames of accounts on computers (may appear in logs or stack traces)
- IP addresses / hostnames
- etc.

If you are not sure, you can always redact or otherwise change the data.

### Non-Acceptable Issues

Issues such as "I need help" or "It doesn't work" will not be addressed and/or will be closed with no assistance given. These type of issues do not have any meaningful details to properly address the problem. Other issues that will not be addressed and/or closed without help include (but are not limited to):
- How to install YamlConfiguration (explained in README)
- How to use YamlConfiguration as a dependency (explained in README)
- How to create libraries
- How to set up a development environment
- How to install libraries
- Other issues of similar nature...


This is not a help forum for software development or associated issues. Other resources, such as [Google](https://www.google.com/), should have answers to most questions not related to YamlConfiguration.

## Licensing

YamlConfiguration uses the following licenses:
- [The GNU General Public License, Version 3](https://www.gnu.org/licences/gpl-3.0.en.html)

### Contributions & Licensing

Contributions to the project will remain licensed under the respective license, as defined by the particular license. Copyright/ownership of the contributions shall be governed by the license. The use of an open source license in the hopes that contributions to the project will have better clarity on legal rights of those contributions.

_Please Note: This is not legal advice. If you are unsure on what your rights are, please consult a lawyer._
