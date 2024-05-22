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

package org.bspfsystems.yamlconfiguration.configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Represents a section of a configuration.
 * <p>
 * Synchronized with the commit on 20-December-2021.
 */
public interface ConfigurationSection {
    
    /**
     * Gets a set containing a copy of all keys in this configuration section.
     * <p>
     * If {@code deep} is set to {@code true}, then the set will contain all
     * keys within this configuration section as well as the keys of any
     * children sections of this section, working recursively down through each
     * child's potential children. As a result, all the keys will be the
     * fully-qualified paths.
     * <p>
     * If {@code deep} is set to {@code false}, then the set will contain only
     * the keys of any values of this configuration section, even if those
     * values themselves are children configuration sections.
     * <p>
     * In either case, the set will be unmodifiable, and any updates to the keys
     * of this configuration section will not be reflected in the returned set.
     * 
     * @param deep {@code true} if the set is to contain the keys of the
     *             children configuration section(s) of this section
     *             (recursive), {@code false} otherwise.
     * @return A copy of the set of keys contained within this configuration
     *         section.
     */
    @NotNull
    @UnmodifiableView
    Set<String> getKeys(final boolean deep);
    
    /**
     * Gets a map containing a copy of all keys and their respective values for
     * this configuration section.
     * <p>
     * If {@code deep} is set to {@code true}, then the map will contain all
     * key-value pairings within this configuration section, as well as the
     * key-value pairings of any children sections of this section, working
     * recursively down through each child's potential children. As a result,
     * all the keys will be the fully-qualified paths.
     * <p>
     * If {@code deep} is set to {@code false}, then the map will contain only
     * the key-value pairings of this configuration section, even if those
     * values themselves are children configuration sections.
     * <p>
     * In either case, the map will be unmodifiable, and any updates to the
     * keys, values, or the pairings of this configuration section will not be
     * reflected in the returned set.
     * 
     * @param deep {@code true} if the map is to contain the key-value pairings
     *             of the children configuration section(s) of this section
     *             (recursive), {@code false} otherwise.
     * @return A copy of the map of key-value pairings contained within this
     *         configuration section.
     */
    @NotNull
    @UnmodifiableView
    Map<String, Object> getValues(final boolean deep);
    
    /**
     * Gets the path of this configuration section from its root configuration.
     * <p>
     * If this configuration section is a configuration itself, this will return
     * an empty string.
     * <p>
     * If this configuration section is no longer contained within a root
     * configuration for any reason, such as being replaced with a different
     * value, this may return {@code null}.
     * <p>
     * This will return the fully-qualified path of this section. If only the
     * final part of the path of this section is desired, please use
     * {@link ConfigurationSection#getName()}.
     * 
     * @return The fully-qualified path of this configuration section relative
     *         to its root configuration, or {@code null} if this section is
     *         orphaned.
     */
    @Nullable
    String getCurrentPath();
    
    /**
     * Gets the name of this individual configuration section in its path.
     * <p>
     * This will always be the final part of
     * {@link ConfigurationSection#getCurrentPath()}, unless this configuration
     * section is orphaned.
     * 
     * @return The name of this configuration section.
     */
    @NotNull
    String getName();
    
    /**
     * Gets the root configuration that contains this configuration section.
     * <p>
     * If this configuration section is itself a configuration, this will return
     * this section.
     * <p>
     * If this configuration section is no longer contained within a root
     * configuration for any reason, such as being replaced with a different
     * value, this may return {@code null}.
     * 
     * @return The root configuration containing this configuration section, or
     *         {@code null} if this section is orphaned.
     */
    @Nullable
    Configuration getRoot();
    
    /**
     * Gets the parent configuration section that directly contains this
     * section.
     * <p>
     * If this configuration section is itself a configuration, this will return
     * {@code null}.
     * <p>
     * If this configuration section is no longer contained within a root
     * configuration for any reason, such as being replaced with a different
     * value, this may return {@code null}.
     * 
     * @return The parent configuration section containing this section, or
     *         {@code null} if this section does not have a parent.
     */
    @Nullable
    ConfigurationSection getParent();
    
    /**
     * Gets the equivalent configuration section from the default configuration
     * defined in {@link ConfigurationSection#getRoot()}.
     * <p>
     * If the root configuration contains no defaults, or the defaults do not
     * contain a value for this configuration section's path, or the value at
     * this path is not itself a configuration section, this will return
     * {@code null}.
     * 
     * @return The equivalent configuration section in the default
     *         configuration, or {@code null} if one does not exist or is not
     *         the correct type.
     */
    @Nullable
    ConfigurationSection getDefaultSection();
    
