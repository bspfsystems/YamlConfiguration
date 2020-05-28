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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type of {@link ConfigurationSection} that is stored in memory.
 */
public class MemorySection implements ConfigurationSection {
	
	protected final Map<String, Object> map;
	
	private final Configuration root;
	private final ConfigurationSection parent;
	private final String path;
	private final String fullPath;
	
	/**
     * Creates an empty MemorySection for use as a root {@link Configuration}
     * section.
     * <p>
     * Note that calling this without being yourself a {@link Configuration}
     * will throw an exception!
     *
     * @throws IllegalStateException Thrown if this is not a {@link
     *     Configuration} root.
     */
	protected MemorySection() {
		
		if(!(this instanceof Configuration)) {
			throw new IllegalStateException("Cannot construct a root MemorySection when this is not a Configuration.");
		}
		
		this.map = new LinkedHashMap<String, Object>();
		
		this.root = (Configuration) this;
		this.parent = null;
		this.path = "";
		this.fullPath = "";
	}
	
	/**
     * Creates an empty MemorySection with the specified parent and path.
     *
     * @param parent Parent section that contains this own section.
     * @param path Path that you may access this section from via the root
     *     {@link Configuration}.
     * @throws IllegalArgumentException Thrown is parent or path is null, or
     *     if parent contains no root Configuration.
     */
	protected MemorySection(@NotNull final ConfigurationSection parent, @NotNull final String path) {
		
		if(parent == null) {
			throw new IllegalArgumentException("Parent cannot be null.");
		}
		if(path == null) {
			throw new IllegalArgumentException("Path cannot be null.");
		}
		if(parent.getRoot() == null) {
			throw new IllegalArgumentException("Path cannot be orphaned.");
		}
		
		this.map = new LinkedHashMap<String, Object>();
		
		this.root = parent.getRoot();
		this.parent = parent;
		this.path = path;
		this.fullPath = createPath(parent, path);
	}
	
	@NotNull
	@Override
	public Set<String> getKeys(final boolean deep) {
		
		final Set<String> result = new LinkedHashSet<String>();
		final Configuration root = getRoot();
		
		if(root != null && root.options().copyDefaults()) {
			
			final ConfigurationSection defs = getDefaultSection();
			if(defs != null) {
				result.addAll(defs.getKeys(deep));
			}
		}
		
		mapChildrenKeys(result, this, deep);
		return result;
	}
	
	@NotNull
	@Override
	public Map<String, Object> getValues(final boolean deep) {
		
		final Map<String, Object> result = new LinkedHashMap<String, Object>();
		final Configuration root = getRoot();
		
		if(root != null && root.options().copyDefaults()) {
			
			final ConfigurationSection defs = getDefaultSection();
			if(defs != null) {
				result.putAll(defs.getValues(deep));
			}
		}
		
		mapChildrenValues(result, this, deep);
		return result;
	}
	
	@NotNull
	@Override
	public String getCurrentPath() {
		return fullPath;
	}
	
	@NotNull
	@Override
	public String getName() {
		return path;
	}
	
	@Nullable
	@Override
	public Configuration getRoot() {
		return root;
	}
	
	@Nullable
	@Override
	public ConfigurationSection getParent() {
		return parent;
	}
	
	@Nullable
	@Override
	public ConfigurationSection getDefaultSection() {
		
		final Configuration root = getRoot();
		final Configuration defs = root == null ? null : root.getDefaults();
		
		if(defs != null) {
			if(defs.isConfigurationSection(getCurrentPath())) {
				return defs.getConfigurationSection(getCurrentPath());
			}
		}
		
		return null;
	}
	
	@Override
	public void addDefault(@NotNull final String path, @Nullable final Object value) {
		
		if(path == null) {
			throw new IllegalArgumentException("Path cannot be null.");
		}
		
		final Configuration root = getRoot();
		if(root == null) {
			throw new IllegalStateException("Cannot add defaults without a root.");
		}
		if(root == this) {
			throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation.");
		}
		
		root.addDefault(createPath(this, path), value);
	}
	
