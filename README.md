# YamlConfiguration

YamlConfiguration is a library for creating .yml files for configurations to be used in Java programs.<br/>
It is based off of [SpigotMC's Bukkit](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/configuration) configuration.

## Installation

The latest release is 1.0.3.
The latest snapshot is 1.0.3-SNAPSHOT.

##### Gradle:
Include the following in your `build.gradle` file:

```
repostories {
	mavenCentral()
	maven {
		url "https://oss.sonatype.org/content/repositories/snapshots/"
	}
}

dependencies {
	compile "org.bspfsystems:yamlconfiguration:${project.yamlconfiguration_version}"
	include("org.bspfsystems:yamlconfiguration:${project.yamlconfiguration_version}") {
		transitive = false
	}
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
    <version>1.0.3-SNAPSHOT</version>
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
YamlConfiguration config = new YamlConfiguration;
```

For more documentation on how to use the config that you just created, please checkout the [Javadocs](https://bspfsystems.org/YamlConfiguration/).