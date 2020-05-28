/*
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2020  Matt Ciolkosz (https://github.com/mciolkosz)
 * Copyright (C) 2014-2020 SpigotMC Pty. Ltd. (https://www.spigotmc.org)
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Various settings for controlling the input and output of a {@link
 * YamlConfiguration}
 */
public final class YamlConfigurationOptions extends FileConfigurationOptions {
	
	private int indent;
	
	protected YamlConfigurationOptions(@NotNull final YamlConfiguration configuration) {
		super(configuration);
		this.indent = 2;
	}
	
	@NotNull
	@Override
	public YamlConfiguration configuration() {
		return (YamlConfiguration) super.configuration();
	}
	
	@NotNull
	@Override
	public YamlConfigurationOptions pathSeparator(final char pathSeparator) {
		super.pathSeparator(pathSeparator);
		return this;
	}
	
	@NotNull
	@Override
	public YamlConfigurationOptions copyDefaults(final boolean copyDefaults) {
		super.copyDefaults(copyDefaults);
		return this;
	}
	
	@NotNull
	@Override
	public YamlConfigurationOptions header(@Nullable final String header) {
		super.header(header);
		return this;
	}
	
	@NotNull
	@Override
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
		return indent;
	}
	
	/**
     * Sets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @param value New indent
     * @return This object, for chaining
     */
	@NotNull
	public YamlConfigurationOptions indent(final int indent) {
		if(indent < 2) {
			throw new IllegalArgumentException("Indent must be at least 2 characters.");
		}
		if(indent > 9) {
			throw new IllegalArgumentException("Indent cannot be greater than 9 characters.");
		}
		this.indent = indent;
		return this;
	}
}
