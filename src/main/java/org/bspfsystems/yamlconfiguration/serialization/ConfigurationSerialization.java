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

package org.bspfsystems.yamlconfiguration.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for storing and retrieving classes for {@link Configuration}.
 * <p>
 * Synchronized with the commit on 07-June-2022.
 */
public final class ConfigurationSerialization {
    
    public static final String SERIALIZED_TYPE_KEY = "==";
    
    private static final Map<String, Class<? extends ConfigurationSerializable>> ALIASES = new HashMap<String, Class<? extends ConfigurationSerializable>>();
    
    private final Class<? extends ConfigurationSerializable> clazz;
    
    /**
     * Constructs a new {@link ConfigurationSerialization} for the given
     * {@link ConfigurationSerializable}.
     * 
     * @param clazz The {@link ConfigurationSerializable} {@link Class}.
     */
    private ConfigurationSerialization(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * Deserializes the given {@link Map} into a
     * {@link ConfigurationSerializable}.
     * 
     * @param map The serialized data as a {@link Map}.
     * @return The deserialized {@link Object}, or {@code null} if the data
     *         cannot be deserialized.
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
     * Gets the {@link Method} of the {@link ConfigurationSerializable} that
     * performs the deserialization. If none can be found, {@code null} will be
     * returned.
     * 
     * @param name The name of the {@link Method} to retrieve.
     * @return The {@link Method} that performs the deserialization, or
     *         {@code null} if none can be found.
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
     * Deserializes the data in the {@link Map} via the given {@link Method}. If
     * any {@link Throwable} is thrown, {@code null} will be returned.
     * 
     * @param method The {@link Method} to use to deserialize the data.
     * @param map The data to deserialize.
     * @return The deserialized {@link ConfigurationSerializable}, or
     *         {@code null} if there is an issue.
     */
    @Nullable
    private ConfigurationSerializable deserializeViaMethod(@NotNull final Method method, @NotNull final Map<String, ?> map) {
        
        try {
            
            final ConfigurationSerializable result = (ConfigurationSerializable) method.invoke(null, map);
            if (result == null) {
                Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization: method returned null.");
            } else {
                return result;
            }
        } catch (Throwable t) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization.", t instanceof InvocationTargetException ? t.getCause() : t);
        }
        
        return null;
    }
    
    /**
     * Gets the {@link Constructor} of the {@link ConfigurationSerializable}
     * that performs the deserialization. If none can be found, {@code null}
     * will be returned.
     * 
     * @return The {@link Constructor} that performs the deserialization, or
     *         {@code null} if none can be found.
     */
    @Nullable
    private Constructor<? extends ConfigurationSerializable> getConstructor() {
        
        try {
            return this.clazz.getConstructor(Map.class);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }
    
    /**
     * Deserializes the data in the {@link Map} via the given
     * {@link Constructor}. If any {@link Throwable} is thrown, {@code null}
     * will be returned.
     * 
     * @param constructor The {@link Constructor} to use to deserialize the
     *                    data.
     * @param map The data as a {@link Map} to deserialize.
     * @return The deserialized {@link ConfigurationSerializable}, or
     *         {@code null} if there is an issue.
     */
    @Nullable
    private ConfigurationSerializable deserializeViaConstructor(@NotNull final Constructor<? extends ConfigurationSerializable> constructor, @NotNull final Map<String, ?> map) {
        
        try {
            return constructor.newInstance(map);
        } catch (Throwable t) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call constructor '" + constructor.toString() + "' of " + clazz + "for deserialization.", t instanceof InvocationTargetException ? t.getCause() : t);
        }
        
        return null;
    }
    
    /**
     * Attempts to deserialize the given {@link Map} into a new instance of the
     * given {@link Class}.
     * <p>
     * The {@link Class} must implement {@link ConfigurationSerializable},
     * including the extra methods as specified in the javadoc of a
     * {@link ConfigurationSerializable}.
     * <p>
     * If a new instance could not be made (an example being the {@link Class}
     * not fully implementing the interface), {@code null} will be returned.
     * 
     * @param map The {@link Map} to deserialize.
     * @param clazz The {@link Class} to deserialize into.
     * @return The new instance of the specified {@link Class}.
     */
    @Nullable
    public static ConfigurationSerializable deserializeObject(@NotNull final Map<String, ?> map, @NotNull final Class<? extends ConfigurationSerializable> clazz) {
        return new ConfigurationSerialization(clazz).deserialize(map);
    }
    
    /**
     * Attempts to deserialize the given arguments into a new instance of the
     * given class.
     * <p>
     * The class must implement {@link ConfigurationSerializable}, including
     * the extra methods as specified in the javadoc of
     * ConfigurationSerializable.
     * <p>
     * If a new instance could not be made, an example being the class not
     * fully implementing the interface, null will be returned.
     * 
     * @param map The data as a {@link Map} to deserialize.
     * @return The {@link ConfigurationSerializable} with the data from the
     *         given {@link Map}.
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
            } catch(ClassCastException e) {
                e.fillInStackTrace();
                throw e;
            }
        } else {
            throw new IllegalArgumentException("The map does not contain type key ('" + ConfigurationSerialization.SERIALIZED_TYPE_KEY + "').");
        }
        
        return new ConfigurationSerialization(clazz).deserialize(map);
    }
    
    /**
     * Registers the given {@link ConfigurationSerializable} {@link Class} by
     * its alias.
     * 
     * @param clazz The {@link Class} to register.
     */
    public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        
        final DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
        if (delegate == null) {
            ConfigurationSerialization.registerClass(clazz, getAlias(clazz));
            ConfigurationSerialization.registerClass(clazz, clazz.getName());
        }
    }
    
    /**
     * Registers the given alias to the specified
     * {@link ConfigurationSerializable} {@link Class}
     * 
     * @param clazz The {@link Class} to register.
     * @param alias Alias to register the {@link Class} as.
     * @see SerializableAs
     */
    public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz, @NotNull final String alias) {
        ConfigurationSerialization.ALIASES.put(alias, clazz);
    }
    
    /**
     * Unregisters the specified alias from a {@link ConfigurationSerializable}.
     *
     * @param alias The alias to unregister.
     */
    public static void unregisterClass(@NotNull final String alias) {
        ConfigurationSerialization.ALIASES.remove(alias);
    }
    
    /**
     * Unregisters any aliases for the specified
     * {@link ConfigurationSerializable} {@link Class}.
     * 
     * @param clazz The {@link Class} to unregister.
     */
    public static void unregisterClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        ConfigurationSerialization.ALIASES.entrySet().removeIf(entry -> entry.getValue().equals(clazz));
    }
    
    /**
     * Attempts to get a registered {@link ConfigurationSerializable}
     * {@link Class} by its alias.
     * 
     * @param alias The alias of the {@link ConfigurationSerializable} to
     *              retrieve.
     * @return The requested {@link ConfigurationSerializable}, or {@code null}
     *         if one is not found.
     */
    @Nullable
    public static Class<? extends ConfigurationSerializable> getClassByAlias(@NotNull final String alias) {
        return ConfigurationSerialization.ALIASES.get(alias);
    }
    
    /**
     * Gets the primary alias for the given {@link ConfigurationSerializable}
     * {@link Class}.
     * 
     * @param clazz The {@link Class} to retrieve the alias of.
     * @return The requested primary alias of the given
     *         {@link ConfigurationSerializable}.
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
