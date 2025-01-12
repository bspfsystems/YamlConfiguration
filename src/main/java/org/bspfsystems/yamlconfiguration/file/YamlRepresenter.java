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

package org.bspfsystems.yamlconfiguration.file;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Represents a representer that can work with configuration sections and
 * configuration serializables.
 * <p>
 * Synchronized with the commit on 24-November-2024.
 */
public final class YamlRepresenter extends Representer {
    
    /**
     * Represents a represent map that works with configuration sections.
     */
    private final class RepresentConfigurationSection extends RepresentMap {
    
        /**
         * Constructs a basic represent configuration section.
         * 
         * @see RepresentMap#RepresentMap()
         */
        private RepresentConfigurationSection() {
            super();
        }
    
        /**
         * Converts the given object (known to be a configuration section) into
         * a node.
         * 
         * @param object The object to represent.
         * @return The node converted from the given object.
         * @see RepresentMap#representData(Object)
         */
        @NotNull
        @Override
        public Node representData(@NotNull final Object object) {
            return super.representData(((ConfigurationSection) object).getValues(false));
        }
    }
    
    /**
     * Represents a represent map that works with configuration serializables.
     */
    private class RepresentConfigurationSerializable extends RepresentMap {
    
        /**
         * Constructs a basic represent configuration serializable.
         * 
         * @see RepresentMap#RepresentMap()
         */
        private RepresentConfigurationSerializable() {
            super();
        }
    
        /**
         * Converts the given object (known to be a configuration serializable)
         * into a node.
         * 
         * @param object The object to represent.
         * @return The node converted from the given object.
         * @see RepresentMap#representData(Object)
         */
        @NotNull
        @Override
        public Node representData(@NotNull final Object object) {
            
            final ConfigurationSerializable serializable = (ConfigurationSerializable) object;
            final Map<String, Object> values = new LinkedHashMap<String, Object>();
            
            values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
            values.putAll(serializable.serialize());
            
            return super.representData(values);
        }
    }
    
    /**
     * Constructs a YAML representer that can represent configuration sections
     * and configuration serializables, while disallowing enums.
     * 
     * @param dumperOptions The dumper options used while dumping YAML via the
     *                      new representer.
     */
    YamlRepresenter(@NotNull final DumperOptions dumperOptions) {
        super(dumperOptions);
        
        this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
        this.multiRepresenters.put(ConfigurationSerializable.class, new RepresentConfigurationSerializable());
        this.multiRepresenters.remove(Enum.class);
    }
}
