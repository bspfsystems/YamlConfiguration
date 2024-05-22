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

package org.bspfsystems.yamlconfiguration.configuration;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an implementation of a configuration that exists only in-memory.
 * This is useful for providing temporary storage and access to data and for
 * providing defaults.
 * <p>
 * Synchronized with the commit on 07-June-2022.
 */
public class MemoryConfiguration extends MemorySection implements Configuration {
    
    protected Configuration defs;
    protected MemoryConfigurationOptions options;
    
    /**
     * Constructs an empty memory configuration with no default values.
     * 
     * @see MemorySection#MemorySection()
     */
    public MemoryConfiguration() {
        super();
    }
    
    /**
     * Constructs an empty memory configuration using the given configuration as
     * a source for all default values.
     * 
     * @param defs The default value provider configuration.
     * @see MemorySection#MemorySection()
     */
    public MemoryConfiguration(@Nullable final Configuration defs) {
        super();
        this.defs = defs;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void addDefault(@NotNull final String path, @Nullable final Object value) {
        if (this.defs == null) {
            this.defs = new MemoryConfiguration();
        }
        this.defs.set(path, value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void addDefaults(@NotNull final Map<String, Object> defs) {
        for (final Map.Entry<String, Object> entry : defs.entrySet()) {
            this.addDefault(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void addDefaults(@NotNull final Configuration defs) {
        for (final String key : defs.getKeys(true)) {
            if (!defs.isConfigurationSection(key)) {
                this.addDefault(key, defs.get(key));
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void setDefaults(@NotNull final Configuration defs) {
        this.defs = defs;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public final Configuration getDefaults() {
        return this.defs;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public final ConfigurationSection getParent() {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public MemoryConfigurationOptions getOptions() {
        if (this.options == null) {
            this.options = new MemoryConfigurationOptions(this);
        }
        return this.options;
    }
}