    /**
     * Sets the default value in the root at the given path as provided.
     * <p>
     * If no source configuration was provided as a default collection, then a
     * new memory configuration will be created to hold the given default value.
     * <p>
     * If value is {@code null}, the value will be removed from the default
     * configuration source.
     * <p>
     * If the value as returned by
     * {@link ConfigurationSection#getDefaultSection()} is {@code null}, then
     * this will create a new configuration section at the path, replacing
     * anything that may have existed there previously.
     * 
     * @param path The path of the default value to set.
     * @param value The value to set the default to.
     */
    void addDefault(@NotNull final String path, @Nullable final Object value);
    
    /**
     * Gets the comments by path.
     * <p>
     * If no comments exist, and empty list will be returned. A {@code null}
     * entry represents an empty line and an empty string represents an empty
     * comment line ({@code #} with nothing after it).
     * <p>
     * The returned list will be unmodifiable, and any changes to the underlying
     * comments in this configuration section will not be reflected in the
     * returned list.
     * 
     * @param path The path of the comments to get.
     * @return The comments as a list, where every entry represents a separate
     *         line.
     */
    @NotNull
    @UnmodifiableView
    List<String> getComments(@NotNull final String path);
    
    /**
     * Gets the inline comments by path.
     * <p>
     * If no comments exist, an empty list will be returned. A {@code null}
     * entry represents an empty line and an empty string represents an empty
     * comment line ({@code #} with nothing after it).
     * <p>
     * The returned list will be unmodifiable, and any changes to the underlying
     * inline comments in this configuration section will not be reflected in
     * the returned list.
     * 
     * @param path The path of the comments to get.
     * @return The inline comments as a list, where every entry represents a
     *         separate line.
     */
    @NotNull
    @UnmodifiableView
    List<String> getInLineComments(@NotNull final String path);
    
    /**
     * Sets the comments at the given path.
     * <p>
     * If the given list is {@code null} itself, any existing comments will be
     * removed. Otherwise, the existing comments will be replaced with the
     * contents of the given list, regardless of the previous comment values.
     * <p>
     * A {@code null} entry in the list will result in an empty line, and an
     * empty string as an entry will result in an empty comment line ({@code #}
     * with nothing after it).
     * 
     * @param path The path of the comments to set.
     * @param comments The new comments to set at the given path, where every
     *                 entry in the list represents one line. {@code null} to
     *                 remove any existing comments.
     */
     void setComments(@NotNull final String path, @Nullable final List<String> comments);
    
    /**
     * Sets the inline comments at the given path.
     * <p>
     * If the given list is {@code null} itself, any existing inline comments
     * will be removed. Otherwise, the existing inline comments will be replaced
     * with the contents of the given list, regardless of the previous comment
     * values.
     * <p>
     * A {@code null} entry in the list will result in an empty line, and an
     * empty string as an entry will result in an empty comment line ({@code #}
     * with nothing after it).
     * 
     * @param path The path of the inline comments to set.
     * @param inLineComments The new inline comments to set at the given path,
     *                       where every entry in the list represents one line.
     *                       {@code null} to remove any existing inline
     *                       comments.
     */
    void setInLineComments(@NotNull final String path, @Nullable final List<String> inLineComments);
    
    /**
     * Creates an empty configuration section at the given path.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was a configuration section itself, that section will
     * be orphaned.
     *
     * @param path The path to create the configuration section at.
     * @return The newly-created configuration section.
     */
    @NotNull
    ConfigurationSection createSection(@NotNull final String path);
    
    /**
     * Creates a configuration section at the given path, with the given values.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was a configuration section itself, that section will
     * be orphaned.
     * 
     * @param path The path to create the configuration section at.
     * @param entries The key-value pairings to place in the newly-created
     *                configuration section.
     * @return The newly-created configuration section.
     */
    @NotNull
    ConfigurationSection createSection(@NotNull final String path, @NotNull final Map<?, ?> entries);
    
