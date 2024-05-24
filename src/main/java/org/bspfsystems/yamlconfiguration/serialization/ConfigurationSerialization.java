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

package org.bspfsystems.yamlconfiguration.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

/**
 * A utility class for storing and retrieving classes for configurations.
 * <p>
 * Synchronized with the commit on 19-November-2023.
 */
public final class ConfigurationSerialization {
    
    public static final String SERIALIZED_TYPE_KEY = "==";
    
    private static final Map<String, Class<? extends ConfigurationSerializable>> ALIASES = new HashMap<String, Class<? extends ConfigurationSerializable>>();
    
    private final Class<? extends ConfigurationSerializable> clazz;
    
    /**
     * Constructs a configuration serialization for the given configuration
     * serializable.
     * 
     * @param clazz The class of the configuration serializable.
     */
    private ConfigurationSerialization(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * Deserializes the given map into a configuration serializable.
     * 
     * @param map The serialized data as a map.
     * @return The deserialized configuration serializable, or {@code null} if
     *         the data cannot be deserialized.
     */
    @Nullable
    private ConfigurationSerializable deserialize(@NotNull final Map<String, ?> map) {
        
        ConfigurationSerializable result = null;
        
        Method method = this.getMethod("deserialize");
        if (method != null) {
            result = this.deserializeViaMethod(method, map);
        }
        
        if (result == null) {
            method = this.getMethod("valueOf");
            if (method != null) {
                result = this.deserializeViaMethod(method, map);
            }
        }
        
        if (result == null) {
            final Constructor<? extends ConfigurationSerializable> constructor = this.getConstructor();
            if (constructor != null) {
                result = this.deserializeViaConstructor(constructor, map);
            }
        }
        
        return result;
    }
    
    /**
     * Gets the method of the configuration serializable that performs the
     * deserialization. If one cannot be found, {@code null} will be returned.
     * 
     * @param name The name of the method to retrieve.
     * @return The method that performs the deserialization, or {@code null} if
     *         one cannot be found.
     */
    @Nullable
    private Method getMethod(@NotNull final String name) {
        
        try {
            final Method method = this.clazz.getDeclaredMethod(name, Map.class);
            
            if (!ConfigurationSerializable.class.isAssignableFrom(method.getReturnType())) {
                return null;
            }
            if (!Modifier.isStatic(method.getModifiers())) {
                return null;
            }
            
            return method;
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }
    
    /**
     * Deserializes the data in the given map via the given method. If any
     * throwable is thrown, {@code null} will be returned.
     * 
     * @param method The method to use to deserialize the data.
     * @param map The data to deserialize.
     * @return The deserialized configuration serializable, or {@code null} if
     *         an issue occurs during deserialization.
     */
    @Nullable
    private ConfigurationSerializable deserializeViaMethod(@NotNull final Method method, @NotNull final Map<String, ?> map) {
        
        try {
            
            final ConfigurationSerializable result = (ConfigurationSerializable) method.invoke(null, map);
            if (result == null) {
                LoggerFactory.getLogger(ConfigurationSerialization.class).error("Could not call method '" + method.toString() + "' of " + this.clazz.getName() + " for deserialization: method returned null.");
            } else {
                return result;
            }
        } catch (final Throwable t) {
            LoggerFactory.getLogger(ConfigurationSerialization.class).error("Could not call method '" + method.toString() + "' of " + this.clazz.getName() + " for deserialization.", t instanceof InvocationTargetException ? t.getCause() : t);
        }
        
        return null;
    }
    
    /**
     * Gets the constructor of the configuration serializable that performs the
     * deserialization. If one cannot be found, {@code null} will be returned.
     * 
     * @return The constructor that performs the deserialization, or
     *         {@code null} if one cannot be found.
     */
    @Nullable
    private Constructor<? extends ConfigurationSerializable> getConstructor() {
        
        try {
            return this.clazz.getConstructor(Map.class);
        } catch (final NoSuchMethodException | SecurityException e) {
            return null;
        }
    }
    
    /**
     * Deserializes the data in the given map via the given constructor. If any
     * throwable is thrown, {@code null} will be returned.
     * 
     * @param constructor The constructor to use to deserialize the data.
     *                    data.
     * @param map The data to deserialize.
     * @return The deserialized configuration serializable, or {@code null} if
     *         an issue occurs during deserialization.
     */
    @Nullable
    private ConfigurationSerializable deserializeViaConstructor(@NotNull final Constructor<? extends ConfigurationSerializable> constructor, @NotNull final Map<String, ?> map) {
        
        try {
            return constructor.newInstance(map);
        } catch (final Throwable t) {
            LoggerFactory.getLogger(ConfigurationSerialization.class.getName()).error("Could not call constructor '" + constructor.toString() + "' of " + clazz + "for deserialization.", t instanceof InvocationTargetException ? t.getCause() : t);
        }
        
        return null;
    }
    
    /**
     * Attempts to deserialize the given map into a new instance of the given
     * class.
     * <p>
     * The class must implement configuration serializable, including the extra
     * methods and/or constructor as specified in the javadocs of a
     * configuration serializable.
     * <p>
     * If a new instance could not be made (an example being the class not fully
     * implementing the interface), {@code null} will be returned.
     * 
     * @param map The map to deserialize.
     * @param clazz The class to deserialize into.
     * @return The new instance of the specified class.
     */
    @Nullable
    public static ConfigurationSerializable deserializeObject(@NotNull final Map<String, ?> map, @NotNull final Class<? extends ConfigurationSerializable> clazz) {
        return new ConfigurationSerialization(clazz).deserialize(map);
    }
    
    /**
     * Attempts to deserialize the given map into a new instance of any known
     * type of configuration serializable that may be indicated by the data
     * contained within the map itself.
     * <p>
     * The class must implement configuration serializable, including the extra
     * methods and/or constructor as specified in the javadocs of a
     * configuration serializable.
     * <p>
     * If a new instance could not be made (an example being the class not fully
     * implementing the interface), {@code null} will be returned.
     * 
     * @param map The map to deserialize.
     * @return The new instance of the given data.
     */
    @Nullable
    public static ConfigurationSerializable deserializeObject(@NotNull final Map<String, ?> map) {
        
        Class<? extends ConfigurationSerializable> clazz = null;
        if (map.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
            
            try {
                final String alias = (String) map.get(ConfigurationSerialization.SERIALIZED_TYPE_KEY);
                if (alias == null) {
                    throw new IllegalArgumentException("Cannot have null alias.");
                }
                clazz = ConfigurationSerialization.getClassByAlias(alias);
                if (clazz == null) {
                    throw new IllegalArgumentException("The specified class does not exist ('" + alias + "').");
                }
            } catch (final ClassCastException e) {
                e.fillInStackTrace();
                throw e;
            }
        } else {
            throw new IllegalArgumentException("The map does not contain type key ('" + ConfigurationSerialization.SERIALIZED_TYPE_KEY + "').");
        }
        
        return new ConfigurationSerialization(clazz).deserialize(map);
    }
    
    /**
     * Registers the given configuration serializable class by any and all
     * aliases/delegate deserializations.
     * 
     * @param clazz The class to register.
     */
    public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        
        final DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
        if (delegate == null) {
            ConfigurationSerialization.registerClass(clazz, getAlias(clazz));
            ConfigurationSerialization.registerClass(clazz, clazz.getName());
        }
    }
    
    /**
     * Registers the given alias to the given configuration serializable class.
     * 
     * @param clazz The class to register.
     * @param alias Alias to register the class as.
     * @see SerializableAs
     */
    public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz, @NotNull final String alias) {
        ConfigurationSerialization.ALIASES.put(alias, clazz);
    }
    
