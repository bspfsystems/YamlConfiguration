/*
 * This file is part of YamlConfigurtion.
 * 
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

package org.bspfsystems.yamlconfiguration.configuration;

import org.jetbrains.annotations.NotNull;

/**
 * Various settings for controlling the input and output of a {@link
 * MemoryConfiguration}
 */
public class MemoryConfigurationOptions extends ConfigurationOptions {
	
	protected MemoryConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
		super(configuration);
	}
	
	@NotNull
	@Override
	public MemoryConfiguration configuration() {
		return (MemoryConfiguration) super.configuration();
	}
	
	@NotNull
	@Override
	public MemoryConfigurationOptions pathSeparator(final char pathSeparator) {
		super.pathSeparator(pathSeparator);
		return this;
	}
	
	@NotNull
	@Override
	public MemoryConfigurationOptions copyDefaults(final boolean copyDefaults) {
		super.copyDefaults(copyDefaults);
		return this;
	}
}