    /**
     * Checks if this configuration section has a value set for the given path.
     * <p>
     * If the value for the given path does not exist but a default value has
     * been set, this will still return {@code false}. If the status of a
     * default value is desired, please use
     * {@link ConfigurationSection#contains(String)}.
     * 
     * @param path The path to check for existence.
     * @return {@code true} if this configuration section contains the given
     *         path, regardless of having a default, {@code false} otherwise.
     * @see ConfigurationSection#contains(String)
     */
    boolean isSet(@NotNull final String path);
    
    /**
     * Sets the given path to the given value. Any existing value will be
     * replaced, regardless of what it and the new given value are.
     * <p>
     * If the given value is {@code null}, the entry will be removed.
     * <p>
     * The given value may not be a configuration or a configuration section. If
     * a new configuration section is desired, please use
     * {@link ConfigurationSection#createSection(String)}.
     * 
     * @param path The path to the given value.
     * @param value The new value to store at the given path.
     */
    void set(@NotNull final String path, @Nullable final Object value);
    
    /**
     * Checks if this configuration section contains the given path.
     * <p>
     * If the value for the given path does not exist but a default value has
     * been set, this will return {@code true}. If the existence of a default
     * value is to be ignored, please use
     * {@link ConfigurationSection#contains(String, boolean)}, where
     * {@code ignoreDefault} is {@code true}.
     * 
     * @param path The path to check for existence.
     * @return {@code true} if this configuration section contains the given
     *         path, either via an actual value or default value being set,
     *         {@code false} otherwise.
     * @see ConfigurationSection#contains(String, boolean)
     */
    boolean contains(@NotNull final String path);
    
    /**
     * Checks if this configuration section contains the given path.
     * <p>
     * If {@code true} is given for {@code ignoreDefault}, then this will
     * return {@code true} only if a value has been set for the given path.
     * <p>
     * If {@code false} is given for {@code ignoreDefaults}, then this will
     * return {@code true} if a value has been set for the given path, or if a
     * default value has been set for the given path.
     * 
     * @param path The path to check for existence.
     * @param ignoreDefault {@code true} if any default values should be ignored
     *                      when checking for a value at the path, {@code false}
     *                      otherwise.
     * @return {@code true} if this configuration section contains an actual
     *         value at the given path, or if a default value has been set
     *         and {@code ignoreDefault} is {@code false}, {@code false}
     *         otherwise.
     */
    boolean contains(@NotNull final String path, final boolean ignoreDefault);
    
    /**
     * Gets the value at the given path.
     * <p>
     * If a value has not been set at the given path, but a default value has
     * been set, this will return the default value. If no value and no default
     * value have been set, this will return {@code null}.
     * 
     * @param path The path of the value to retrieve.
     * @return The requested value.
     */
    @Nullable
    Object get(@NotNull final String path);
    
    /**
     * Gets the value at the given path, returning the given default value if
     * one has not been set.
     * <p>
     * If a value has not been set at the given path, the given default value
     * will be returned, regardless of whether a default value exists in the
     * root configuration.
     * 
     * @param path The path of the value to retrieve.
     * @param def The value to use as a default.
     * @return The requested value.
     */
    @Contract("_, !null -> !null")
    @Nullable
    Object get(@NotNull final String path, @Nullable final Object def);
    
    /**
     * Checks if the value at the given path is a boolean.
     * <p>
     * If the value exists at the given path but is not a boolean, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check the default value and return appropriately.
     * 
     * @param path The path of the boolean to check.
     * @return {@code true} if a boolean value exists, or if there is no value,
     *         a default has been set, and it is a boolean, {@code false}
     *         otherwise.
     */
    boolean isBoolean(@NotNull final String path);
    
    /**
     * Gets the boolean value at the given path.
     * <p>
     * If the value exists at the given path but is not a boolean, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the boolean to retrieve.
     * @return The requested boolean.
     */
    boolean getBoolean(@NotNull final String path);
    
    /**
     * Gets the boolean value at the given path.
     * <p>
     * If the value exists at the given path but is not a boolean, or if the
     * value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the boolean to retrieve.
     * @param def The boolean to use as the default.
     * @return The requested boolean.
     */
    boolean getBoolean(@NotNull final String path, final boolean def);
    
