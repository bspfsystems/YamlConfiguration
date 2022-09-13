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
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * If value is {@code null}, the value will be removed from the default
     * {@link Configuration} source.
     *
     * @param path Path of the value to set.
     * @param value Value to set the default to.
     * @throws IllegalArgumentException Thrown if path is {@code null}.
     */
    @Override
    void addDefault(@NotNull final String path, final @Nullable Object value) throws IllegalArgumentException;
    
    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default values.
     * 
     * @param defs A {@link Map} of Path{@literal ->}Values to add to the
     *             defaults.
     */
    void addDefaults(@NotNull final Map<String, Object> defs);
    
    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * This method will not hold a reference to the specified
     * {@link Configuration}, nor will it automatically update if that
     * {@link Configuration} ever changes. If you require this, you should set
     * the default source with {@link Configuration#setDefaults(Configuration)}.
     * 
     * @param defs A {@link Configuration} holding a list of defaults to copy.
     */
    void addDefaults(@NotNull final Configuration defs);
    
    /**
     * Sets the source of all default values for this {@link Configuration}.
     * <p>
     * If a previous source was set, or previous default values were defined,
     * then they will not be copied to the new source.
     * 
     * @param defs New source of default values for this {@link Configuration}.
     */
    void setDefaults(@NotNull final Configuration defs);
    
    /**
     * Gets the source {@link Configuration} for this {@link Configuration}.
     * <p>
     * If no configuration source was set, but default values were added, then
     * a {@link MemoryConfiguration} will be returned. If no source was set
     * and no defaults were set, then this method will return {@link null}.
     * 
     * @return The {@link Configuration} source for default values, or
     *         {@code null} if none exist.
     */
    @Nullable
    Configuration getDefaults();
    
    /**
     * Gets the {@link ConfigurationOptions} for this {@link Configuration}.
     * <p>
     * All setters through this method are chainable.
     * 
     * @return The {@link ConfigurationOptions} for this {@link Configuration}.
     */
    @NotNull
    ConfigurationOptions getOptions();
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link Configuration#getOptions()} instead; it provides the
     * same functionality.
     *
     * @return The {@link ConfigurationOptions} for this {@link Configuration}.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link Configuration#getOptions()} instead.
     * @see Configuration#getOptions()
     */
    @Deprecated
    @NotNull
    ConfigurationOptions options();
}
