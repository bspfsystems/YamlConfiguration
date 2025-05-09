/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2024 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2025 BSPF Systems, LLC (https://bspfsystems.org/)
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

import org.jetbrains.annotations.NotNull;

/**
 * Represents the various settings for controlling the input and output of a
 * memory configuration.
 * <p>
 * Synchronized with the commit on 13-March-2019.
 */
public class MemoryConfigurationOptions extends ConfigurationOptions {
    
    /**
     * Constructs a set of memory configuration options.
     * 
     * @param configuration The memory configuration to create the options for.
     * @see ConfigurationOptions#ConfigurationOptions(Configuration)
     */
    protected MemoryConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
        super(configuration);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public MemoryConfiguration getConfiguration() {
        return (MemoryConfiguration) super.getConfiguration();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public MemoryConfigurationOptions setPathSeparator(final char pathSeparator) {
        super.setPathSeparator(pathSeparator);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public MemoryConfigurationOptions setCopyDefaults(final boolean copyDefaults) {
        super.setCopyDefaults(copyDefaults);
        return this;
    }
}