	@NotNull
	@Override
	public ConfigurationSection createSection(@NotNull final String path) {
		
		if(path == null) {
			throw new IllegalArgumentException("Cannot set to an empty path.");
		}
		if(path.isEmpty()) {
			throw new IllegalArgumentException("Cannot set to an empty path.");
		}
		
		final Configuration root = getRoot();
		if(root == null) {
			throw new IllegalStateException("Cannot create section without a root.");
		}
		
		final char separator = root.options().pathSeparator();
		int lead = -1;
		int tail;
		
		ConfigurationSection section = this;
		while((lead = path.indexOf(separator, tail = lead + 1)) != -1) {
			
			final String node = path.substring(tail, lead);
			final ConfigurationSection subSection = section.getConfigurationSection(node);
			if(subSection == null) {
				section = section.createSection(node);
			}
			else {
				section = subSection;
			}
		}
		
		final String key = path.substring(tail);
		if(section == this) {
			final ConfigurationSection result = new MemorySection(this, key);
			map.put(key, result);
			return result;
		}
		return section.createSection(key);
	}
	
	@NotNull
	@Override
	public ConfigurationSection createSection(@NotNull final String path, @NotNull final Map<?, ?> map) {
		
		final ConfigurationSection section = createSection(path);
		for(final Map.Entry<?, ?> entry : map.entrySet()) {
			
			if(entry.getValue() instanceof Map) {
				section.createSection(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
			}
			else {
				section.set(entry.getKey().toString(), entry.getValue());
			}
		}
		
		return section;
	}
	
	@Override
	public boolean isSet(@NotNull final String path) {
		
		final Configuration root = getRoot();
		if(root == null) {
			return false;
		}
		if(root.options().copyDefaults()) {
			return contains(path);
		}
		return get(path, null) != null;
	}
	
	@Override
	public void set(@NotNull final String path, @Nullable final Object value) {
		
		if(path == null) {
			throw new IllegalArgumentException("Cannot set to an empty path.");
		}
		if(path.isEmpty()) {
			throw new IllegalArgumentException("Cannot set to an empty path.");
		}
		
		final Configuration root = getRoot();
		if(root == null) {
			throw new IllegalStateException("Cannot use section without a root.");
		}
		
		final char separator = root.options().pathSeparator();
		int lead = -1;
		int tail;
		
		ConfigurationSection section = this;
		while((lead = path.indexOf(separator, tail = lead + 1)) != -1) {
			
			final String node = path.substring(lead, tail);
			final ConfigurationSection subSection = section.getConfigurationSection(node);
			
			if(subSection == null) {
				if(value == null) {
					return;
				}
				section = section.createSection(node);
			}
			else {
				section = subSection;
			}
		}
		
		final String key = path.substring(tail);
		if(section == this) {
			if(value == null) {
				map.remove(key);
			}
			else {
				map.put(key, value);
			}
		}
		else {
			section.set(key, value);
		}
	}
	
	@Override
	public boolean contains(@NotNull final String path) {
		return contains(path, false);
	}
	
	@Override
	public boolean contains(@NotNull final String path, final boolean ignoreDefault) {
		return ((ignoreDefault) ? get(path, null) : get(path)) != null;
	}
	
	@Nullable
	@Override
	public Object get(@NotNull final String path) {
		return get(path, getDefault(path));
	}
	
	@Nullable
	@Override
	public Object get(@NotNull final String path, @Nullable final Object def) {
		
		if(path == null) { 
			throw new IllegalArgumentException("Path cannot be null.");
		}
		if(path.length() == 0) {
			return this;
		}
		
		final Configuration root = getRoot();
		if(root == null) {
			throw new IllegalStateException("Cannot access section without a root.");
		}
		
		final char separator = root.options().pathSeparator();
		int lead = -1;
		int tail;
		
		ConfigurationSection section = this;
		while((lead = path.indexOf(separator, tail = lead + 1)) != -1) {
			section = section.getConfigurationSection(path.substring(tail, lead));
			if(section == null) {
				return def;
			}
		}
		
		final String key = path.substring(tail);
		if(section == this) {
			final Object result = map.get(key);
			return result == null ? def : result;
		}
		return section.get(key, def);
	}
	
	@Override
	public boolean isBoolean(@NotNull final String path) {
		return get(path) instanceof Boolean;
	}
	
	@Override
	public boolean getBoolean(@NotNull final String path) {
		final Object def = getDefault(path);
		return getBoolean(path, def instanceof Boolean ? ((Boolean) def).booleanValue() : false);
	}
	
	@Override
	public boolean getBoolean(@NotNull final String path, final boolean def) {
		final Object val = get(path, def);
		return val instanceof Boolean ? ((Boolean) val).booleanValue() : def;
	}
	
	@Override
	public boolean isByte(@NotNull final String path) {
		return get(path) instanceof Byte;
	}
	
	@Override
	public byte getByte(@NotNull final String path) {
		final Object def = getDefault(path);
		return getByte(path, def instanceof Number ? ((Number) def).byteValue() : (byte) 0);
	}
	
	@Override
	public byte getByte(@NotNull final String path, final byte def) {
		final Object val = get(path, def);
		return val instanceof Number ? ((Number) val).byteValue() : def;
	}
	
	@Override
	public boolean isShort(@NotNull final String path) {
		return get(path) instanceof Short;
	}
	
	@Override
	public short getShort(@NotNull final String path) {
		final Object def = getDefault(path);
		return getShort(path, def instanceof Number ? ((Number) def).shortValue() : (short) 0);
	}
	
	@Override
	public short getShort(@NotNull final String path, final short def) {
		final Object val = get(path, def);
		return val instanceof Number ? ((Number) val).shortValue() : def;
	}
	
	@Override
	public boolean isInt(@NotNull final String path) {
		return get(path) instanceof Integer;
	}
	
	@Override
	public int getInt(@NotNull final String path) {
		final Object def = getDefault(path);
		return getInt(path, def instanceof Number ? ((Number) def).intValue() : 0);
	}
	
	@Override
	public int getInt(@NotNull final String path, final int def) {
		final Object val = get(path, def);
		return val instanceof Number ? ((Number) val).intValue() : def;
	}
	
	@Override
	public boolean isLong(@NotNull final String path) {
		return get(path) instanceof Long;
	}
	
	@Override
	public long getLong(@NotNull final String path) {
		final Object def = getDefault(path);
		return getLong(path, def instanceof Number ? ((Long) def).longValue() : 0L);
	}
	
	@Override
	public long getLong(@NotNull final String path, final long def) {
		final Object val = get(path, def);
		return val instanceof Number ? ((Long) val).longValue() : def;
	}
	
	@Override
	public boolean isFloat(@NotNull final String path) {
		return get(path) instanceof Float;
	}
	
	@Override
	public float getFloat(@NotNull final String path) {
		final Object def = getDefault(path);
		return getFloat(path, def instanceof Number ? ((Number) def).floatValue() : 0.0F);
	}
	
	@Override
	public float getFloat(@NotNull final String path, final float def) {
		final Object val = get(path, def);
		return val instanceof Number ? ((Number) val).floatValue() : def;
	}
	
	@Override
	public boolean isDouble(@NotNull final String path) {
		return get(path) instanceof Double;
	}
	
	@Override
	public double getDouble(@NotNull final String path) {
		final Object def = getDefault(path);
		return getDouble(path, def instanceof Number ? ((Number) def).doubleValue() : 0.0D);
	}
	
	@Override
	public double getDouble(@NotNull final String path, final double def) {
		final Object val = get(path, def);
		return val instanceof Number ? ((Number) val).doubleValue() : def;
	}
	
	@Override
	public boolean isChar(@NotNull final String path) {
		return get(path) instanceof Boolean;
	}
	
	@Override
	public char getChar(@NotNull final String path) {
		final Object def = getDefault(path);
		return getChar(path, def instanceof Character ? ((Character) def).charValue() : '\u0000');
	}
	
	@Override
	public char getChar(@NotNull final String path, final char def) {
		final Object val = get(path, def);
		return val instanceof Character ? ((Character) val).charValue() : def;
	}
	
	@Override
	public boolean isString(@NotNull final String path) {
		return get(path) instanceof String;
	}
	
	@Nullable
	@Override
	public String getString(@NotNull final String path) {
		final Object def = getDefault(path);
		return getString(path, def != null ? def.toString() : null);
	}
	
	@Nullable
	@Override
	public String getString(@NotNull final String path, @Nullable final String def) {
		final Object val = get(path, def);
		return val != null ? val.toString() : def;
	}
	
	@Override
	public boolean isList(@NotNull final String path) {
		return get(path) instanceof List;
	}
	
	@Nullable
	@Override
	public List<?> getList(@NotNull final String path) {
		final Object def = getDefault(path);
		return getList(path, def instanceof List ? (List<?>) def : null);
	}
	
	@Nullable
	@Override
	public List<?> getList(@NotNull final String path, @Nullable final List<?> def) {
		final Object val = get(path, def);
		return val instanceof List ? (List<?>) val : def;
	}
	
	@Override
	public boolean isConfigurationSection(@NotNull final String path) {
		return get(path) instanceof ConfigurationSection;
	}
	
	@Nullable
	@Override
	public ConfigurationSection getConfigurationSection(@NotNull final String path) {
		
		Object val = get(path, null);
		if(val != null) {
			return val instanceof ConfigurationSection ? (ConfigurationSection) val : null;
		}
		
		val = get(path, getDefault(path));
		return val instanceof ConfigurationSection ? createSection(path) : null;
	}
	
	@Nullable
	@Override
	public <T extends Object> T getObject(@NotNull final String path, @NotNull final Class<T> clazz) {
		if(clazz == null) {
			throw new IllegalArgumentException("Class cannot be null.");
		}
		final Object def = getDefault(path);
		return getObject(path, clazz, def != null && clazz.isInstance(def) ? clazz.cast(def) : null);
	}
	
	@Nullable
	@Override
	public <T extends Object> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
		if(clazz == null) {
			throw new IllegalArgumentException("Class cannot be null.");
		}
		final Object val = get(path, def);
		return val != null && clazz.isInstance(val) ? clazz.cast(val) : def;
	}
	