    /**
     * Checks if the value at the given path is a number.
     * <p>
     * If the value exists at the given path but is not a number, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the give path but a default value has been
     * set, this will check that value and return appropriately.
     * <p>
     * This method is meant to be a more generic check than the specific
     * number-based checks below, such as
     * {@link ConfigurationSection#isByte(String)}, or
     * {@link ConfigurationSection#isDouble(String)}. Those methods check
     * to see if the requested value (or default) is of the exact type, whereas
     * this method will simply check for a number.
     * 
     * @param path The path of the number to check.
     * @return {@code true} if a number value exists, or if there is no value,
     *         a default has been set, and it is a number, {@code false}
     *         otherwise.
     * @see ConfigurationSection#isByte(String)
     * @see ConfigurationSection#isShort(String)
     * @see ConfigurationSection#isInt(String)
     * @see ConfigurationSection#isLong(String)
     * @see ConfigurationSection#isFloat(String)
     * @see ConfigurationSection#isDouble(String)
     */
    boolean isNumber(@NotNull final String path);
    
    /**
     * Checks if the value at the given path is a byte.
     * <p>
     * If the value exists at the given path but is not a byte, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * <p>
     * This method will check to see if the requested value (or default) is
     * specifically a byte. If the value is another type of number, this will
     * return {@code false}. To check for a generic number please use
     * {@link ConfigurationSection#isNumber(String)}.
     * 
     * @param path The path of the byte to check.
     * @return {@code true} if a byte value exists, or if there is no value, a
     *         default has been set, and it is a byte, {@code false} otherwise.
     * @see ConfigurationSection#isNumber(String)
     */
    boolean isByte(@NotNull final String path);
    
    /**
     * Gets the byte value at the given path.
     * <p>
     * If the value exists at the given path but is not a byte, this will return
     * {@code (byte) 0}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the byte to retrieve.
     * @return The requested byte.
     */
    byte getByte(@NotNull final String path);
    
    /**
     * Gets the byte value at the given path.
     * <p>
     * If the value exists at the given path but is not a byte, or if the value
     * does not exist at the given path, this will return the given default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the byte to retrieve.
     * @param def The byte to use as the default.
     * @return The requested byte.
     */
    byte getByte(@NotNull final String path, final byte def);
    
    /**
     * Checks if the value at the given path is a short.
     * <p>
     * If the value exists at the given path but is not a short, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * <p>
     * This method will check to see if the requested value (or default) is
     * specifically a short. If the value is another type of number, this will
     * return {@code false}. To check for a generic number, please use
     * {@link ConfigurationSection#isNumber(String)}.
     * 
     * @param path The path of the short to check.
     * @return {@code true} if a short value exists, or if there is no value, a
     *         default has been set, and it is a short, {@code false} otherwise.
     * @see ConfigurationSection#isNumber(String)
     */
    boolean isShort(@NotNull final String path);
    
    /**
     * Gets the short value at the given path.
     * <p>
     * If the value exists at the given path but is not a short, this will
     * return {@code (short) 0}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the short to retrieve.
     * @return The requested short.
     */
    short getShort(@NotNull final String path);
    
    /**
     * Gets the short value at the given path.
     * <p>
     * If the value exists at the given path but is not a short, or if the
     * value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the short to retrieve.
     * @param def The short to use as the default.
     * @return The requested short.
     */
    short getShort(@NotNull final String path, final short def);
    
    /**
     * Checks if the value at the given path is an int.
     * <p>
     * If the value exists at the given path but is not an int, this will return
     * {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * <p>
     * This method will check to see if the requested value (or default) is
     * specifically an int. If the value is another type of number, this will
     * return {@code false}. To check for a generic number, please use
     * {@link ConfigurationSection#isNumber(String)}.
     * 
     * @param path The path of the int to check.
     * @return {@code true} if an int value exists, or if there is no value, a
     *         default has been set, and it is an int, {@code false} otherwise.
     * @see ConfigurationSection#isNumber(String)
     */
    boolean isInt(@NotNull final String path);
    
    /**
     * Gets the int value at the given path.
     * <p>
     * If the value exists at the given path but is not an int, this will return
     * {@code 0}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the int to retrieve.
     * @return The requested int.
     */
    int getInt(@NotNull final String path);
    
    /**
     * Gets the int value at the given path.
     * <p>
     * If the value exists at the given path but is not an int, or if the value
     * does not exist at the given path, this will return the given default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the int to retrieve.
     * @param def The int to use as the default.
     * @return The requested int.
     */
    int getInt(@NotNull final String path, final int def);
    
