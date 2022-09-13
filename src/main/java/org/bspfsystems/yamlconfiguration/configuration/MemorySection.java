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

package org.bspfsystems.yamlconfiguration.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type of {@link ConfigurationSection} that is stored in memory.
 * <p>
 * Synchronized with the commit on 07-June-2022.
 */
public class MemorySection implements ConfigurationSection {
    
    protected final Map<String, SectionPathData> map;
    
    private final Configuration root;
    private final ConfigurationSection parent;
    private final String path;
    private final String fullPath;
    
    /**
     * Creates an empty {@link MemorySection} for use as a root
     * {@link ConfigurationSection}.
     * <p>
     * <b>Note:</b> If the new {@link MemorySection} is not a
     * {@link Configuration}, an {@link IllegalStateException} will be thrown.
     * 
     * @throws IllegalStateException If the new {@link MemorySection} is not a
     *                               {@link Configuration}.
     */
    protected MemorySection() throws IllegalStateException {
        
        if (!(this instanceof Configuration)) {
            throw new IllegalStateException("Cannot construct a root MemorySection when this is not a Configuration.");
        }
        
        this.map = new LinkedHashMap<String, SectionPathData>();
        
        this.root = (Configuration) this;
        this.parent = null;
        this.path = "";
        this.fullPath = "";
    }
    
