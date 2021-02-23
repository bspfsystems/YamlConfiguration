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

package org.bspfsystems.yamlconfiguration.file;

import org.bspfsystems.yamlconfiguration.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Various settings for controlling the input and output of a {@link
 * YamlConfiguration}
 */
public final class YamlConfigurationOptions extends FileConfigurationOptions {
	
	private int indent;
	
	/**
	 * Constructs a new set of {@link YamlConfigurationOptions}.
	 *
	 * @param configuration The {@link YamlConfiguration} to create the
	 *                      {@link YamlConfigurationOptions} for.
	 * @see FileConfigurationOptions#FileConfigurationOptions(MemoryConfiguration)
	 */
	protected YamlConfigurationOptions(@NotNull final YamlConfiguration configuration) {
		super(configuration);
		this.indent = 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public YamlConfiguration configuration() {
		return (YamlConfiguration) super.configuration();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public YamlConfigurationOptions pathSeparator(final char pathSeparator) {
		super.pathSeparator(pathSeparator);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public YamlConfigurationOptions copyDefaults(final boolean copyDefaults) {
		super.copyDefaults(copyDefaults);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public YamlConfigurationOptions header(@Nullable final String header) {
		super.header(header);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public YamlConfigurationOptions copyHeader(final boolean copyHeader) {
		super.copyHeader(copyHeader);
		return this;
	}
	
	/**
     * Gets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @return How much to indent by
     */
	public int indent() {
		return this.indent;
	}
	
	/**
     * Sets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @param indent New indent
     * @return This object, for chaining
     */
	@NotNull
	public YamlConfigurationOptions indent(final int indent) {
		if (indent < 2) {
			throw new IllegalArgumentException("Indent must be at least 2 characters.");
		}
		if (indent > 9) {
			throw new IllegalArgumentException("Indent cannot be greater than 9 characters.");
		}
		this.indent = indent;
		return this;
	}
}
