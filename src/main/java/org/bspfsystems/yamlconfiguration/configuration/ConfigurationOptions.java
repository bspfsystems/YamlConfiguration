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

package org.bspfsystems.yamlconfiguration.configuration;

import org.jetbrains.annotations.NotNull;

/**
 * Various settings for controlling the input and output of a {@link
 * Configuration}
 */
public class ConfigurationOptions {
	
	private final Configuration configuration;
	
	private char pathSeparator;
	private boolean copyDefaults;
	
	protected ConfigurationOptions(@NotNull final Configuration configuration) {
		this.configuration = configuration;
		this.pathSeparator = '.';
		this.copyDefaults = false;
	}
	
	/**
     * Returns the {@link Configuration} that this object is responsible for.
     *
     * @return Parent configuration
     */
	@NotNull
	public Configuration configuration() {
		return configuration;
	}
	
	/**
     * Gets the char that will be used to separate {@link
     * ConfigurationSection}s
     * <p>
     * This value does not affect how the {@link Configuration} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @return Path separator
     */
	public final char pathSeparator() {
		return pathSeparator;
	}
	
	/**
     * Sets the char that will be used to separate {@link
     * ConfigurationSection}s
     * <p>
     * This value does not affect how the {@link Configuration} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @param pathSeparator Path separator
     * @return This object, for chaining
     */
	@NotNull
	public ConfigurationOptions pathSeparator(char pathSeparator) {
		this.pathSeparator = pathSeparator;
		return this;
	}
	
	/**
     * Checks if the {@link Configuration} should copy values from its default
     * {@link Configuration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link ConfigurationSection#contains(java.lang.String)} will always
     * return the same value as {@link
     * ConfigurationSection#isSet(java.lang.String)}. The default value is
     * false.
     *
     * @return Whether or not defaults are directly copied
     */
	public final boolean copyDefaults() {
		return copyDefaults;
	}
	
	/**
     * Sets if the {@link Configuration} should copy values from its default
     * {@link Configuration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link ConfigurationSection#contains(java.lang.String)} will always
     * return the same value as {@link
     * ConfigurationSection#isSet(java.lang.String)}. The default value is
     * false.
     *
     * @param copyDefaults Whether or not defaults are directly copied
     * @return This object, for chaining
     */
	@NotNull
	public ConfigurationOptions copyDefaults(boolean copyDefaults) {
		this.copyDefaults = copyDefaults;
		return this;
	}
}