	@Nullable
	@Override
	public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz) {
		return getObject(path, clazz);
	}
	
	@Nullable
	@Override
	public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
		return getObject(path, clazz, def);
	}
	
	@NotNull
	@Override
	public List<Boolean> getBooleanList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Boolean>(0);
		}
		
		final List<Boolean> result = new ArrayList<Boolean>();
		for(final Object object : list) {
			
			if(object instanceof Boolean) {
				result.add((Boolean) object);
			}
			else if(object instanceof String) {
				result.add(Boolean.valueOf((String) object));
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Byte> getByteList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Byte>(0);
		}
		
		final List<Byte> result = new ArrayList<Byte>();
		for(final Object object : list) {
			
			if(object instanceof Byte) {
				result.add((Byte) object);
			}
			else if(object instanceof String) {
				try {
					result.add(Byte.valueOf((String) object));
				}
				catch(NumberFormatException e) {
					// Ignore
				}
			}
			else if(object instanceof Character) {
				try {
					result.add(Byte.valueOf((byte) ((Character) object).charValue()));
				}
				catch(ClassCastException e) {
					// Ignore
				}
			}
			else if(object instanceof Number) {
				result.add(Byte.valueOf(((Number) object).byteValue()));
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Short> getShortList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Short>(0);
		}
		
		final List<Short> result = new ArrayList<Short>();
		for(final Object object : list) {
			
			if(object instanceof Short) {
				result.add((Short) object);
			}
			else if(object instanceof String) {
				try {
					result.add(Short.valueOf((String) object));
				}
				catch(NumberFormatException e) {
					// Ignore
				}
			}
			else if(object instanceof Character) {
				try {
					result.add(Short.valueOf((short) ((Character) object).charValue()));
				}
				catch(ClassCastException e) {
					// Ignore
				}
			}
			else if(object instanceof Number) {
				result.add(Short.valueOf(((Number) object).shortValue()));
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Integer> getIntList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Integer>(0);
		}
		
		final List<Integer> result = new ArrayList<Integer>();
		for(final Object object : list) {
			
			if(object instanceof Integer) {
				result.add((Integer) object);
			}
			else if(object instanceof String) {
				try {
					result.add(Integer.valueOf((String) object));
				}
				catch(NumberFormatException e) {
					// Ignore
				}
			}
			else if(object instanceof Character) {
				try {
					result.add(Integer.valueOf((int) ((Character) object).charValue()));
				}
				catch(ClassCastException e) {
					// Ignore
				}
			}
			else if(object instanceof Number) {
				result.add(Integer.valueOf(((Number) object).intValue()));
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Long> getLongList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Long>(0);
		}
		
		final List<Long> result = new ArrayList<Long>();
		for(final Object object : list) {
			
			if(object instanceof Long) {
				result.add((Long) object);
			}
			else if(object instanceof String) {
				try {
					result.add(Long.valueOf((String) object));
				}
				catch(NumberFormatException e) {
					// Ignore
				}
			}
			else if(object instanceof Character) {
				try {
					result.add(Long.valueOf((long) ((Character) object).charValue()));
				}
				catch(ClassCastException e) {
					// Ignore
				}
			}
			else if(object instanceof Number) {
				result.add(Long.valueOf(((Number) object).longValue()));
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Float> getFloatList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Float>(0);
		}
		
		final List<Float> result = new ArrayList<Float>();
		for(final Object object : list) {
			
			if(object instanceof Float) {
				result.add((Float) object);
			}
			else if(object instanceof String) {
				try {
					result.add(Float.valueOf((String) object));
				}
				catch(NumberFormatException e) {
					// Ignore
				}
			}
			else if(object instanceof Character) {
				try {
					result.add(Float.valueOf((float) ((Character) object).charValue()));
				}
				catch(ClassCastException e) {
					// Ignore
				}
			}
			else if(object instanceof Number) {
				result.add(Float.valueOf(((Number) object).floatValue()));
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Double> getDoubleList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Double>(0);
		}
		
		final List<Double> result = new ArrayList<Double>();
		for(final Object object : list) {
			
			if(object instanceof Double) {
				result.add((Double) object);
			}
			else if(object instanceof String) {
				try {
					result.add(Double.valueOf((String) object));
				}
				catch(NumberFormatException e) {
					// Ignore
				}
			}
			else if(object instanceof Character) {
				try {
					result.add(Double.valueOf((double) ((Character) object).charValue()));
				}
				catch(ClassCastException e) {
					// Ignore
				}
			}
			else if(object instanceof Number) {
				result.add(Double.valueOf(((Number) object).doubleValue()));
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Character> getCharList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Character>(0);
		}
		
		final List<Character> result = new ArrayList<Character>();
		for(final Object object : list) {
			
			if(object instanceof Character) {
				result.add((Character) object);
			}
			else if(object instanceof String) {
				if(((String) object).length() == 1) {
					result.add(Character.valueOf(((String) object).charAt(0)));
				}
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<String> getStringList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<String>(0);
		}
		
		final List<String> result = new ArrayList<String>();
		for(final Object object : list) {
			if(object != null) {
				result.add(object.toString());
			}
		}
		
		return result;
	}
	
	@NotNull
	@Override
	public List<Map<?, ?>> getMapList(@NotNull final String path) {
		
		final List<?> list = getList(path);
		if(list == null) {
			return new ArrayList<Map<?, ?>>(0);
		}
		
		final List<Map<?, ?>> result = new ArrayList<Map<?, ?>>();
		for(final Object object : list) {
			if(object instanceof Map) {
				result.add((Map<?, ?>) object);
			}
		}
		
		return result;
	}
	
	@Nullable
	protected Object getDefault(@NotNull final String path) {
		
		if(path == null) {
			throw new IllegalArgumentException("Path cannot be null.");
		}
		
		final Configuration root = getRoot();
		final Configuration defs = root == null ? null : root.getDefaults();
		return defs == null ? null : defs.get(createPath(this, path));
	}
	
	protected void mapChildrenKeys(@NotNull final Set<String> output, @NotNull final ConfigurationSection section, final boolean deep) {
		
		if(section instanceof MemorySection) {
			
			final MemorySection memorySection = (MemorySection) section;
			for(final Map.Entry<String, Object> entry : memorySection.map.entrySet()) {
				output.add(createPath(section, entry.getKey(), this));
				
				if(deep && entry.getValue() instanceof ConfigurationSection) {
					mapChildrenKeys(output, (ConfigurationSection) entry.getValue(), deep);
				}
			}
		}
		else {
			
			final Set<String> keys = section.getKeys(deep);
			for(final String key : keys) {
				output.add(createPath(section, key, this));
			}
		}
	}
	
	protected void mapChildrenValues(@NotNull final Map<String, Object> output, @NotNull final ConfigurationSection section, final boolean deep) {
		
		if(section instanceof MemorySection) {
			
			final MemorySection memorySection = (MemorySection) section;
			for(final Map.Entry<String, Object> entry : memorySection.map.entrySet()) {
				
				final String path = createPath(section, entry.getKey(), this);
				output.remove(path);
				output.put(path, entry.getValue());
				
				if(deep && entry.getValue() instanceof ConfigurationSection) {
					mapChildrenValues(output, (ConfigurationSection) entry.getValue(), deep);
				}
			}
		}
		else {
			
			final Map<String, Object> values = section.getValues(deep);
			for(final Map.Entry<String, Object> entry : values.entrySet()) {
				output.put(createPath(section, entry.getKey(), this), entry.getValue());
			}
		}
	}
	
	@Override
	public String toString() {
		
		final Configuration root = getRoot();
		final StringBuilder builder = new StringBuilder();
		
		builder.append(getClass().getSimpleName());
		builder.append("[path='");
		builder.append(getCurrentPath());
		builder.append("', root='");
		builder.append(root == null ? null : root.getClass().getSimpleName());
		builder.append("']");
		
		return builder.toString();
	}
	
	/**
     * Creates a full path to the given {@link ConfigurationSection} from its
     * root {@link Configuration}.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not
     * only {@link MemorySection}.
     *
     * @param section Section to create a path for.
     * @param key Name of the specified section.
     * @return Full path of the section from its root.
     */
	@NotNull
	public static String createPath(@NotNull final ConfigurationSection section, @Nullable final String key) {
		return createPath(section, key, ((section == null) ? null : section.getRoot()));
	}
	
	/**
     * Creates a relative path to the given {@link ConfigurationSection} from
     * the given relative section.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not
     * only {@link MemorySection}.
     *
     * @param section Section to create a path for.
     * @param key Name of the specified section.
     * @param relative Section to create the path relative to.
     * @return Full path of the section from its root.
     */
	@NotNull
	public static String createPath(@NotNull final ConfigurationSection section, @Nullable final String key, @Nullable final ConfigurationSection relative) {
		
		if(section == null) {
			throw new IllegalArgumentException("Cannot create path without a section.");
		}
		
		final Configuration root = section.getRoot();
		if(root == null) {
			throw new IllegalStateException("Cannot create path without a root.");
		}
		
		final char separator = root.options().pathSeparator();
		final StringBuilder builder = new StringBuilder();
		
		if(section != null) {
			for(ConfigurationSection parent = section; parent != null && parent != relative; parent = parent.getParent()) {
				
				if(builder.length() > 0) {
					builder.insert(0, separator);
				}
				builder.insert(0, parent.getName());
			}
		}
		
		if(key != null && key.length() > 0) {
			if(builder.length() > 0) {
				builder.append(separator);
			}
			builder.append(key);
		}
		
		return builder.toString();
	}
}
