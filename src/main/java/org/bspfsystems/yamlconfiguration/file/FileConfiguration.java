/*
 * This file is part of YamlConfigurtion.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2020  Matt Ciolkosz (https://github.com/mciolkosz)
 * Copyright (C) 2014-2020 SpigotMC Pty. Ltd. (https://www.spigotmc.org)
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is a base class for all File based implementations of {@link
 * Configuration}
 */
public abstract class FileConfiguration extends MemoryConfiguration {
	
	/**
     * Creates an empty {@link FileConfiguration} with no default values.
     */
	public FileConfiguration() {
		super();
	}
	
	/**
     * Creates an empty {@link FileConfiguration} using the specified {@link
     * Configuration} as a source for all default values.
     *
     * @param defaults Default value provider
     */
	public FileConfiguration(@Nullable final Configuration defs) {
		super(defs);
	}
	
	/**
     * Saves this {@link FileConfiguration} to the specified location.
     * <p>
     * If the file does not exist, it will be created. If already exists, it
     * will be overwritten. If it cannot be overwritten or created, an
     * exception will be thrown.
     * <p>
     * This method will save using the system default encoding, or possibly
     * using UTF8.
     *
     * @param file File to save to.
     * @throws IOException Thrown when the given file cannot be written to for
     *     any reason.
     * @throws IllegalArgumentException Thrown when file is null.
     */
	public void save(@NotNull final File file) throws IOException {
		
		if(file == null) {
			throw new IllegalArgumentException("File cannot be null.");
		}
		if(!file.exists()) {
			file.createNewFile();
		}
		
		final String data = saveToString();
		final Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8.name());
		
		try {
			writer.write(data);
		}
		finally {
			writer.close();
		}
	}
	
	/**
     * Saves this {@link FileConfiguration} to the specified location.
     * <p>
     * If the file does not exist, it will be created. If already exists, it
     * will be overwritten. If it cannot be overwritten or created, an
     * exception will be thrown.
     * <p>
     * This method will save using the system default encoding, or possibly
     * using UTF8.
     *
     * @param file File to save to.
     * @throws IOException Thrown when the given file cannot be written to for
     *     any reason.
     * @throws IllegalArgumentException Thrown when file is null.
     */
	public void save(@NotNull final String fileName) throws IOException {
		
		if(fileName == null) {
			throw new IllegalArgumentException("File name cannot be null.");
		}
		save(new File(fileName));
	}
	
	/**
     * Saves this {@link FileConfiguration} to a string, and returns it.
     *
     * @return String containing this configuration.
     */
	@NotNull
	public abstract String saveToString();
	
	/**
     * Loads this {@link FileConfiguration} from the specified location.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given file.
     * <p>
     * If the file cannot be loaded for any reason, an exception will be
     * thrown.
     *
     * @param file File to load from.
     * @throws FileNotFoundException Thrown when the given file cannot be
     *     opened.
     * @throws IOException Thrown when the given file cannot be read.
     * @throws InvalidConfigurationException Thrown when the given file is not
     *     a valid Configuration.
     * @throws IllegalArgumentException Thrown when file is null.
     */
	public void load(@NotNull Reader reader) throws IOException, InvalidConfigurationException {
		
		final BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
		final StringBuilder builder = new StringBuilder();
		
		try {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
		}
		finally {
			bufferedReader.close();
		}
		
		loadFromString(builder.toString());
	}
	
	/**
     * Loads this {@link FileConfiguration} from the specified reader.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given stream.
     *
     * @param reader the reader to load from
     * @throws IOException thrown when underlying reader throws an IOException
     * @throws InvalidConfigurationException thrown when the reader does not
     *      represent a valid Configuration
     * @throws IllegalArgumentException thrown when reader is null
     */
	public void load(@NotNull File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
		
		if(file == null) {
			throw new IllegalArgumentException("File cannot be null.");
		}
		load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8.name()));
	}
	
	/**
     * Loads this {@link FileConfiguration} from the specified location.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given file.
     * <p>
     * If the file cannot be loaded for any reason, an exception will be
     * thrown.
     *
     * @param file File to load from.
     * @throws FileNotFoundException Thrown when the given file cannot be
     *     opened.
     * @throws IOException Thrown when the given file cannot be read.
     * @throws InvalidConfigurationException Thrown when the given file is not
     *     a valid Configuration.
     * @throws IllegalArgumentException Thrown when file is null.
     */
	public void load(@NotNull String fileName) throws FileNotFoundException, IOException, InvalidConfigurationException {
		
		if(fileName == null) {
			throw new IllegalArgumentException("File name cannot be null.");
		}
		load(new File(fileName));
	}
	
	/**
     * Loads this {@link FileConfiguration} from the specified string, as
     * opposed to from file.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given string.
     * <p>
     * If the string is invalid in any way, an exception will be thrown.
     *
     * @param contents Contents of a Configuration to load.
     * @throws InvalidConfigurationException Thrown if the specified string is
     *     invalid.
     * @throws IllegalArgumentException Thrown if contents is null.
     */
	public abstract void loadFromString(@NotNull String data) throws InvalidConfigurationException;
	
	/**
     * Loads this {@link FileConfiguration} from the specified string, as
     * opposed to from file.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given string.
     * <p>
     * If the string is invalid in any way, an exception will be thrown.
     *
     * @param contents Contents of a Configuration to load.
     * @throws InvalidConfigurationException Thrown if the specified string is
     *     invalid.
     * @throws IllegalArgumentException Thrown if contents is null.
     */
	@NotNull
	protected abstract String buildHeader();
	
	@NotNull
	@Override
	public FileConfigurationOptions options() {
		if(options == null) {
			options = new FileConfigurationOptions(this);
		}
		return (FileConfigurationOptions) options;
	}
}
