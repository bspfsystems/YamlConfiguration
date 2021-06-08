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

package org.bspfsystems.yamlconfiguration.configuration;

/**
 * Exception thrown when attempting to load an invalid {@link Configuration}
 */
public final class InvalidConfigurationException extends Exception {

	private static final long serialVersionUID = 685592388091335686L;
	
	/**
     * Creates a new instance of InvalidConfigurationException without a
     * message or cause.
     */
	public InvalidConfigurationException() {
		super();
	}
	
	/**
     * Constructs an instance of InvalidConfigurationException with the
     * specified message.
     *
     * @param message The details of the exception.
     */
	public InvalidConfigurationException(final String message) {
		super(message);
	}
	
	/**
     * Constructs an instance of InvalidConfigurationException with the
     * specified cause.
     *
     * @param cause The cause of the exception.
     */
	public InvalidConfigurationException(final Throwable cause) {
		super(cause);
	}
	
	/**
     * Constructs an instance of InvalidConfigurationException with the
     * specified message and cause.
     *
     * @param cause The cause of the exception.
     * @param message The details of the exception.
     */
	public InvalidConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
