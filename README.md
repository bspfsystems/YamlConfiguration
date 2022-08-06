# YamlConfiguration

YamlConfiguration is a library for creating and editing YAML files for configurations to be used in Java programs. In addition, it also provides the ability to use in-memory-only Configurations for internal functions, as needed.

It is based off of [SpigotMC's Bukkit](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/configuration) configuration sub-project, which stems from the original [Bukkit Project](https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/).

## Obtaining YamlConfiguration

You can obtain a copy of YamlConfiguration via the following methods:
- Download a pre-built copy from the [Releases page](https://github.com/bspfsystems/YamlConfiguration/releases/latest/). The latest version is release 1.2.1.
- Build from source (see below).
- Include it as a dependency in your project (see the Development API section).
- 
### Build from Source

YamlConfiguration uses [Apache Maven](https://maven.apache.org/) to build and handle dependencies.

#### Requirements

- Java Development Kit (JDK) 8 or higher
- Git
- Apache Maven

#### Compile / Build

Run the following commands to build the library `.jar` file:
```
git clone https://github.com/bspfsystems/YamlConfiguration.git
cd YamlConfiguration/
mvn clean install
```

The `.jar` file will be located in the `target/` folder.

## Developer API

### Add YamlConfiguration as a Dependency

To add YamlConfiguration as a dependency to your project, use one of the following common methods (you may use others that exist, these are the common ones):

**Maven:**<br />
Include the following in your `pom.xml` file:<br />
```
<repositories>
  <repository>
    <id>sonatype-repo</id>
    <url>https://oss.sonatype.org/content/repositories/releases/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>org.bspfsystems</groupId>
    <artifactId>yamlconfiguration</artifactId>
    <version>1.2.1</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```

**Gradle:**<br />
Include the following in your `build.gradle` file:<br />
```
repositories {
    mavenCentral()
    maven {
        url "https://oss.sonatype.org/content/repositories/releases/"
    }
}

dependencies {
    implementation "org.bspfsystems:yamlconfiguration:1.2.1"
}
```

### API Examples

These are some basic usages of YamlConfiguration; for a full scope of what the library offers, please see the Javadocs section below.
```
// Load a file into memory
YamlConfiguration config = new YamlConfiguration();
try {
    config.load(new File("config.yml"));
} catch (IOException | InvalidConfigurationException e) {
    e.printStackTrace();
}

// Set a few values in the configuration
config.set("random_int", (new Random()).nextInt());
config.set("uuid", UUID.randomUUID().toString());

// Read a value
System.out.println("UUID: " + config.getString("uuid"));

// Save to a file
try {
    config.save(new File("newconfig.yml"));
} catch (IOException e) {
    e.printStackTrace();
}
```

### Javadocs

The API Javadocs can be found [here](https://bspfsystems.org/docs/yamlconfiguration/), kindly hosted by [javadoc.io](https://javadoc.io/).

## Contributing, Support, and Issues

Please check out [CONTRIBUTING.md](CONTRIBUTING.md) for more information.

## Licensing

YamlConfiguration uses the following licenses:
- [The GNU General Public License, Version 3](https://www.gnu.org/licences/gpl-3.0.en.html)

### Contributions & Licensing

Contributions to the project will remain licensed under the respective license, as defined by the particular license. Copyright/ownership of the contributions shall be governed by the license. The use of an open source license in the hopes that contributions to the project will have better clarity on legal rights of those contributions.

_Please Note: This is not legal advice. If you are unsure on what your rights are, please consult a lawyer._