    /**
     * Checks if the value at the given path is a long.
     * <p>
     * If the value exists at the given path but is not a long, this will return
     * {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * <p>
     * This method will check to see if the requested value (or default) is
     * specifically a long. If the value is another type of number, this will
     * return {@code false}. To check for a generic number, please use
     * {@link ConfigurationSection#isNumber(String)}.
     * 
     * @param path The path of the long to check.
     * @return {@code true} if a long value exists, or if there is no value, a
     *         default has been set, and it is a long, {@code false} otherwise.
     * @see ConfigurationSection#isNumber(String)
     */
    boolean isLong(@NotNull final String path);
    
    /**
     * Gets the long value at the given path.
     * <p>
     * If the value exists at the given path but is not a long, this will return
     * {@code 0L}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the long to retrieve.
     * @return The requested long.
     */
    long getLong(@NotNull final String path);
    
    /**
     * Gets the long value at the given path.
     * <p>
     * If the value exists at the given path but is not a long, or if the value
     * does not exist at the given path, this will return the given default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the long to retrieve.
     * @param def The long to use as the default.
     * @return The requested long.
     */
    long getLong(@NotNull final String path, final long def);
    
    /**
     * Checks if the value at the given path is a float.
     * <p>
     * If the value exists at the given path but is not a float, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * <p>
     * This method will check to see if the requested value (or default) is
     * specifically a float. If the value is another type of number, this will
     * return {@code false}. To check for a generic number, please use
     * {@link ConfigurationSection#isNumber(String)}.
     * 
     * @param path The path of the float to check.
     * @return {@code true} if a float value exists, or if there is no value, a
     *         default has been set, and it is a float, {@code false} otherwise.
     * @see ConfigurationSection#isNumber(String)
     */
    boolean isFloat(@NotNull final String path);
    
    /**
     * Gets the float value at the given path.
     * <p>
     * If the value exists at the given path but is not a float, this will
     * return {@code 0.0F}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the float to retrieve.
     * @return The requested float.
     */
    float getFloat(@NotNull final String path);
    
    /**
     * Gets the float value at the given path.
     * <p>
     * If the value exists at the given path but is not a float, or if the value
     * does not exist at the given path, this will return the given default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the float to retrieve.
     * @param def The float to use as the default.
     * @return The requested float.
     */
    float getFloat(@NotNull final String path, final float def);
    
    /**
     * Checks if the value at the given path is a double.
     * <p>
     * If the value exists at the given path but is not a double, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * <p>
     * This method will check to see if the requested value (or default) is
     * specifically a double. If the value is another type of number, this will
     * return {@code false}. To check for a generic number, please use
     * {@link ConfigurationSection#isNumber(String)}.
     * 
     * @param path The path of the double to check.
     * @return {@code true} if a double value exists, or if there is no value, a
     *         default has been set, and it is a double, {@code false}
     *         otherwise.
     * @see ConfigurationSection#isNumber(String)
     */
    boolean isDouble(@NotNull final String path);
    
    /**
     * Gets the double value at the given path.
     * <p>
     * If the value exists at the given path but is not a double, this will
     * return {@code 0.0D}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the double to retrieve.
     * @return The requested double.
     */
    double getDouble(@NotNull final String path);
    
    /**
     * Gets the double value at the given path.
     * <p>
     * If the value exists at the given path but is not a double, or if the
     * value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the double to retrieve.
     * @param def The double to use as the default.
     * @return The requested double.
     */
    double getDouble(@NotNull final String path, final double def);
    
    /**
     * Checks if the value at the given path is a char.
     * <p>
     * If the value exists at the given path but is not a char, this will return
     * {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the char to check.
     * @return {@code true} if a char value exists, or if there is no value, a
     *         default has been set, and it is a char, {@code false} otherwise.
     */
    boolean isChar(@NotNull final String path);
    
    /**
     * Gets the char value at the given path.
     * <p>
     * If the value exists at the given path but is not a char, this will return
     * {@code \u0000}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the char to retrieve.
     * @return The requested char.
     */
    char getChar(@NotNull final String path);
    
    /**
     * Gets the char value at the given path.
     * <p>
     * If the value exists at the given path but is not a char, or if the value
     * does not exist at the given path, this will return the given default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the char to retrieve.
     * @param def The char to use as the default.
     * @return The requested char.
     */
    char getChar(@NotNull final String path, final char def);
    