    /**
     * Creates an empty {@link MemorySection} with the specified parent and
     * path.
     * 
     * @param parent The parent {@link ConfigurationSection} that contains the
     *               new {@link MemorySection}.
     * @param path The path that the new {@link MemorySection} will be set on.
     * @throws IllegalArgumentException If the parent or path is {@code null},
     *                                  or if the parent contains no root
     *                                  {@link Configuration}.
     */
    private MemorySection(@NotNull final ConfigurationSection parent, @NotNull final String path) {
        
        if (parent.getRoot() == null) {
            throw new IllegalArgumentException("Path cannot be orphaned.");
        }
        
        this.map = new LinkedHashMap<String, SectionPathData>();
        
        this.root = parent.getRoot();
        this.parent = parent;
        this.path = path;
        this.fullPath = createPath(parent, path);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Set<String> getKeys(final boolean deep) {
        
        final Set<String> result = new LinkedHashSet<String>();
        final Configuration root = this.getRoot();
        
        if (root != null && root.getOptions().getCopyDefaults()) {
            
            final ConfigurationSection defs = this.getDefaultSection();
            if (defs != null) {
                result.addAll(defs.getKeys(deep));
            }
        }
        
        this.mapChildrenKeys(result, this, deep);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Map<String, Object> getValues(final boolean deep) {
        
        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        final Configuration root = this.getRoot();
        
        if (root != null && root.getOptions().getCopyDefaults()) {
            
            final ConfigurationSection defs = this.getDefaultSection();
            if (defs != null) {
                result.putAll(defs.getValues(deep));
            }
        }
        
        this.mapChildrenValues(result, this, deep);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getCurrentPath() {
        return this.fullPath;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getName() {
        return this.path;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public Configuration getRoot() {
        return this.root;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public ConfigurationSection getParent() {
        return this.parent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public ConfigurationSection getDefaultSection() {
        
        final Configuration root = this.getRoot();
        final Configuration defs = root == null ? null : root.getDefaults();
        
        if (defs != null) {
            if (defs.isConfigurationSection(this.getCurrentPath())) {
                return defs.getConfigurationSection(this.getCurrentPath());
            }
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefault(@NotNull final String path, @Nullable final Object value) {
        
        final Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot add defaults without a root.");
        }
        if (root == this) {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation.");
        }
        
        root.addDefault(MemorySection.createPath(this, path), value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<String> getComments(@NotNull final String path) {
        final SectionPathData data = this.getSectionPathData(path);
        return data == null ? Collections.emptyList() : data.getComments();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<String> getInLineComments(@NotNull final String path) {
        final SectionPathData data = this.getSectionPathData(path);
        return data == null ? Collections.emptyList() : data.getInLineComments();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setComments(@NotNull final String path, @Nullable final List<String> comments) {
        final SectionPathData data = this.getSectionPathData(path);
        if (data != null) {
            data.setComments(comments);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setInLineComments(@NotNull final String path, @Nullable final List<String> inLineComments) {
        final SectionPathData data = this.getSectionPathData(path);
        if (data != null) {
            data.setInLineComments(inLineComments);
        }
    }
    
    /**
     * Gets the {@link SectionPathData} at the given path, or {@code null} if
     * none exists.
     * 
     * @param path The path of the {@link SectionPathData} to retrieve.
     * @return The {@link SectionPathData}, or {@code null} if none exists.
     */
    @Nullable
    private SectionPathData getSectionPathData(@NotNull final String path) {
        
        final Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot access section without a root.");
        }
        
        final char separator = root.getOptions().getPathSeparator();
        int lead = -1;  // Leading (higher) index.
        int trail;      // Trailing (lower) index.
        
        ConfigurationSection section = this;
        while ((lead = path.indexOf(separator, trail = lead + 1)) != -1) {
            
            section = section.getConfigurationSection(path.substring(trail, lead));
            if (section == null) {
                return null;
            }
        }
        
        final String key = path.substring(trail);
        if (section == this) {
            return this.map.get(key);
        } else if (section instanceof MemorySection) {
            return ((MemorySection) section).getSectionPathData(key);
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ConfigurationSection createSection(@NotNull final String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Cannot set to an empty path.");
        }
        
        final Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create section without a root.");
        }
        
        final char separator = root.getOptions().getPathSeparator();
        int lead = -1;
        int tail;
        
        ConfigurationSection section = this;
        while ((lead = path.indexOf(separator, tail = lead + 1)) != -1) {
            
            final String node = path.substring(tail, lead);
            final ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }
        
        final String key = path.substring(tail);
        if (section == this) {
            final ConfigurationSection result = new MemorySection(this, key);
            this.map.put(key, new SectionPathData(result));
            return result;
        }
        return section.createSection(key);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ConfigurationSection createSection(@NotNull final String path, @NotNull final Map<?, ?> map) {
        
        final ConfigurationSection section = createSection(path);
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            
            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
            } else {
                section.set(entry.getKey().toString(), entry.getValue());
            }
        }
        
        return section;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSet(@NotNull final String path) {
        
        final Configuration root = this.getRoot();
        if (root == null) {
            return false;
        }
        if (root.getOptions().getCopyDefaults()) {
            return this.contains(path);
        }
        return this.get(path, null) != null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void set(@NotNull final String path, @Nullable final Object value) {
        
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Cannot set to an empty path.");
        }
        
        final Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root.");
        }
        
        final char separator = root.getOptions().getPathSeparator();
        int lead = -1;  // Leading (higher) index.
        int trail;      // Trailing (lower) index.
        
        ConfigurationSection section = this;
        while ((lead = path.indexOf(separator, trail = lead + 1)) != -1) {
            
            final String node = path.substring(trail, lead);
            final ConfigurationSection subSection = section.getConfigurationSection(node);
            
            if (subSection == null) {
                if (value == null) {
                    return;
                }
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }
        
        final String key = path.substring(trail);
        if (section == this) {
            if (value == null) {
                this.map.remove(key);
            } else {
                final SectionPathData data = this.map.get(key);
                if (data == null) {
                    this.map.put(key, new SectionPathData(value));
                } else {
                    data.setData(value);
                }
            }
        } else {
            section.set(key, value);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull final String path) {
        return this.contains(path, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull final String path, final boolean ignoreDefault) {
        return ((ignoreDefault) ? this.get(path, null) : this.get(path)) != null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public Object get(@NotNull final String path) {
        return this.get(path, this.getDefault(path));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public Object get(@NotNull final String path, @Nullable final Object def) {
        
        if (path.length() == 0) {
            return this;
        }
        
        final Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot access section without a root.");
        }
        
        final char separator = root.getOptions().getPathSeparator();
        int lead = -1;  // Leading (higher) index.
        int trail;      // Trailing (lower) index.
        
        ConfigurationSection section = this;
        while ((lead = path.indexOf(separator, trail = lead + 1)) != -1) {
            
            final String currentPath = path.substring(trail, lead);
            if (!section.contains(currentPath, true)) {
                return def;
            }
            
            section = section.getConfigurationSection(currentPath);
            if (section == null) {
                return def;
            }
        }
        
        final String key = path.substring(trail);
        if (section == this) {
            final SectionPathData result = this.map.get(key);
            return (result == null) ? def : result.getData();
        }
        return section.get(key, def);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoolean(@NotNull final String path) {
        return this.get(path) instanceof Boolean;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getBoolean(path, def instanceof Boolean && (Boolean) def);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(@NotNull final String path, final boolean def) {
        final Object val = this.get(path, def);
        return val instanceof Boolean ? (Boolean) val : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isByte(@NotNull final String path) {
        return this.get(path) instanceof Byte;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getByte(path, def instanceof Number ? ((Number) def).byteValue() : (byte) 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(@NotNull final String path, final byte def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).byteValue() : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShort(@NotNull final String path) {
        return this.get(path) instanceof Short;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getShort(path, def instanceof Number ? ((Number) def).shortValue() : (short) 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(@NotNull final String path, final short def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).shortValue() : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInt(@NotNull final String path) {
        return this.get(path) instanceof Integer;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getInt(path, def instanceof Number ? ((Number) def).intValue() : 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(@NotNull final String path, final int def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).intValue() : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLong(@NotNull final String path) {
        return this.get(path) instanceof Long;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getLong(path, def instanceof Number ? (Long) def : 0L);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(@NotNull final String path, final long def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? (Long) val : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFloat(@NotNull final String path) {
        return this.get(path) instanceof Float;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getFloat(path, def instanceof Number ? ((Number) def).floatValue() : 0.0F);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(@NotNull final String path, final float def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).floatValue() : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDouble(@NotNull final String path) {
        return this.get(path) instanceof Double;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getDouble(path, def instanceof Number ? ((Number) def).doubleValue() : 0.0D);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(@NotNull final String path, final double def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).doubleValue() : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChar(@NotNull final String path) {
        return this.get(path) instanceof Boolean;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public char getChar(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getChar(path, def instanceof Character ? (Character) def : '\u0000');
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public char getChar(@NotNull final String path, final char def) {
        final Object val = this.get(path, def);
        return val instanceof Character ? (Character) val : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isString(@NotNull final String path) {
        return this.get(path) instanceof String;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getString(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getString(path, def != null ? def.toString() : null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public String getString(@NotNull final String path, @Nullable final String def) {
        final Object val = this.get(path, def);
        return val != null ? val.toString() : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isList(@NotNull final String path) {
        return this.get(path) instanceof List;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public List<?> getList(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getList(path, def instanceof List ? (List<?>) def : null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public List<?> getList(@NotNull final String path, @Nullable final List<?> def) {
        final Object val = this.get(path, def);
        return val instanceof List ? (List<?>) val : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConfigurationSection(@NotNull final String path) {
        return this.get(path) instanceof ConfigurationSection;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public ConfigurationSection getConfigurationSection(@NotNull final String path) {
        
        Object val = this.get(path, null);
        if (val != null) {
            return val instanceof ConfigurationSection ? (ConfigurationSection) val : null;
        }
        
        val = this.get(path, this.getDefault(path));
        return val instanceof ConfigurationSection ? this.createSection(path) : null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz) {
        final Object def = this.getDefault(path);
        return this.getObject(path, clazz, clazz.isInstance(def) ? clazz.cast(def) : null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, _, !null -> !null")
    @Nullable
    public <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
        final Object val = this.get(path, def);
        return clazz.isInstance(val) ? clazz.cast(val) : def;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz) {
        return this.getObject(path, clazz);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, _, !null -> !null")
    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
        return this.getObject(path, clazz, def);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Boolean> getBooleanList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Boolean>(0);
        }
        
        final List<Boolean> result = new ArrayList<Boolean>();
        for (final Object object : list) {
            
            if (object instanceof Boolean) {
                result.add((Boolean) object);
            } else if(object instanceof String) {
                result.add(Boolean.valueOf((String) object));
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Byte> getByteList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Byte>(0);
        }
        
        final List<Byte> result = new ArrayList<Byte>();
        for (final Object object : list) {
            
            if(object instanceof Byte) {
                result.add((Byte) object);
            } else if (object instanceof String) {
                try {
                    result.add(Byte.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((byte) ((Character) object).charValue());
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).byteValue());
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Short> getShortList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Short>(0);
        }
        
        final List<Short> result = new ArrayList<Short>();
        for (final Object object : list) {
            
            if (object instanceof Short) {
                result.add((Short) object);
            } else if(object instanceof String) {
                try {
                    result.add(Short.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((short) ((Character) object).charValue());
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).shortValue());
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Integer> getIntList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Integer>(0);
        }
        
        final List<Integer> result = new ArrayList<Integer>();
        for (final Object object : list) {
            
            if (object instanceof Integer) {
                result.add((Integer) object);
            } else if(object instanceof String) {
                try {
                    result.add(Integer.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((int) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).intValue());
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Long> getLongList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Long>(0);
        }
        
        final List<Long> result = new ArrayList<Long>();
        for (final Object object : list) {
            
            if (object instanceof Long) {
                result.add((Long) object);
            } else if (object instanceof String) {
                try {
                    result.add(Long.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((long) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).longValue());
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Float> getFloatList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Float>(0);
        }
        
        final List<Float> result = new ArrayList<Float>();
        for (final Object object : list) {
            
            if (object instanceof Float) {
                result.add((Float) object);
            } else if (object instanceof String) {
                try {
                    result.add(Float.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((float) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).floatValue());
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Double> getDoubleList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Double>(0);
        }
        
        final List<Double> result = new ArrayList<Double>();
        for (final Object object : list) {
            
            if (object instanceof Double) {
                result.add((Double) object);
            } else if (object instanceof String) {
                try {
                    result.add(Double.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((double) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).doubleValue());
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Character> getCharList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Character>(0);
        }
        
        final List<Character> result = new ArrayList<Character>();
        for (final Object object : list) {
            
            if (object instanceof Character) {
                result.add((Character) object);
            } else if (object instanceof String) {
                if (((String) object).length() == 1) {
                    result.add(((String) object).charAt(0));
                }
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<String> getStringList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<String>(0);
        }
        
        final List<String> result = new ArrayList<String>();
        for (final Object object : list) {
            if (object != null) {
                result.add(object.toString());
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Map<?, ?>> getMapList(@NotNull final String path) {
        
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Map<?, ?>>(0);
        }
        
        final List<Map<?, ?>> result = new ArrayList<Map<?, ?>>();
        for (final Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }
        
        return result;
    }
    
    /**
     * Gets the default {@link Object} for the given path.
     * 
     * @param path The path to retrieve the default {@link Object} from.
     * @return The default {@link Object}, or {@code null} if the default is
     *         {@code null}.
     */
    @Nullable
    protected Object getDefault(@NotNull final String path) {
        
        final Configuration root = this.getRoot();
        final Configuration defs = root == null ? null : root.getDefaults();
        return defs == null ? null : defs.get(MemorySection.createPath(this, path));
    }
    
    /**
     * Maps the keys of the given child {@link ConfigurationSection}.
     * 
     * @param output The {@link Set} of mapped keys.
     * @param section The child {@link ConfigurationSection}.
     * @param deep If {@code true}, any children of the given
     *             {@link ConfigurationSection} will have their keys mapped. If
     *             {@code false}, only the given
     *             {@link ConfigurationSection ConfigurationSection's} keys will
     *             be mapped.
     */
    protected void mapChildrenKeys(@NotNull final Set<String> output, @NotNull final ConfigurationSection section, final boolean deep) {
        
        if (section instanceof MemorySection) {
            
            final MemorySection memorySection = (MemorySection) section;
            for (final Map.Entry<String, SectionPathData> entry : memorySection.map.entrySet()) {
                output.add(MemorySection.createPath(section, entry.getKey(), this));
                
                if (deep && entry.getValue().getData() instanceof ConfigurationSection) {
                    this.mapChildrenKeys(output, (ConfigurationSection) entry.getValue().getData(), deep);
                }
            }
        } else {
            
            final Set<String> keys = section.getKeys(deep);
            for (final String key : keys) {
                output.add(MemorySection.createPath(section, key, this));
            }
        }
    }
    
    /**
     * Maps the key-value pairs of the given child {@link ConfigurationSection}.
     * 
     * @param output The {@link Map} of key-value pairs.
     * @param section The child {@link ConfigurationSection}.
     * @param deep If {@code true}, any children of the given
     *             {@link ConfigurationSection} will have their key-value pairs
     *             mapped. If {@code false}, only the given
     *             {@link ConfigurationSection ConfigurationSection's} key-value
     *             pairs will be mapped.
     */
    protected void mapChildrenValues(@NotNull final Map<String, Object> output, @NotNull final ConfigurationSection section, final boolean deep) {
        
        if (section instanceof MemorySection) {
            
            final MemorySection memorySection = (MemorySection) section;
            for (final Map.Entry<String, SectionPathData> entry : memorySection.map.entrySet()) {
                
                final String path = MemorySection.createPath(section, entry.getKey(), this);
                output.remove(path);
                output.put(path, entry.getValue().getData());
                
                if (deep && entry.getValue().getData() instanceof ConfigurationSection) {
                    this.mapChildrenValues(output, (ConfigurationSection) entry.getValue().getData(), deep);
                }
            }
        } else {
            
            final Map<String, Object> values = section.getValues(deep);
            for (final Map.Entry<String, Object> entry : values.entrySet()) {
                output.put(MemorySection.createPath(section, entry.getKey(), this), entry.getValue());
            }
        }
    }
    
    /**
     * Creates a {@link String} representation of this {@link MemorySection}.
     * 
     * @return A {@link String} representation of this {@link MemorySection}.
     */
    @Override
    public String toString() {
        
        final Configuration root = this.getRoot();
        final StringBuilder builder = new StringBuilder();
        
        builder.append(getClass().getSimpleName());
        builder.append("[path='");
        builder.append(this.getCurrentPath());
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
     * only this {@link MemorySection}.
     * 
     * @param section The {@link ConfigurationSection} to create a path for.
     * @param key The name of the specified {@link ConfigurationSection}.
     * @return The full path of the {@link ConfigurationSection} from its root.
     */
    @NotNull
    public static String createPath(@NotNull final ConfigurationSection section, @Nullable final String key) {
        return MemorySection.createPath(section, key, section.getRoot());
    }
    
    /**
     * Creates a relative path to the given {@link ConfigurationSection} from
     * the given relative {@link ConfigurationSection}.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not
     * only this {@link MemorySection}.
     * 
     * @param section The {@link ConfigurationSection} to create a path for.
     * @param key The name of the specified section.
     * @param relative The {@link ConfigurationSection} to create the path
     *                 relative to.
     * @return The full path of the {@link ConfigurationSection} from its root.
     */
    @NotNull
    public static String createPath(@NotNull final ConfigurationSection section, @Nullable final String key, @Nullable final ConfigurationSection relative) {
        
        final Configuration root = section.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root.");
        }
        
        final char separator = root.getOptions().getPathSeparator();
        final StringBuilder builder = new StringBuilder();
        
        for (ConfigurationSection parent = section; parent != null && parent != relative; parent = parent.getParent()) {
            
            if (builder.length() > 0) {
                builder.insert(0, separator);
            }
            builder.insert(0, parent.getName());
        }
        
        if (key != null && key.length() > 0) {
            if (builder.length() > 0) {
                builder.append(separator);
            }
            builder.append(key);
        }
        
        return builder.toString();
    }
}
