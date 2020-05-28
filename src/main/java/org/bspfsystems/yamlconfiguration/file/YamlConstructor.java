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

import java.util.LinkedHashMap;
import java.util.Map;

import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public final class YamlConstructor extends SafeConstructor {
	
	private class ConstructCustomObject extends ConstructYamlMap {
		
		private ConstructCustomObject() {
			super();
		}
		
		@Nullable
		@Override
		public Object construct(@NotNull final Node node) {
			
			if(node.isTwoStepsConstruction()) {
				throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
			}
			
			final Map<?, ?> raw = (Map<?, ?>) super.construct(node);
			if(raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
				
				final Map<String, Object> typed = new LinkedHashMap<String, Object>(raw.size());
				for(final Map.Entry<?, ?> entry : raw.entrySet()) {
					typed.put(entry.getKey().toString(), entry.getValue());
				}
				
				try {
					return ConfigurationSerialization.deserializeObject(typed);
				}
				catch(IllegalArgumentException e) {
					throw new YAMLException("Could not deserialize object.", e);
				}
			}
			
			return raw;
		}
		
		@Override
		public void construct2ndStep(@NotNull final Node node, @NotNull final Object object) {
			throw new YAMLException("Unexpected referntial mapping struture. Node: " + node);
		}
	}
	
	public YamlConstructor() {
		this.yamlConstructors.put(Tag.MAP, new ConstructCustomObject());
	}
}
