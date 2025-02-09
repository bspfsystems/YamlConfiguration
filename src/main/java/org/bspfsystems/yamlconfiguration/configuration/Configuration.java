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

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a source of configurable options and settings.
 * <p>
 * Synchronized with the commit on 28-April-2019.
 */
public interface Configuration extends ConfigurationSection {
    
    /**
     * Sets the default value of the given path as provided.
     * <p>
     * If no source configuration was provided as a default collection, then a
     * new memory configuration will be created to hold the given default value.
     * <p>
     * If value is {@code null}, the value will be removed from the default
     * configuration source.
     *
     * @param path The path of the value to set.
     * @param value The value to set the default to.
     * @throws IllegalArgumentException Thrown if the path is {@code null}.
     */
    @Override
    void addDefault(@NotNull final String path, @Nullable final Object value) throws IllegalArgumentException;
    
    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source configuration was provided as a default collection, then a
     * new memory configuration will be created to hold the given default
     * values.
     * 
     * @param defs A map of paths to values to add to the defaults.
     */
    void addDefaults(@NotNull final Map<String, Object> defs);
    
    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source configuration was provided as a default collection, then a
     * new memory configuration will be created to hold the given default
     * values.
     * <p>
     * This method will not hold a reference to the given configuration, nor
     * will it automatically update if the given configuration ever changes. If
     * the automatic updates are required, please set the default source with
     * {@link Configuration#setDefaults(Configuration)}.
     * 
     * @param defs A configuration holding a map of defaults to copy.
     */
    void addDefaults(@NotNull final Configuration defs);
    
    /**
     * Sets the source of all default values for this configuration.
     * <p>
     * If a previous source was set, or previous default values were defined,
     * then they will not be copied to the new source.
     * 
     * @param defs New source of default values for this configuration.
     */
    void setDefaults(@NotNull final Configuration defs);
    
    /**
     * Gets the default configuration for this configuration.
     * <p>
     * If no configuration source was set, but default values were added, then
     * a memory configuration will be returned. If no source was set and no
     * defaults were set, then this method will return {@code null}.
     * 
     * @return The configuration source for default values, or {@code null} if
     *         none exist.
     */
    @Nullable
    Configuration getDefaults();
    
    /**
     * Gets the options for this configuration.
     * <p>
     * All setters through this method are chainable.
     * 
     * @return The options for this configuration.
     */
    @NotNull
    ConfigurationOptions getOptions();
}
