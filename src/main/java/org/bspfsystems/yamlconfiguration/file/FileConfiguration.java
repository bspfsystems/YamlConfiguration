/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 *
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2022 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2022 BSPF Systems, LLC (https://bspfsystems.org/)
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
 * This is a base class for all {@link File}-based implementations of
 * a {@link MemoryConfiguration}.
 * <p>
 * Synchronized with the commit on 07-June-2022.
 */
public abstract class FileConfiguration extends MemoryConfiguration {
    
    /**
     * Creates an empty {@link FileConfiguration} with no default values.
     * 
     * @see MemoryConfiguration#MemoryConfiguration()
     */
    public FileConfiguration() {
        super();
    }
    
    /**
     * Creates an empty {@link FileConfiguration} using the specified
     * {@link Configuration} as a source for all default values.
     * 
     * @param defs A {@link Configuration} containing the values to use as
     *             defaults.
     * @see MemoryConfiguration#MemoryConfiguration(Configuration)
     */
    public FileConfiguration(@Nullable final Configuration defs) {
        super(defs);
    }
    
    /**
     * Saves this {@link FileConfiguration} to the given {@link File}.
     * <p>
     * If the {@link File} does not exist, it will be created. If already
     * exists, it will be overwritten. If it cannot be overwritten or created,
     * an {@link IOException} will be thrown.
     * <p>
     * This method will save using {@link StandardCharsets#UTF_8}.
     * 
     * @param file The {@link File} to save to.
     * @throws IOException If the {@link File} cannot be written to.
     * @see FileConfiguration#saveToString()
     */
    public final void save(@NotNull final File file) throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("File has not been created at " + file.getPath());
            }
        }
        
        try (final Writer writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8.name())) {
            writer.write(this.saveToString());
        }
    }
    
    /**
     * Saves this {@link FileConfiguration} to a {@link File} at the given path.
     * <p>
     * If the {@link File} does not exist, it will be created. If already
     * exists, it will be overwritten. If it cannot be overwritten or created,
     * an {@link IOException} will be thrown.
     * <p>
     * This method will save using {@link StandardCharsets#UTF_8}.
     * 
     * @param path The path of the {@link File} to save to.
     * @throws IOException If the {@link File} cannot be written to.
     * @see FileConfiguration#save(File)
     */
    public final void save(@NotNull final String path) throws IOException {
        this.save(new File(path));
    }
    
    /**
     * Saves this {@link FileConfiguration} to a {@link String}, and returns it.
     *
     * @return The {@link String} containing this {@link FileConfiguration}.
     */
    @NotNull
    public abstract String saveToString();
    
    /**
     * Loads this {@link FileConfiguration} from the given {@link Reader}.
     * <p>
     * All values contained in-memory in this {@link FileConfiguration} will be
     * removed, leaving only the {@link FileConfigurationOptions} as well as any
     * defaults. The new values will be loaded into memory from the given
     * {@link Reader}.
     * 
     * @param reader The {@link Reader} used to load this
     *               {@link FileConfiguration}.
     * @throws IOException If the given {@link Reader} encounters an error and
     *                     throws an {@link IOException}.
     * @throws InvalidConfigurationException If the data in the {@link Reader}
     *                                       cannot be parsed as a
     *                                       {@link FileConfiguration}.
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
     * Loads this {@link FileConfiguration} from the given {@link File}.
     * <p>
     * All values contained in-memory in this {@link FileConfiguration} will be
     * removed, leaving only the {@link FileConfigurationOptions} as well as any
     * defaults. The new values will be loaded into memory from the given
     * {@link File}.
     * 
     * @param file The {@link File} used to load this {@link FileConfiguration}.
     * @throws IOException If the given {@link File} cannot be read.
     * @throws InvalidConfigurationException If the data in the {@link File}
     *                                       cannot be parsed as a
     *                                       {@link FileConfiguration}.
     * @see FileConfiguration#load(Reader)
     */
    public final void load(@NotNull final File file) throws IOException, InvalidConfigurationException {
        this.load(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8.name()));
    }
    
    /**
     * Loads this {@link FileConfiguration} from a {@link File} at the given
     * path.
     * <p>
     * All values contained in-memory in this {@link FileConfiguration} will be
     * removed, leaving only the {@link FileConfigurationOptions} as well as any
     * defaults. The new values will be loaded into memory from the {@link File}
     * at the given path.
     * 
     * @param path The path of the {@link File} to load from.
     * @throws IOException If the {@link File} cannot be read.
     * @throws InvalidConfigurationException If the data in the {@link File}
     *                                       cannot be parsed as a
     *                                       {@link FileConfiguration}.
     * @see FileConfiguration#load(File)
     */
    public final void load(@NotNull final String path) throws IOException, InvalidConfigurationException {
        this.load(new File(path));
    }
    
    /**
     * Saves this {@link FileConfiguration} to a {@link String}, and returns it.
     *
     * @return The {@link String} containing this {@link FileConfiguration}.
     */
    
    /**
     * Loads this {@link FileConfiguration} from the given {@link String}.
     * <p>
     * All values contained in-memory in this {@link FileConfiguration} will be
     * removed, leaving only the {@link FileConfigurationOptions} as well as any
     * defaults. The new values will be loaded into memory from the
     * {@link String}.
     * 
     * @param data A {@link String} representation of the
     *             {@link FileConfiguration} data to load.
     * @throws InvalidConfigurationException If the given {@link String} cannot
     *                                       be parsed as a
     *                                       {@link FileConfiguration}.
     */
    public abstract void loadFromString(@NotNull final String data) throws InvalidConfigurationException;
    
    /**
     * This method is deprecated and exists only for backwards compatibility; it
     * only returns an empty {@link String}. Please use
     * {@link FileConfigurationOptions#getHeader()} instead.
     * 
     * @return An empty {@link String}.
     * @deprecated This method only exists for backwards compatibility. Use
     *             {@link FileConfigurationOptions#getHeader()} instead.
     * @see FileConfigurationOptions#getHeader()
     */
    @Deprecated
    @NotNull
    protected final String buildHeader() {
        return "";
    }
    
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
    
    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    @NotNull
    public FileConfigurationOptions options() {
        return this.getOptions();
    }
}
