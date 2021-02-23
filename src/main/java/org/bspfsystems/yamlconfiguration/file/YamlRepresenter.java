/*
 * This file is part of YamlConfiguration.
 *
 * Implementation of SnakeYAML to be easy to use with files.
 *
 * Copyright (C) 2014-2021 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2021 BSPF Systems, LLC (https://bspfsystems.org/)
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

import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;

public final class YamlRepresenter extends Representer {
	
	private class RepresentConfigurationSection extends RepresentMap {
		
		private RepresentConfigurationSection() {
			super();
		}
		
		@NotNull
		@Override
		public Node representData(@NotNull final Object object) {
			return super.representData(((ConfigurationSection) object).getValues(false));
		}
	}
	
	private class RepresentConfigurationSerializable extends RepresentMap {
		
		private RepresentConfigurationSerializable() {
			super();
		}
		
		@NotNull
		@Override
		public Node representData(@NotNull Object object) {
			
			final ConfigurationSerializable serializable = (ConfigurationSerializable) object;
			final Map<String, Object> values = new LinkedHashMap<String, Object>();
			
			values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
			values.putAll(serializable.serialize());
			
			return super.representData(values);
		}
	}
	
	public YamlRepresenter() {
		this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
		this.multiRepresenters.put(ConfigurationSerializable.class, new RepresentConfigurationSerializable());
		this.multiRepresenters.remove(Enum.class);
	}
}
