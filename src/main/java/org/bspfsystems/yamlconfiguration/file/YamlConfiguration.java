/*
 * This file is part of YamlConfigurtion.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2014-2020 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020 BSPF Systems, LLC (https://github.com/bspfsystems/)
 * 
 * Many of the files in this project are sourced from the Bukkit API as
 * part of the SpigotMC project (https://hub.spigotmc.org/stash/).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bspfsystems.yamlconfiguration.file;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

/**
 * An implementation of {@link Configuration} which saves all files in Yaml.
 * Note that this implementation is not synchronized.
 */
public final class YamlConfiguration extends FileConfiguration {
	
	protected static final String COMMENT_PREFIX = "# ";
	protected static final String BLANK_CONFIG = "{}\n";
	
	private final DumperOptions yamlOptions;
	private final Representer yamlRepresenter;
	private final Yaml yaml;
	
	public YamlConfiguration() {
		this.yamlOptions = new DumperOptions();
		this.yamlRepresenter = new YamlRepresenter();
		this.yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);
	}
	
	@NotNull
	@Override
	public String saveToString() {
		
		this.yamlOptions.setIndent(options().indent());
		this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
		final String header = buildHeader();
		String dump = this.yaml.dump(this.getValues(false));
		
		if (dump.equals(YamlConfiguration.BLANK_CONFIG)) {
			dump = "";
		}
		
		return header + dump;
	}
	
	@Override
	public void loadFromString(@NotNull final String data) throws InvalidConfigurationException {
		
		final Map<?, ?> map;
		try {
			map = (Map<?, ?>) this.yaml.load(data);
		} catch (YAMLException e) {
			throw new InvalidConfigurationException(e);
		} catch (ClassCastException e) {
			throw new InvalidConfigurationException("Top level is not a Map.", e);
		}
		
		final String header = this.parseHeader(data);
		if (header.length() > 0) {
			this.options().header(header);
		}
		
		if (map != null) {
			this.convertMapsToSections(map, this);
		}
	}
	
	protected void convertMapsToSections(@NotNull final Map<?, ?> map, @NotNull final ConfigurationSection section) {
		
		for (final Map.Entry<?, ?> entry : map.entrySet()) {
			final String key = entry.getKey().toString();
			final Object value = entry.getValue();
			
			if (value instanceof Map) {
				convertMapsToSections((Map<?, ?>) value, section.createSection(key));
			} else {
				section.set(key, value);
			}
		}
	}
	
	@NotNull
	protected String parseHeader(@NotNull final String data) {
		
		final String[] lines = data.split("\r?\n", -1);
		final StringBuilder builder = new StringBuilder();
		
		boolean readingHeader = true;
		boolean foundHeader = false;
		
		for (int index = 0; index < lines.length && readingHeader; index++) {
			
			final String line = lines[index];
			if (line.startsWith(YamlConfiguration.COMMENT_PREFIX)) {
				
				if (index > 0) {
					builder.append("\n");
				}
				if (line.length() > YamlConfiguration.COMMENT_PREFIX.length()) {
					builder.append(line.substring(YamlConfiguration.COMMENT_PREFIX.length()));
				}
				foundHeader = true;
			} else if (foundHeader && line.length() == 0) {
				builder.append("\n");
			} else if (foundHeader) {
				readingHeader = false;
			}
		}
		
		return builder.toString();
	}
	
	@NotNull
	@Override
	protected String buildHeader() {
		
		final String header = this.options().header();
		if (this.options().copyHeader()) {
			
			final Configuration def = this.getDefaults();
			if (def instanceof FileConfiguration) {
				
				final FileConfiguration fileDef = (FileConfiguration) def;
				final String defHeader = fileDef.buildHeader();
				
				if (defHeader.length() > 0) {
					return defHeader;
				}
			}
		}
		
		if (header == null) {
			return "";
		}
		
		final StringBuilder builder = new StringBuilder();
		final String[] lines = header.split("\r?\n", -1);
		
		boolean startedHeader = false;
		for (int index = lines.length - 1; index >= 0; index--) {
			
			builder.insert(0, "\n");
			if (startedHeader || lines[index].length() != 0) {
				builder.insert(0, lines[index]);
				builder.insert(0, YamlConfiguration.COMMENT_PREFIX);
				startedHeader = true;
			}
		}
		
		return builder.toString();
	}
	
	@NotNull
	@Override
	public YamlConfigurationOptions options() {
		if (this.options == null) {
			this.options = new YamlConfigurationOptions(this);
		}
		return (YamlConfigurationOptions) this.options;
	}
	
	/**
     * Creates a new {@link YamlConfiguration}, loading from the given file.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     * <p>
     * The encoding used may follow the system dependent default.
     *
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if file is null
     */
	@NotNull
	public static YamlConfiguration loadConfiguration(@NotNull final File file) {
		
		final YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load config from file: " + file, e);
		}
		
		return config;
	}
	
	/**
     * Creates a new {@link YamlConfiguration}, loading from the given reader.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     *
     * @param reader input
     * @return resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     */
	@NotNull
	public static YamlConfiguration loadConfiguration(@NotNull final Reader reader) {
		
		final YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(reader);
		} catch (IOException | InvalidConfigurationException e) {
			Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load config from reader.", e);
		}
		
		return config;
	}
}
