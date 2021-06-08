# YamlConfiguration

YamlConfiguration is a library for creating YML files for configurations to be used in Java programs.<br/>
It is based off of [SpigotMC's Bukkit](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/configuration) configuration.

## Installation

The latest release is 1.0.8.
The latest snapshot is 1.0.9-SNAPSHOT.

##### Gradle:
Include the following in your `build.gradle` file:

```
repositories {
	mavenCentral()
	maven {
		url "https://oss.sonatype.org/content/repositories/snapshots/"
	}
}

dependencies {
	include implementation("org.bspfsystems:yamlconfiguration:${project.yamlconfiguration_version}")
}
```

##### Maven:
Include the following in your `pom.xml` file:

```
<repositories>
  <repository>
    <id>sonatype-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
  <repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>org.bspfsystems</groupId>
    <artifactId>yamlconfiguration</artifactId>
    <version>1.0.9-SNAPSHOT</version>
    <scope></scope>
  </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-assembly-plugin</artifactId>
      <version>3.3.0</version>
      <configuration>
        <descriptorRefs>
          <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
      </configuration>
      <executions>
        <execution>
          <id>make-assembly</id>
          <phase>package</phase>
          <goals>
            <goal>single</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

## Usage

To create a new YamlConfiguration in your project, please include the following line in your code:

```
YamlConfiguration config = new YamlConfiguration();
```

For more documentation on how to use the config that you just created, please checkout the [Javadocs](https://javadoc.io/doc/org.bspfsystems/yamlconfiguration/latest/index.html). Please note that it might take 24 hours for the latest Javadocs to be available at the link if there was a recent release.

## Support / Issues

This repository is a derivative of the [SpigotMC Repo's Configuration Library](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/configuration). Please check there to see if the issue in this repo is present in that repo as well. If it is, please report that using SpigotMC's issues system.

If the issue is with this repository alone, or is an issue that SpigotMC has fixed but has not been fixed here, those issues can be reported [here](https://github.com/bspfsystems/YamlConfiguration/issues/).

## Licensing

This project is licensed under the GNU General Public License, version 3. A copy of the license can be obtained from [this repository](LICENSE) or from the [Free Software Foundation's site](http://www.gnu.org/licenses/gpl-3.0.en.html).
