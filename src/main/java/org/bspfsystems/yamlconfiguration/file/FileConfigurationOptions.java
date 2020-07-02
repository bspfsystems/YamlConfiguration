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

package org.bspfsystems.yamlconfiguration.file;

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
	
	protected FileConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
		super(configuration);
		this.header = null;
		this.copyHeader = true;
	}
	
	@NotNull
	@Override
	public FileConfiguration configuration() {
		return (FileConfiguration) super.configuration();
	}
	
	@NotNull
	@Override
	public FileConfigurationOptions pathSeparator(final char pathSeparator) {
		super.pathSeparator(pathSeparator);
		return this;
	}
	
	@NotNull
	@Override
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
		return header;
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
     * @param value New header
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
     * FileConfiguration#setDefaults(org.bukkit.configuration.Configuration)}
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
		return copyHeader;
	}
	
	/**
     * Sets whether or not the header should be copied from a default source.
     * <p>
     * If this is true, if a default {@link FileConfiguration} is passed to
     * {@link
     * FileConfiguration#setDefaults(org.bukkit.configuration.Configuration)}
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
     * @param value Whether or not to copy the header
     * @return This object, for chaining
     */
	@NotNull
	public FileConfigurationOptions copyHeader(final boolean copyHeader) {
		this.copyHeader = copyHeader;
		return this;
	}
}
