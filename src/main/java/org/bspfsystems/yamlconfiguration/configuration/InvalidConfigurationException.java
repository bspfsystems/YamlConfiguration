/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2024 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2025 BSPF Systems, LLC (https://bspfsystems.org/)
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

import java.io.Serial;
import org.jetbrains.annotations.NotNull;

/**
 * An exception thrown when attempting to load an invalid configuration.
 * <p>
 * Synchronized with the commit on 15-December-2013.
 */
public final class InvalidConfigurationException extends Exception {
    
    @Serial
    private static final long serialVersionUID = 685592388091335686L;
    
    /**
     * Constructs an exception without a message or cause.
     * 
     * @see Exception#Exception()
     */
    public InvalidConfigurationException() {
        super();
    }
    
    /**
     * Constructs an exception with a message.
     * 
     * @param message The details of the exception.
     * @see Exception#Exception(String)
     */
    public InvalidConfigurationException(@NotNull final String message) {
        super(message);
    }
    
    /**
     * Constructs an exception with an upstream cause.
     * 
     * @param cause The cause of the new exception.
     * @see Exception#Exception(Throwable)
     */
    public InvalidConfigurationException(@NotNull final Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs an exception with a message and upstream cause.
     * 
     * @param message The details of the exception.
     * @param cause The cause of the new exception.
     * @see Exception#Exception(String, Throwable)
     */
    public InvalidConfigurationException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
