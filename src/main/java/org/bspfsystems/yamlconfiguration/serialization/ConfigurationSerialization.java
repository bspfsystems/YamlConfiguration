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

package org.bspfsystems.yamlconfiguration.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for storing and retrieving classes for {@link Configuration}.
 */
public final class ConfigurationSerialization {
	
	public static final String SERIALIZED_TYPE_KEY = "==";
	
	private static Map<String, Class<? extends ConfigurationSerializable>> aliases;
	
	private final Class<? extends ConfigurationSerializable> clazz;
	
	protected ConfigurationSerialization(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
		this.clazz = clazz;
		aliases = new HashMap<String, Class<? extends ConfigurationSerializable>>();
	}
	
	@Nullable
	public ConfigurationSerializable deserialize(@NotNull final Map<String, ?> args) {
		
		if(args == null) {
			throw new IllegalArgumentException("Args must not be null.");
		}
		
		ConfigurationSerializable result = null;
		Method method = null;
		
		if(result == null) {
			method = getMethod("deserialize", true);
			if(method != null) {
				result = deserializeViaMethod(method, args);
			}
		}
		
		if(result == null) {
			method = getMethod("valueOf", true);
			if(method != null) {
				result = deserializeViaMethod(method, args);
			}
		}
		
		if(result == null) {
			final Constructor<? extends ConfigurationSerializable> constructor = getConstructor();
			if(constructor != null) {
				result = deserializeViaConstructor(constructor, args);
			}
		}
		
		return result;
	}
	
	@Nullable
	protected Constructor<? extends ConfigurationSerializable> getConstructor() {
		
		try {
			return clazz.getConstructor(Map.class);
		}
		catch(NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
	@Nullable
	protected Method getMethod(@NotNull final String name, final boolean isStatic) {
		
		try {
			final Method method = clazz.getDeclaredMethod(name, Map.class);
			
			if(!ConfigurationSerializable.class.isAssignableFrom(method.getReturnType())) {
				return null;
			}
			if(Modifier.isStatic(method.getModifiers()) != isStatic) {
				return null;
			}
			
			return method;
		}
		catch(NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
	@Nullable
	protected ConfigurationSerializable deserializeViaConstructor(@NotNull Constructor<? extends ConfigurationSerializable> constructor, @NotNull Map<String, ?> args) {
		
		try {
			return constructor.newInstance(args);
		}
		catch(Throwable t) {
			Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call constructor '" + constructor.toString() + "' of " + clazz + "for deserialization.", t instanceof InvocationTargetException ? t.getCause() : t);
		}
		
		return null;
	}
	
	@Nullable
	protected ConfigurationSerializable deserializeViaMethod(@NotNull final Method method, @NotNull final Map<String, ?> args) {
		
		try {
			
			final ConfigurationSerializable result = (ConfigurationSerializable) method.invoke(null, args);
			if(result == null) {
				Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization: method returned null.");
			}
			else {
				return result;
			}
		}
		catch(Throwable t) {
			Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization.", t instanceof InvocationTargetException ? t.getCause() : t);
		}
		
		return null;
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
     * @param args Arguments for deserialization
     * @param clazz Class to deserialize into
     * @return New instance of the specified class
     */
	@Nullable
	public static ConfigurationSerializable deserializeObject(@NotNull final Map<String, ?> args, @NotNull final Class<? extends ConfigurationSerializable> clazz) {
		return new ConfigurationSerialization(clazz).deserialize(args);
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
     * @param args Arguments for deserialization
     * @return New instance of the specified class
     */
	@Nullable
	public static ConfigurationSerializable deserializeObject(@NotNull final Map<String, ?> args) {
		
		Class<? extends ConfigurationSerializable> clazz = null;
		if(args.containsKey(SERIALIZED_TYPE_KEY)) {
			
			try {
				final String alias = (String) args.get(SERIALIZED_TYPE_KEY);
				if(alias == null) {
					throw new IllegalArgumentException("Cannot have null alias.");
				}
				clazz = getClassByAlias(alias);
				if(clazz == null) {
					throw new IllegalArgumentException("Specified class does not exist ('" + alias + "').");
				}
			}
			catch(ClassCastException e) {
				e.fillInStackTrace();
				throw e;
			}
		}
		else {
			throw new IllegalArgumentException("Args doesn't contain type key ('" + SERIALIZED_TYPE_KEY + "').");
		}
		
		return new ConfigurationSerialization(clazz).deserialize(args);
	}
	
	/**
     * Registers the given {@link ConfigurationSerializable} class by its
     * alias
     *
     * @param clazz Class to register
     */
	public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
		
		final DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
		if(delegate == null) {
			registerClass(clazz, getAlias(clazz));
			registerClass(clazz, clazz.getName());
		}
	}
	
	/**
     * Registers the given alias to the specified {@link
     * ConfigurationSerializable} class
     *
     * @param clazz Class to register
     * @param alias Alias to register as
     * @see SerializableAs
     */
	public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz, @NotNull String alias) {
		aliases.put(alias, clazz);
	}
	
	/**
     * Unregisters the specified alias to a {@link ConfigurationSerializable}
     *
     * @param alias Alias to unregister
     */
	public static void unregisterClass(@NotNull final String alias) {
		aliases.remove(alias);
	}
	
	/**
     * Unregisters any aliases for the specified {@link
     * ConfigurationSerializable} class
     *
     * @param clazz Class to unregister
     */
	public static void unregisterClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
		while(aliases.values().remove(clazz)) {
			// Run the while loop.
		}
	}
	
	/**
     * Attempts to get a registered {@link ConfigurationSerializable} class by
     * its alias
     *
     * @param alias Alias of the serializable
     * @return Registered class, or null if not found
     */
	@Nullable
	public static Class<? extends ConfigurationSerializable> getClassByAlias(@NotNull final String alias) {
		return aliases.get(alias);
	}
	
	/**
     * Gets the correct alias for the given {@link ConfigurationSerializable}
     * class
     *
     * @param clazz Class to get alias for
     * @return Alias to use for the class
     */
	@NotNull
	public static String getAlias(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
		
		DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
		if(delegate != null) {
			if(delegate.value() == null || delegate.value() == clazz) {
				delegate = null;
			}
			else {
				return getAlias(delegate.value());
			}
		}
		
		if(delegate == null) {
			final SerializableAs alias = clazz.getAnnotation(SerializableAs.class);
			if(alias != null && alias.value() != null) {
				return alias.value();
			}
		}
		
		return clazz.getName();
	}
}
