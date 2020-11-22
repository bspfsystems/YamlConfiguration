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
 * This is a {@link Configuration} implementation that does not save or load
 * from any source, and stores all values in memory only.
 * This is useful for temporary Configurations for providing defaults.
 */
public class MemoryConfiguration extends MemorySection implements Configuration {
	
	protected Configuration defs;
	protected MemoryConfigurationOptions options;
	
	/**
     * Creates an empty {@link MemoryConfiguration} with no default values.
     */
	public MemoryConfiguration() {
		
	}
	
	/**
     * Creates an empty {@link MemoryConfiguration} using the specified {@link
     * Configuration} as a source for all default values.
     *
     * @param defs Default value provider
     * @throws IllegalArgumentException Thrown if defaults is null
     */
	public MemoryConfiguration(@Nullable final Configuration defs) {
		this.defs = defs;
	}
	
	@Override
	public void addDefault(@NotNull String path, @Nullable Object value) {
		if (this.defs == null) {
			this.defs = new MemoryConfiguration();
		}
		this.defs.set(path, value);
	}
	
	@Override
	public void addDefaults(@NotNull final Map<String, Object> defs) {
		for (final Map.Entry<String, Object> entry : defs.entrySet()) {
			this.addDefault(entry.getKey(), entry.getValue());
		}
	}
	
	@Override
	public void addDefaults(@NotNull final Configuration defs) {
		this.addDefaults(defs.getValues(true));
	}
	
	@Override
	public void setDefaults(@NotNull final Configuration defs) {
		this.defs = defs;
	}
	
	@Nullable
	@Override
	public Configuration getDefaults() {
		return this.defs;
	}
	
	@Nullable
	@Override
	public ConfigurationSection getParent() {
		return null;
	}
	
	@NotNull
	@Override
	public MemoryConfigurationOptions options() {
		if (this.options == null) {
			this.options = new MemoryConfigurationOptions(this);
		}
		return this.options;
	}
}
