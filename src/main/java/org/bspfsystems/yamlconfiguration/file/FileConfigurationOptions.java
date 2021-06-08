/*
 * This file is part of YamlConfiguration.
 *
 * Implementation of SnakeYAML to be easy to use with files.
 *
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2021 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2021 BSPF Systems, LLC (https://bspfsystems.org/)
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

package org.bspfsystems.yamlconfiguration.file;

import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfiguration;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Various settings for controlling the input and output of a {@link
 * FileConfiguration}
 */
public class FileConfigurationOptions extends MemoryConfigurationOptions {
	
	private String header;
	private boolean copyHeader;
	
	/**
	 * Constructs a new set of {@link FileConfigurationOptions}.
	 *
	 * @param configuration The {@link MemoryConfiguration} to create the
	 *                      {@link FileConfigurationOptions} for.
	 * @see MemoryConfigurationOptions#MemoryConfigurationOptions(MemoryConfiguration)
	 */
	protected FileConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
		super(configuration);
		this.header = null;
		this.copyHeader = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public FileConfiguration configuration() {
		return (FileConfiguration) super.configuration();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public FileConfigurationOptions pathSeparator(final char pathSeparator) {
		super.pathSeparator(pathSeparator);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public FileConfigurationOptions copyDefaults(final boolean copyDefaults) {
		super.copyDefaults(copyDefaults);
		return this;
	}
	
	/**
     * Gets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link FileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * Null is a valid value which will indicate that no header is to be
     * applied. The default value is null.
     *
     * @return Header
     */
	@Nullable
	public final String header() {
		return this.header;
	}
	
	/**
     * Sets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link FileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * Null is a valid value which will indicate that no header is to be
     * applied.
     *
     * @param header New header
     * @return This object, for chaining
     */
	@NotNull
	public FileConfigurationOptions header(@Nullable final String header) {
		this.header = header;
		return this;
	}
	
	/**
     * Gets whether or not the header should be copied from a default source.
     * <p>
     * If this is true, if a default {@link FileConfiguration} is passed to
     * {@link
     * FileConfiguration#setDefaults(Configuration)}
     * then upon saving it will use the header from that config, instead of
     * the one provided here.
     * <p>
     * If no default is set on the configuration, or the default is not of
     * type FileConfiguration, or that config has no header ({@link #header()}
     * returns null) then the header specified in this configuration will be
     * used.
     * <p>
     * Defaults to true.
     *
     * @return Whether or not to copy the header
     */
	public final boolean copyHeader() {
		return this.copyHeader;
	}
	
	/**
     * Sets whether or not the header should be copied from a default source.
     * <p>
     * If this is true, if a default {@link FileConfiguration} is passed to
     * {@link
     * FileConfiguration#setDefaults(Configuration)}
     * then upon saving it will use the header from that config, instead of
     * the one provided here.
     * <p>
     * If no default is set on the configuration, or the default is not of
     * type FileConfiguration, or that config has no header ({@link #header()}
     * returns null) then the header specified in this configuration will be
     * used.
     * <p>
     * Defaults to true.
     *
     * @param copyHeader Whether or not to copy the header
     * @return This object, for chaining
     */
	@NotNull
	public FileConfigurationOptions copyHeader(final boolean copyHeader) {
		this.copyHeader = copyHeader;
		return this;
	}
}
