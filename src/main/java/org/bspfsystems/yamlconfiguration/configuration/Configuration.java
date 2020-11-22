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

package org.bspfsystems.yamlconfiguration.configuration;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a source of configurable options and settings
 */
public interface Configuration extends ConfigurationSection {
	
	/**
     * Sets the default value of the given path as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * If value is null, the value will be removed from the default
     * Configuration source.
     *
     * @param path Path of the value to set.
     * @param value Value to set the default to.
     * @throws IllegalArgumentException Thrown if path is null.
     */
	@Override
	void addDefault(@NotNull String path, @Nullable Object value);
	
	/**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default values.
     *
     * @param defs A map of Path{@literal ->}Values to add to defaults.
     * @throws IllegalArgumentException Thrown if defaults is null.
     */
	void addDefaults(@NotNull Map<String, Object> defs);
	
	/**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * This method will not hold a reference to the specified Configuration,
     * nor will it automatically update if that Configuration ever changes. If
     * you require this, you should set the default source with {@link
     * #setDefaults(Configuration)}.
     *
     * @param defs A configuration holding a list of defaults to copy.
     * @throws IllegalArgumentException Thrown if defaults is null or this.
     */
	void addDefaults(@NotNull Configuration defs);
	
	/**
     * Sets the source of all default values for this {@link Configuration}.
     * <p>
     * If a previous source was set, or previous default values were defined,
     * then they will not be copied to the new source.
     *
     * @param defs New source of default values for this configuration.
     * @throws IllegalArgumentException Thrown if defaults is null or this.
     */
	void setDefaults(@NotNull Configuration defs);
	
	/**
     * Gets the source {@link Configuration} for this configuration.
     * <p>
     * If no configuration source was set, but default values were added, then
     * a {@link MemoryConfiguration} will be returned. If no source was set
     * and no defaults were set, then this method will return null.
     *
     * @return Configuration source for default values, or null if none exist.
     */
	@Nullable
	Configuration getDefaults();
	
	/**
     * Gets the {@link ConfigurationOptions} for this {@link Configuration}.
     * <p>
     * All setters through this method are chainable.
     *
     * @return Options for this configuration
     */
	@NotNull
	ConfigurationOptions options();
}
