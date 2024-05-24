/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2023 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2024 BSPF Systems, LLC (https://bspfsystems.org/)
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
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Represents a custom safe constructor for use with a YAML configuration.
 * <p>
 * Synchronized with the commit on 14-March-2023.
 */
public final class YamlConstructor extends SafeConstructor {
    
    /**
     * Represents a custom construct YAML map for use with a YAML configuration.
     */
    private final class ConstructCustomObject extends ConstructYamlMap {
    
        /**
         * Constructs an empty construct custom object.
         * 
         * @see ConstructYamlMap#ConstructYamlMap()
         */
        private ConstructCustomObject() {
            super();
        }
    
        /**
         * Converts the given node into an object.
         *
         * @param node The node to convert.
         * @return The object converted from the given node.
         */
        @Nullable
        @Override
        public Object construct(@NotNull final Node node) {
            
            if (node.isTwoStepsConstruction()) {
                throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
            }
            
            final Map<?, ?> raw = (Map<?, ?>) super.construct(node);
            if (raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                
                final Map<String, Object> typed = new LinkedHashMap<String, Object>(raw.size());
                for (final Map.Entry<?, ?> entry : raw.entrySet()) {
                    typed.put(entry.getKey().toString(), entry.getValue());
                }
                
                try {
                    return ConfigurationSerialization.deserializeObject(typed);
                } catch (IllegalArgumentException e) {
                    throw new YAMLException("Could not deserialize object.", e);
                }
            }
            
            return raw;
        }
    
        /**
         * Disallows the 2nd step of constructing an object from a node.
         * 
         * @param node The composed node.
         * @param object The object constructed earlier by the initial
         *               construction step for the given node.
         */
        @Override
        public void construct2ndStep(@NotNull final Node node, @NotNull final Object object) {
            throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
        }
    }
    
    /**
     * Constructs a YAML constructor.
     * 
     * @param loaderOptions The loader options used while loading YAML via the
     *                      new constructor.
     * @see SafeConstructor#SafeConstructor(LoaderOptions);
     */
    YamlConstructor(@NotNull final LoaderOptions loaderOptions) {
        super(loaderOptions);
        this.yamlConstructors.put(Tag.MAP, new ConstructCustomObject());
    }
    
    /**
     * Converts the given node into an object.
     * 
     * @param node The node to convert.
     * @return The object converted from the given node.
     */
    @Nullable
    Object construct(@NotNull final Node node) {
        return this.constructObject(node);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void flattenMapping(@NotNull final MappingNode mappingNode) {
        super.flattenMapping(mappingNode);
    }
}