    /**
     * Checks if the value at the given path is a string.
     * <p>
     * If the value exists at the given path but is not a string, this will
     * return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the string to check.
     * @return {@code true} if a string value exists, or if there is no value, a
     *         default has been set, and it is a string, {@code false} otherwise.
     */
    boolean isString(@NotNull final String path);
    
    /**
     * Gets the string value at the given path.
     * <p>
     * If the value exists at the given path but is not a string, this will
     * return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the string to retrieve.
     * @return The requested string.
     */
    @Nullable
    String getString(@NotNull final String path);
    
    /**
     * Gets the string value at the given path.
     * <p>
     * If the value exists at the given path but is not a string, or if the
     * value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the string to retrieve.
     * @param def The string to use as the default.
     * @return The requested string.
     */
    @Contract("_, !null -> !null")
    @Nullable
    String getString(@NotNull final String path, @Nullable final String def);
    
    /**
     * Checks if the value at the given path is a generic list.
     * <p>
     * If the value exists at the given path but is not a list, this will return
     * {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the list to check.
     * @return {@code true} if a list value exists, or if there is no value, a
     *         default has been set, and it is a list, {@code false} otherwise.
     */
    boolean isList(@NotNull final String path);
    
    /**
     * Gets the list value at the given path.
     * <p>
     * If the value exists at the given path but is not a list, this will return
     * {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the list to retrieve.
     * @return The requested list.
     */
    @Nullable
    List<?> getList(@NotNull final String path);
    
    /**
     * Gets the list value at the given path.
     * <p>
     * If the value exists at the given path but is not a list, or if the value
     * does not exist at the given path, this will return the given default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the list to retrieve.
     * @param def The list to use as the default.
     * @return The requested list.
     */
    @Contract("_, !null -> !null")
    @Nullable
    List<?> getList(@NotNull final String path, final @Nullable List<?> def);
    
    /**
     * Checks if the value at the given path is a configuration section.
     * <p>
     * If the value exists at the given path but is not a configuration section,
     * this will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the configuration section to check.
     * @return {@code true} if a configuration section value exists, or if there
     *         is no value, a default has been set, and it is a configuration
     *         section. {@code false} otherwise.
     */
    boolean isConfigurationSection(@NotNull final String path);
    
    /**
     * Gets the configuration section value at the given path.
     * <p>
     * If the value exists at the given path but is not a configuration section,
     * this will return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the configuration section to retrieve.
     * @return The requested configuration section.
     */
    @Nullable
    ConfigurationSection getConfigurationSection(@NotNull final String path);
    