    /**
     * Unregisters the specified alias.
     *
     * @param alias The alias to unregister.
     */
    public static void unregisterClass(@NotNull final String alias) {
        ConfigurationSerialization.ALIASES.remove(alias);
    }
    
    /**
     * Unregisters any aliases for the given configuration serializable class
     * 
     * @param clazz The class to unregister.
     */
    public static void unregisterClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        ConfigurationSerialization.ALIASES.entrySet().removeIf(entry -> entry.getValue().equals(clazz));
    }
    
    /**
     * Attempts to get a registered configuration serializable class by its
     * alias.
     * 
     * @param alias The alias of the configuration serializable to retrieve.
     * @return The requested configuration serializable, or {@code null} if one
     *         is not found.
     */
    @Nullable
    public static Class<? extends ConfigurationSerializable> getClassByAlias(@NotNull final String alias) {
        return ConfigurationSerialization.ALIASES.get(alias);
    }
    
    /**
     * Gets the primary alias for the given configuration serializable class.
     * 
     * @param clazz The class to retrieve the primary alias of.
     * @return The primary alias of the given configuration serializable.
     */
    @NotNull
    public static String getAlias(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
        if (delegate != null && delegate.value() != clazz) {
            return ConfigurationSerialization.getAlias(delegate.value());
        }
        
        final SerializableAs alias = clazz.getAnnotation(SerializableAs.class);
        if (alias != null) {
            return alias.value();
        }
        
        return clazz.getName();
    }
}
