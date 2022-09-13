/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2022 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2022 BSPF Systems, LLC (https://bspfsystems.org/)
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

package org.bspfsystems.yamlconfiguration.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an "alias" that a {@link ConfigurationSerializable} may be
 * stored as.
 * If this is not present on a {@link ConfigurationSerializable} {@link Class},
 * it will use the fully qualified name of the {@link Class}.
 * <p>
 * This value will be stored in the {@link ConfigurationSerialization} so that
 * the {@link ConfigurationSerialization} can determine what type it is.
 * <p>
 * Using this annotation on any other {@link Class} than a
 * {@link ConfigurationSerializable} will have no effect.
 * <p>
 * * Synchronized with the commit on 23-April-2019.
 * 
 * @see ConfigurationSerialization#registerClass(Class, String)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializableAs {
    
    /**
     * This is the name your {@link ConfigurationSerialization} {@link Class}
     * will be stored and retrieved as.
     * <p>
     * This name MUST be unique. We recommend using names such as
     * "MyPluginThing" instead of "Thing".
     * 
     * @return The name to serialize the {@link Class} as.
     */
    @NotNull
    String value();
}