    /**
     * Gets the value of type {@code T} at the given path.
     * <p>
     * If the value exists at the given path but is not of type {@code T}, this
     * will return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * <b>Note:</b> Using this method to get a string is <b>not</b> the same as
     * {@link ConfigurationSection#getString(String)}, as the latter internally
     * converts all non-strings to strings, whereas this method will only give
     * the requested value if it is an instance of {@code T}. The same applies
     * to any number, as they are cast to a number internally, and then the
     * correct value is returned.
     * 
     * @param path The path of the value of type {@code T} to retrieve.
     * @param clazz The class of type {@code T} of the requested value.
     * @param <T> The type of the requested value.
     * @return The requested value of type {@code T}.
     */
    @Nullable
    <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz);
    
    /**
     * Gets the value of type {@code T} at the given path.
     * <p>
     * If the value exists at the given path but is not of type {@code T}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * <p>
     * <b>Note:</b> Using this method to get a string is <b>not</b> the same as
     * {@link ConfigurationSection#getString(String)}, as the latter internally
     * converts all non-strings to strings, whereas this method will only give
     * the requested value if it is an instance of {@code T}. The same applies
     * to any number, as they are cast to a number internally, and then the
     * correct value is returned.
     * 
     * @param path The path of the value of type {@code T} to retrieve.
     * @param clazz The class of type {@code T} of the requested value.
     * @param def The value of type {@code T} to use as the default.
     * @param <T> The type of the requested value.
     * @return The requested value of type {@code T}.
     */
    @Contract("_, _, !null -> !null")
    @Nullable
    <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def);
    
    /**
     * Gets the configuration serializable value of type {@code T} at the given
     * path.
     * <p>
     * If the value exists at the given path but is not a configuration
     * serializable of type {@code T}, this will return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the configuration serializable value of type
     *             {@code T} to retrieve.
     * @param clazz The class of type {@code T} of the requested configuration
     *              serializable.
     * @param <T> The type of the requested configuration serializable.
     * @return The requested configuration serializable value of type {@code T}.
     */
    @Nullable
    <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz);
    
    /**
     * Gets the configuration serializable value of type {@code T} value at the
     * given path.
     * <p>
     * If the value exists at the given path but is not a configuration
     * serializable value of type {@code T}, or if the value does not exist at
     * the given path, this will return the given default.
     * <p>
     * If a default value has been set in the root configuration, it will be
     * ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the configuration serializable value of type
     *             {@code T} to retrieve.
     * @param clazz The class of type {@code T} of the requested configuration
     *              serializable.
     * @param def The configuration serializable value of type {@code T} to use
     *            as the default.
     * @param <T> The type of the requested configuration serializable.
     * @return The requested configuration serializable value of type {@code T}.
     */
    @Contract("_, _, !null -> !null")
    @Nullable
    <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def);
    
    /**
     * Gets the list of booleans at the given path.
     * <p>
     * If the value exists at the given path but is not a list of booleans, this
     * will attempt to cast any values into a boolean, if possible. It may miss
     * on any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of booleans to retrieve.
     * @return The requested list of booleans.
     */
    @NotNull
    List<Boolean> getBooleanList(@NotNull final String path);
    
    /**
     * Gets the list of bytes at the given path.
     * <p>
     * If the value exists at the given path but is not a list of bytes, this
     * will attempt to cast any values into a byte, if possible. It may miss on
     * any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of bytes to retrieve.
     * @return The requested list of bytes.
     */
    @NotNull
    List<Byte> getByteList(@NotNull final String path);
    
    /**
     * Gets the list of shorts at the given path.
     * <p>
     * If the value exists at the given path but is not a list of shorts, this
     * will attempt to cast any values into a short, if possible. It may miss on
     * any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of shorts to retrieve.
     * @return The requested list of shorts.
     */
    @NotNull
    List<Short> getShortList(@NotNull final String path);
    
    /**
     * Gets the list of ints at the given path.
     * <p>
     * If the value exists at the given path but is not a list of ints, this
     * will attempt to cast any values into an int, if possible. It may miss
     * on any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of ints to retrieve.
     * @return The requested list of ints.
     */
    @NotNull
    List<Integer> getIntList(@NotNull final String path);
    
    /**
     * Gets the list of longs at the given path.
     * <p>
     * If the value exists at the given path but is not a list of longs, this
     * will attempt to cast any values into a long, if possible. It may miss on
     * any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of longs to retrieve.
     * @return The requested list of longs.
     */
    @NotNull
    List<Long> getLongList(@NotNull final String path);
    
    /**
     * Gets the list of floats at the given path.
     * <p>
     * If the value exists at the given path but is not a list of floats, this
     * will attempt to cast any values into a float, if possible. It may miss on
     * any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of floats to retrieve.
     * @return The requested list of floats.
     */
    @NotNull
    List<Float> getFloatList(@NotNull final String path);
    
    /**
     * Gets the list of doubles at the given path.
     * <p>
     * If the value exists at the given path but is not a list of doubles, this
     * will attempt to cast any values into a double, if possible. It may miss
     * on any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of doubles to retrieve.
     * @return The requested list of doubles.
     */
    @NotNull
    List<Double> getDoubleList(@NotNull final String path);
    
    /**
     * Gets the list of chars at the given path.
     * <p>
     * If the value exists at the given path but is not a list of chars, this
     * will attempt to cast any values into a char, if possible. It may miss on
     * any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of chars to retrieve.
     * @return The requested list of chars.
     */
    @NotNull
    List<Character> getCharList(@NotNull final String path);
    
    /**
     * Gets the list of strings at the given path.
     * <p>
     * If the value exists at the given path but is not a list of strings, this
     * will attempt to cast any values into a string, if possible. It may miss
     * on any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of strings to retrieve.
     * @return The requested list of strings.
     */
    @NotNull
    List<String> getStringList(@NotNull final String path);
    
    /**
     * Gets the list of maps at the given path.
     * <p>
     * If the value exists at the given path but is not a list of maps, this
     * will attempt to cast any values into a map, if possible. It may miss on
     * any values that are not compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty list.
     * 
     * @param path The path of the list of maps to retrieve.
     * @return The requested list of maps.
     */
    @NotNull
    List<Map<?, ?>> getMapList(@NotNull final String path);
}
