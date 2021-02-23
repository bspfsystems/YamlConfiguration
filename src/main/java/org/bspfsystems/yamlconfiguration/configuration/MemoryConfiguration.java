/*
 * This file is part of YamlConfiguration.
 *
 * Implementation of SnakeYAML to be easy to use with files.
 *
 * Copyright (C) 2014-2021 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2021 BSPF Systems, LLC (https://bspfsystems.org/)
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
		// Do nothing.
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDefault(@NotNull String path, @Nullable Object value) {
		if (this.defs == null) {
			this.defs = new MemoryConfiguration();
		}
		this.defs.set(path, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDefaults(@NotNull final Map<String, Object> defs) {
		for (final Map.Entry<String, Object> entry : defs.entrySet()) {
			this.addDefault(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDefaults(@NotNull final Configuration defs) {
		this.addDefaults(defs.getValues(true));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaults(@NotNull final Configuration defs) {
		this.defs = defs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Configuration getDefaults() {
		return this.defs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public ConfigurationSection getParent() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public MemoryConfigurationOptions options() {
		if (this.options == null) {
			this.options = new MemoryConfigurationOptions(this);
		}
		return this.options;
	}
}
