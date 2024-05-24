/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2023 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2024 BSPF Systems, LLC (https://bspfsystems.org/)
 * 
 * Many of the files in this project are sourced from the Bukkit API as
 * part of The Bukkit Project (https://bukkit.org/), now maintained by
 * SpigotMC Pty. Ltd. (https://www.spigotmc.org/). These files can be found
 * at https://github.com/Bukkit/Bukkit/ and https://hub.spigotmc.org/stash/,
 * respectively.
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a base class for all file-based implementations of a memory
 * configuration.
 * <p>
 * Synchronized with the commit on 07-June-2022.
 */
public abstract class FileConfiguration extends MemoryConfiguration {
    
    /**
     * Constructs an empty file configuration with no default values.
     * 
     * @see MemoryConfiguration#MemoryConfiguration()
     */
    protected FileConfiguration() {
        super();
    }
    
    /**
     * Constructs an empty file configuration using the given configuration as a
     * source for all default values.
     * 
     * @param defs The default value provider configuration.
     * @see MemoryConfiguration#MemoryConfiguration(Configuration)
     */
    protected FileConfiguration(@Nullable final Configuration defs) {
        super(defs);
    }
    
    /**
     * Saves this file configuration to the given file.
     * <p>
     * If the given file does not exist, it will attempt to be created. If it
     * already exists, any contents will be overwritten, regardless of the old
     * or new contents. If it cannot be created or overwritten, an I/O exception
     * will be thrown.
     * <p>
     * This method will save using the UTF-8 charset.
     * 
     * @param file The file to save to.
     * @throws IOException If this file configuration cannot be written.
     * @see FileConfiguration#saveToString()
     */
    public final void save(@NotNull final File file) throws IOException {
        
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("File has not been created at " + file.getPath());
            }
        }
        
        final Writer writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8);
        writer.write(this.saveToString());
        writer.close();
    }
    
    /**
     * Saves this file configuration to the given file.
     * <p>
     * If the file at the given path does not exist, it will attempt to be
     * created. If it already exists, any contents will be overwritten,
     * regardless of the old or new contents. If it cannot be created or
     * overwritten, an I/O exception will be thrown.
     * <p>
     * This method will save using the UTF-8 charset.
     * 
     * @param path The path of the file to save to.
     * @throws IOException If this file configuration cannot be written.
     * @see FileConfiguration#save(File)
     */
    public final void save(@NotNull final String path) throws IOException {
        this.save(new File(path));
    }
    
    /**
     * Converts this file configuration to a string.
     *
     * @return A string representing this file configuration.
     */
    @NotNull
    public abstract String saveToString();
    
    /**
     * Loads this file configuration from the given reader.
     * <p>
     * All values contained in-memory in this file configuration will be
     * removed, leaving only the file configuration options as well as any
     * defaults. The new values will be loaded into memory from the given
     * reader.
     * 
     * @param reader The reader used to load this file configuration.
     * @throws IOException If the given reader encounters an error.
     * @throws InvalidConfigurationException If the data in the reader cannot be
     *                                       parsed as a file configuration.
     * @see FileConfiguration#loadFromString(String)
     */
    public final void load(@NotNull final Reader reader) throws IOException, InvalidConfigurationException {
        
        final BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        final StringBuilder builder = new StringBuilder();
        
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } finally {
            bufferedReader.close();
        }
        
        this.loadFromString(builder.toString());
    }
    
    /**
     * Loads this file configuration from the given file.
     * <p>
     * All values contained in-memory in this file configuration will be
     * removed, leaving only the file configuration options as well as any
     * defaults. The new values will be loaded into memory from the given file.
     * 
     * @param file The file used to load this file configuration.
     * @throws IOException If the given file cannot be read.
     * @throws InvalidConfigurationException If the data in the file cannot be
     *                                       parsed as a file configuration.
     * @see FileConfiguration#load(Reader)
     */
    public final void load(@NotNull final File file) throws IOException, InvalidConfigurationException {
        this.load(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
    }
    
    /**
     * Loads this file configuration from the file at the given path.
     * <p>
     * All values contained in-memory in this file configuration will be
     * removed, leaving only the file configuration options as well as any
     * defaults. The new values will be loaded into memory from the file at the
     * given path.
     * 
     * @param path The path of the file to load from.
     * @throws IOException If the file cannot be read.
     * @throws InvalidConfigurationException If the data in the file cannot be
     *                                       parsed as a file configuration.
     * @see FileConfiguration#load(File)
     */
    public final void load(@NotNull final String path) throws IOException, InvalidConfigurationException {
        this.load(new File(path));
    }
    
    /**
     * Loads this file configuration from the given string.
     * <p>
     * All values contained in-memory in this file configuration will be
     * removed, leaving only the file configuration options as well as any
     * defaults. The new values will be loaded into memory from the given
     * string.
     * 
     * @param data The string representation of the file configuration data to
     *             load.
     * @throws InvalidConfigurationException If the given string cannot be
     *                                       parsed as a file configuration.
     */
    public abstract void loadFromString(@NotNull final String data) throws InvalidConfigurationException;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public FileConfigurationOptions getOptions() {
        if (this.options == null) {
            this.options = new FileConfigurationOptions(this);
        }
        return (FileConfigurationOptions) this.options;
    }
}
