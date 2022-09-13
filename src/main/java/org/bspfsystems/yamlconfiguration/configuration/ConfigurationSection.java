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

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a section of a {@link Configuration}.
 * <p>
 * Synchronized with the commit on 20-December-2021.
 */
public interface ConfigurationSection {
    
    /**
     * Gets a {@link Set} containing all keys in this
     * {@link ConfigurationSection}.
     * <p>
     * If {@code deep} is set to {@code true}, then the {@link Set} will contain
     * all keys within any child
     * {@link ConfigurationSection ConfigurationSection(s)} (and their
     * respective children, etc.) contained within this
     * {@link ConfigurationSection}. The keys will be in a valid path notation
     * for use.
     * <p>
     * If {@code deep} is set to {@code false}, then the {@link Set} will
     * contain only the keys of any direct children, and not any of those
     * children's children.
     * 
     * @param deep {@code true} if the {@link Set} is to contain the keys of the
     *             children {@link ConfigurationSection ConfigurationSection(s)}
     *             of this {@link ConfigurationSection}, {@code false}
     *             otherwise.
     * @return The {@link Set} of keys contained within this
     *         {@link ConfigurationSection}.
     */
    @NotNull
    Set<String> getKeys(final boolean deep);
    
    /**
     * Gets a {@link Map} containing all keys and their values for this
     * {@link ConfigurationSection}.
     * <p>
     * If {@code deep} is set to {@code true}, then the {@link Map} will contain
     * all keys and their respective values within any child
     * {@link ConfigurationSection ConfigurationSection(s)} (and their
     * respective children, etc.) contained in this
     * {@link ConfigurationSection}. The keys will be in a valid path notation
     * for use.
     * <p>
     * If {@code deep} is set to {@code false}, then the {@link Map} will
     * contain only the keys and respective values of any direct children, and
     * not any of those children's children.
     * 
     * @param deep {@code true} if the {@link Map} is to contain the keys and
     *             respective values of the children
     *             {@link ConfigurationSection ConfigurationSection(s)} of this
     *             {@link ConfigurationSection}, {@code false} otherwise.
     * @return A {@link Map} of keys and their respective values contained
     *         within this {@link ConfigurationSection}.
     */
    @NotNull
    Map<String, Object> getValues(final boolean deep);
    
    /**
     * Gets the path of this {@link ConfigurationSection} from its root
     * {@link Configuration}
     * <p>
     * For any {@link Configuration} themselves, this will return an empty
     * {@link String}.
     * <p>
     * If this {@link ConfigurationSection} is no longer contained within its
     * root for any reason, such as being replaced with a different value, this
     * may return {@code null}.
     * <p>
     * To retrieve the single name of this section, that is, the final part of
     * the path returned by this method, you may use
     * {@link ConfigurationSection#getName()}.
     * 
     * @return Path of this {@link ConfigurationSection} relative to its root.
     */
    @Nullable
    String getCurrentPath();
    
    /**
     * Gets the name of this individual {@link ConfigurationSection}, in the
     * path.
     * <p>
     * This will always be the final part of
     * {@link ConfigurationSection#getCurrentPath()}, unless this
     * {@link ConfigurationSection} is orphaned.
     * 
     * @return Name of this {@link ConfigurationSection}.
     */
    @NotNull
    String getName();
    
    /**
     * Gets the root {@link Configuration} that contains this
     * {@link ConfigurationSection}
     * <p>
     * For any {@link Configuration} themselves, this will return its own
     * object.
     * <p>
     * If this {@link ConfigurationSection} is no longer contained within its
     * root for any reason, such as being replaced with a different value, this
     * may return {@code null}.
     * 
     * @return The root {@link Configuration} containing this
     *         {@link ConfigurationSection}.
     */
    @Nullable
    Configuration getRoot();
    
    /**
     * Gets the parent {@link ConfigurationSection} that directly contains
     * this {@link ConfigurationSection}.
     * <p>
     * For any {@link Configuration} themselves, this will return {@code null}.
     * <p>
     * If this {@link ConfigurationSection} is no longer contained within its
     * parent for any reason, such as being replaced with a different value,
     * this may return {@code null}.
     * 
     * @return The parent {@link ConfigurationSection} containing this
     *         {@link ConfigurationSection}.
     */
    @Nullable
    ConfigurationSection getParent();
    
    /**
     * Gets the equivalent {@link ConfigurationSection} from the default
     * {@link Configuration} defined in {@link ConfigurationSection#getRoot()}.
     * <p>
     * If the root contains no defaults, or the defaults do not contain a
     * value for this path, or the value at this path is not a
     * {@link ConfigurationSection}, then this will return {@code null}.
     * 
     * @return The equivalent {@link ConfigurationSection} in the default
     *         {@link ConfigurationSection}.
     */
    @Nullable
    ConfigurationSection getDefaultSection();
    
    /**
     * Sets the default value in the root at the given path as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * If value is {@code null}, the value will be removed from the default
     * {@link Configuration} source.
     * <p>
     * If the value as returned by
     * {@link ConfigurationSection#getDefaultSection()} is {@code null}, then
     * this will create a new {@link ConfigurationSection} at the path,
     * replacing anything that may have existed there previously.
     * 
     * @param path The path of the value to set.
     * @param value The value to set the default to.
     */
    void addDefault(@NotNull final String path, @Nullable final Object value);
    
    /**
     * Gets the requested comment {@link List} by path.
     * <p>
     * If no comments exist, and empty {@link List} will be returned. A
     * {@code null} entry represents an empty line and an empty {@link String}
     * represents an empty comment line.
     * 
     * @param path Path of the comments to get.
     * @return An unmodifiable {@link List} of the requested comments, where
     *         every entry represents one line.
     */
    @NotNull
    List<String> getComments(@NotNull final String path);
    
    /**
     * Gets the requested inline comment {@link List} by path.
     * <p>
     * If no comments exist, an empty {@link List} will be returned. A
     * {@code null} entry represents an empty line and an empty {@link String}
     * represents an empty comment ine.
     * 
     * @param path Path of the comments to get.
     * @return An unmodifiable {@link List} of the requested comments, where
     *         every entry represents one line.
     */
    @NotNull
    List<String> getInLineComments(@NotNull final String path);
    
    /**
     * Sets the comment {@link List} at the specified path.
     * <p>
     * If value is {@code null}, the comments will be removed. A {@code null}
     * entry is an empty line and an empty {@link String} entry is an empty
     * comment line. If the path does not exist, no comments will be set. Any
     * existing comments will be replaced, regardless of what the new comments
     * are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     * 
     * @param path Path of the comments to set.
     * @param comments New comments to set at the path, every entry represents
     *                 one line.
     */
     void setComments(@NotNull final String path, @Nullable final List<String> comments);
    
    /**
     * Sets the inline comment {@link List} at the specified path.
     * <p>
     * If value is {@code null}, the comments will be removed. A {@code null}
     * entry is an empty line and an empty {@link String} entry is an empty
     * comment line. If the path does not exist, no comment will be set. Any
     * existing comments will be replaced, regardless of what the new comments
     * are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     * 
     * @param path Path of the comments to set.
     * @param inLineComments New comments to set at the path, every entry
     *                       represents one line.
     */
    void setInLineComments(@NotNull final String path, @Nullable final List<String> inLineComments);
    
    /**
     * Creates an empty {@link ConfigurationSection} at the specified path.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     *
     * @param path The path to create the {@link ConfigurationSection} at.
     * @return The newly-created {@link ConfigurationSection}.
     */
    @NotNull
    ConfigurationSection createSection(@NotNull final String path);
    
    /**
     * Creates a {@link ConfigurationSection} at the specified path, with the
     * specified values.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     * 
     * @param path The path to create the {@link ConfigurationSection} at.
     * @param map The key-value pairs to place in the
     *            {@link ConfigurationSection}.
     * @return The newly-created {@link ConfigurationSection}.
     */
    @NotNull
    ConfigurationSection createSection(@NotNull final String path, @NotNull final Map<?, ?> map);
    
    /**
     * Checks if this {@link ConfigurationSection} has a value set for the
     * given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will still return {@code false}.
     * 
     * @param path The path to check for existence.
     * @return {@code true} if this {@link ConfigurationSection} contains the
     *         requested path, regardless of having a default. {@code false}
     *         otherwise
     */
    boolean isSet(@NotNull final String path);
    
    /**
     * Sets the specified path to the given value.
     * <p>
     * If the value is {@code null}, the entry will be removed. Any existing
     * entry will be replaced, regardless of what the new value is.
     * <p>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store a {@link Configuration} or a {@link ConfigurationSection},
     * please use {@link ConfigurationSection#createSection(String)} for that.
     * 
     * @param path The path to the given value.
     * @param value The (new) value stored at the given path.
     */
    void set(@NotNull final String path, @Nullable final Object value);
    
    /**
     * Checks if this {@link ConfigurationSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will return {@code true}.
     * 
     * @param path The path to check for existence.
     * @return {@code true} if this {@link ConfigurationSection} contains the
     *         requested path, either via default or being set. {@code false}
     *         otherwise.
     */
    boolean contains(@NotNull final String path);
    
    /**
     * Checks if this {@link ConfigurationSection} contains the given path.
     * <p>
     * If {@code true} is specified for {@code ignoreDefaults}, then this will
     * return {@code true} only if a value has been set for the given path.
     * <p>
     * If {@code false} is specified for {@code ignoreDefaults}, then this will
     * return {@code true} if a value has been set for the given path, or if a
     * default value has been set for the given path.
     * 
     * @param path The path to check for existence.
     * @param ignoreDefault {@code true} if any default values should be ignored
     *                      when checking for a value at the path, {@code false}
     *                      otherwise.
     * @return {@code true} if this {@link ConfigurationSection} contains a
     *         value at the requested path, or if a default value has been set
     *         and {@code ignoreDefaults} is {@code false}. {@code false}
     *         otherwise.
     */
    boolean contains(@NotNull final String path, final boolean ignoreDefault);
    
    /**
     * Gets the {@link Object} at the given path.
     * <p>
     * If a value has not been set at the given path, but a default value has
     * been set, this will return the default value. If no value and no default
     * value have been set, this will return {@code null}.
     * 
     * @param path The path of the {@link Object} to retrieve.
     * @return The requested {@link Object}.
     */
    @Nullable
    Object get(@NotNull final String path);
    
    /**
     * Gets the {@link Object} at the given path, returning the given default
     * value if none has been set.
     * <p>
     * If a value has not been set at the given path, the given default value
     * will be returned, regardless of whether a default value exists in the
     * root {@link Configuration}.
     *
     * @param path The path of the {@link Object} to retrieve.
     * @param def The {@link Object} to use as a default value.
     * @return The requested {@link Object}.
     */
    @Contract("_, !null -> !null")
    @Nullable
    Object get(@NotNull final String path, @Nullable final Object def);
    
    /**
     * Checks if the value at the given path is a {@code boolean}.
     * <p>
     * If the value exists at the given path but is not a {@code boolean}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code boolean} to check.
     * @return {@code true} if a {@code boolean} value exists, or if there is no
     *         value, a default has been set, and it is a {@code boolean}.
     *         {@code false} otherwise.
     */
    boolean isBoolean(@NotNull final String path);
    
    /**
     * Gets the {@code boolean} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code boolean}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code boolean} to retrieve.
     * @return The requested {@code boolean}.
     */
    boolean getBoolean(@NotNull final String path);
    
    /**
     * Gets the {@code boolean} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code boolean}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code boolean} to retrieve.
     * @param def The {@code boolean} to use as the default.
     * @return The requested {@code boolean}.
     */
    boolean getBoolean(@NotNull final String path, final boolean def);
    
    /**
     * Checks if the value at the given path is a {@code byte}.
     * <p>
     * If the value exists at the given path but is not a {@code byte}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code byte} to check.
     * @return {@code true} if a {@code byte} value exists, or if there is no
     *         value, a default has been set, and it is a {@code byte}.
     *         {@code false} otherwise.
     */
    boolean isByte(@NotNull final String path);
    
    /**
     * Gets the {@code byte} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code byte}, this
     * will return {@code (byte) 0}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code byte} to retrieve.
     * @return The requested {@code byte}.
     */
    byte getByte(@NotNull final String path);
    
    /**
     * Gets the {@code byte} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code byte}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code byte} to retrieve.
     * @param def The {@code byte} to use as the default.
     * @return The requested {@code byte}.
     */
    byte getByte(@NotNull final String path, final byte def);
    
    /**
     * Checks if the value at the given path is a {@code short}.
     * <p>
     * If the value exists at the given path but is not a {@code short}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code short} to check.
     * @return {@code true} if a {@code short} value exists, or if there is no
     *         value, a default has been set, and it is a {@code short}.
     *         {@code false} otherwise.
     */
    boolean isShort(@NotNull final String path);
    
    /**
     * Gets the {@code short} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code short}, this
     * will return {@code (short) 0}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code short} to retrieve.
     * @return The requested {@code short}.
     */
    short getShort(@NotNull final String path);
    
    /**
     * Gets the {@code short} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code short}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code short} to retrieve.
     * @param def The {@code short} to use as the default.
     * @return The requested {@code short}.
     */
    short getShort(@NotNull final String path, final short def);
    
    /**
     * Checks if the value at the given path is an {@code int}.
     * <p>
     * If the value exists at the given path but is not an {@code int}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code int} to check.
     * @return {@code true} if an {@code int} value exists, or if there is no
     *         value, a default has been set, and it is an {@code int}.
     *         {@code false} otherwise.
     */
    boolean isInt(@NotNull final String path);
    
    /**
     * Gets the {@code int} value at the given path.
     * <p>
     * If the value exists at the given path but is not an {@code int}, this
     * will return {@code 0}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code int} to retrieve.
     * @return The requested {@code int}.
     */
    int getInt(@NotNull final String path);
    
    /**
     * Gets the {@code int} value at the given path.
     * <p>
     * If the value exists at the given path but is not an {@code int}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code int} to retrieve.
     * @param def The {@code int} to use as the default.
     * @return The requested {@code int}.
     */
    int getInt(@NotNull final String path, final int def);
    
    /**
     * Checks if the value at the given path is a {@code long}.
     * <p>
     * If the value exists at the given path but is not a {@code long}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code long} to check.
     * @return {@code true} if a {@code long} value exists, or if there is no
     *         value, a default has been set, and it is a {@code long}.
     *         {@code false} otherwise.
     */
    boolean isLong(@NotNull final String path);
    
    /**
     * Gets the {@code long} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code long}, this
     * will return {@code 0L}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code long} to retrieve.
     * @return The requested {@code long}.
     */
    long getLong(@NotNull final String path);
    
    /**
     * Gets the {@code long} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code long}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code long} to retrieve.
     * @param def The {@code long} to use as the default.
     * @return The requested {@code long}.
     */
    long getLong(@NotNull final String path, final long def);
    
    /**
     * Checks if the value at the given path is a {@code float}.
     * <p>
     * If the value exists at the given path but is not a {@code float}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code float} to check.
     * @return {@code true} if a {@code float} value exists, or if there is no
     *         value, a default has been set, and it is a {@code float}.
     *         {@code false} otherwise.
     */
    boolean isFloat(@NotNull final String path);
    
    /**
     * Gets the {@code float} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code float}, this
     * will return {@code 0.0F}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code float} to retrieve.
     * @return The requested {@code float}.
     */
    float getFloat(@NotNull final String path);
    
    /**
     * Gets the {@code float} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code float}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code float} to retrieve.
     * @param def The {@code float} to use as the default.
     * @return The requested {@code float}.
     */
    float getFloat(@NotNull final String path, final float def);
    
    /**
     * Checks if the value at the given path is a {@code double}.
     * <p>
     * If the value exists at the given path but is not a {@code double}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code double} to check.
     * @return {@code true} if a {@code double} value exists, or if there is no
     *         value, a default has been set, and it is a {@code double}.
     *         {@code false} otherwise.
     */
    boolean isDouble(@NotNull final String path);
    
    /**
     * Gets the {@code double} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code double}, this
     * will return {@code 0.0D}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code double} to retrieve.
     * @return The requested {@code double}.
     */
    double getDouble(@NotNull final String path);
    
    /**
     * Gets the {@code double} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code double}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code double} to retrieve.
     * @param def The {@code double} to use as the default.
     * @return The requested {@code double}.
     */
    double getDouble(@NotNull final String path, final double def);
    
    /**
     * Checks if the value at the given path is a {@code char}.
     * <p>
     * If the value exists at the given path but is not a {@code char}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@code char} to check.
     * @return {@code true} if a {@code char} value exists, or if there is no
     *         value, a default has been set, and it is a {@code char}.
     *         {@code false} otherwise.
     */
    boolean isChar(@NotNull final String path);
    
    /**
     * Gets the {@code char} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code char}, this
     * will return {@code \u0000}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@code char} to retrieve.
     * @return The requested {@code char}.
     */
    char getChar(@NotNull final String path);
    
    /**
     * Gets the {@code char} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@code char}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@code char} to retrieve.
     * @param def The {@code char} to use as the default.
     * @return The requested {@code char}.
     */
    char getChar(@NotNull final String path, final char def);
    
    /**
     * Checks if the value at the given path is a {@link String}.
     * <p>
     * If the value exists at the given path but is not a {@link String}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@link String} to check.
     * @return {@code true} if a {@link String} value exists, or if there is no
     *         value, a default has been set, and it is a {@link String}.
     *         {@code false} otherwise.
     */
    boolean isString(@NotNull final String path);
    
    /**
     * Gets the {@link String} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link String}, this
     * will return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@link String} to retrieve.
     * @return The requested {@link String}.
     */
    @Nullable
    String getString(@NotNull final String path);
    
    /**
     * Gets the {@link String} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link String}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@link String} to retrieve.
     * @param def The {@link String} to use as the default.
     * @return The requested {@link String}.
     */
    @Contract("_, !null -> !null")
    @Nullable
    String getString(@NotNull final String path, @Nullable final String def);
    
    /**
     * Checks if the value at the given path is a {@link List}.
     * <p>
     * If the value exists at the given path but is not a {@link List}, this
     * will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@link List} to check.
     * @return {@code true} if a {@link List} value exists, or if there is no
     *         value, a default has been set, and it is a {@link List}.
     *         {@code false} otherwise.
     */
    boolean isList(@NotNull final String path);
    
    /**
     * Gets the {@link List} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List}, this
     * will return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@link List} to retrieve.
     * @return The requested {@link List}.
     */
    @Nullable
    List<?> getList(@NotNull final String path);
    
    /**
     * Gets the {@link List} value at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List}, or if
     * the value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param path The path of the {@link List} to retrieve.
     * @param def The {@link List} to use as the default.
     * @return The requested {@link List}.
     */
    @Contract("_, !null -> !null")
    @Nullable
    List<?> getList(@NotNull final String path, final @Nullable List<?> def);
    
    /**
     * Checks if the value at the given path is a {@link ConfigurationSection}.
     * <p>
     * If the value exists at the given path but is not a
     * {@link ConfigurationSection}, this will return {@code false}.
     * <p>
     * If the value does not exist at the given path but a default value has
     * been set, this will check that value and return appropriately.
     * 
     * @param path The path of the {@link ConfigurationSection} to check.
     * @return {@code true} if a {@link ConfigurationSection} value exists, or
     *         if there is no value, a default has been set, and it is a
     *         {@link ConfigurationSection}. {@code false} otherwise.
     */
    boolean isConfigurationSection(@NotNull final String path);
    
    /**
     * Gets the {@link ConfigurationSection} value at the given path.
     * <p>
     * If the value exists at the given path but is not a
     * {@link ConfigurationSection}, this will return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param path The path of the {@link ConfigurationSection} to retrieve.
     * @return The requested {@link ConfigurationSection}.
     */
    @Nullable
    ConfigurationSection getConfigurationSection(@NotNull final String path);
    
    /**
     * Gets the {@link Object} value of type {@link T} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link Object} of type
     * {@link T}, this will return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * <b>Note:</b> Using this method to get a {@link String} is <b>not</b> the
     * same as {@link ConfigurationSection#getString(String)}, as the latter
     * internally converts all non-{@link String Strings} to
     * {@link String Strings}, whereas this method will only give the requested
     * {@link Object} if it is an instance of {@link T}. The same applies to
     * any {@link Number}, as they are cast to a {@link Number} internally, and
     * then the correct value is returned.
     * 
     * @param <T> The type of the requested {@link Object}.
     * @param path The path of the {@link Object} of type {@link T} to retrieve.
     * @param clazz The {@link Class} of type {@link T} of the requested
     *              {@link Object}.
     * @return The requested {@link Object} of type {@link T}.
     */
    @Nullable
    <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz);
    
    /**
     * Gets the {@link Object} value of type {@link T} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link Object} value
     * of type {@link T}, or if the value does not exist at the given path, this
     * will return the given default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * <p>
     * <b>Note:</b> Using this method to get a {@link String} is <b>not</b> the
     * same as {@link ConfigurationSection#getString(String, String)}, as the
     * latter internally converts all non-{@link String Strings} to
     * {@link String Strings}, whereas this method will only give the requested
     * {@link Object} if it is an instance of {@link T}. The same applies to
     * any {@link Number}, as they are cast to a {@link Number} internally, and
     * then the correct value is returned.
     * 
     * @param <T> The type of the requested {@link Object}.
     * @param path The path of the {@link Object} value of type {@link T} to
     *             retrieve.
     * @param clazz The {@link Class} of type {@link T} of the requested
     *              {@link Object}.
     * @param def The {@link Object} value of type {@link T} to use as the
     *            default.
     * @return The requested {@link Object} value of type {@link T}.
     */
    @Contract("_, _, !null -> !null")
    @Nullable
    <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def);
    
    /**
     * Gets the {@link ConfigurationSerializable} value of type {@link T} at the
     * given path.
     * <p>
     * If the value exists at the given path but is not a
     * {@link ConfigurationSerializable} value of type {@link T}, this will
     * return {@code null}.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * 
     * @param <T> The type of the requested {@link ConfigurationSerializable}.
     * @param path The path of the {@link ConfigurationSerializable} value of
     *             type {@link T} to retrieve.
     * @param clazz The {@link Class} of type {@link T} of the requested
     *              {@link ConfigurationSerializable}.
     * @return The requested {@link ConfigurationSerializable} value of type
     *         {@link T}.
     */
    @Nullable
    <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz);
    
    /**
     * Gets the {@link ConfigurationSerializable} value of type {@link T} value
     * at the given path.
     * <p>
     * If the value exists at the given path but is not a
     * {@link ConfigurationSerializable} value of type {@link T}, or if the
     * value does not exist at the given path, this will return the given
     * default.
     * <p>
     * If a default value has been set in the root {@link Configuration}, it
     * will be ignored, even if the given path does not have a set value.
     * 
     * @param <T> The type of the requested {@link ConfigurationSerializable}.
     * @param path The path of the {@link ConfigurationSerializable} value of
     *             type {@link T} to retrieve.
     * @param clazz The {@link Class} of type {@link T} of the requested
     *              {@link ConfigurationSerializable}.
     * @param def The {@link ConfigurationSerializable} value of type {@link T}
     *            to use as the default.
     * @return The requested {@link ConfigurationSerializable} value of type
     *         {@link T}.
     */
    @Contract("_, _, !null -> !null")
    @Nullable
    <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def);
    
    /**
     * Gets the {@link List} of {@link Boolean Booleans} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Boolean Booleans}, this will attempt to cast any values into a
     * {@link Boolean}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Boolean Booleans} to
     *             retrieve.
     * @return The requested {@link List} of {@link Boolean Booleans}.
     */
    @NotNull
    List<Boolean> getBooleanList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Byte Bytes} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Byte Bytes}, this will attempt to cast any values into a
     * {@link Byte}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Byte Bytes} to
     *             retrieve.
     * @return The requested {@link List} of {@link Byte Bytes}.
     */
    @NotNull
    List<Byte> getByteList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Short Shorts} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Short Shorts}, this will attempt to cast any values into a
     * {@link Short}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Short Shorts} to
     *             retrieve.
     * @return The requested {@link List} of {@link Short Shorts}.
     */
    @NotNull
    List<Short> getShortList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Integer Integers} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Integer Integers}, this will attempt to cast any values into a
     * {@link Integer}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Integer Integers} to
     *             retrieve.
     * @return The requested {@link List} of {@link Integer Integers}.
     */
    @NotNull
    List<Integer> getIntList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Long Longs} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Long Longs}, this will attempt to cast any values into a
     * {@link Long}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Long Longs} to
     *             retrieve.
     * @return The requested {@link List} of {@link Long Longs}.
     */
    @NotNull
    List<Long> getLongList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Float Floats} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Float Floats}, this will attempt to cast any values into a
     * {@link Float}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Float Floats} to
     *             retrieve.
     * @return The requested {@link List} of {@link Float Floats}.
     */
    @NotNull
    List<Float> getFloatList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Double Doubles} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Double Doubles}, this will attempt to cast any values into a
     * {@link Double}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Double Doubles} to
     *             retrieve.
     * @return The requested {@link List} of {@link Double Doubles}.
     */
    @NotNull
    List<Double> getDoubleList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Character Characters} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Character Characters}, this will attempt to cast any values into a
     * {@link Character}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Character Characters}
     *             to retrieve.
     * @return The requested {@link List} of {@link Character Characters}.
     */
    @NotNull
    List<Character> getCharList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link String Strings} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link String Strings}, this will attempt to cast any values into a
     * {@link String}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link String Strings} to
     *             retrieve.
     * @return The requested {@link List} of {@link String Strings}.
     */
    @NotNull
    List<String> getStringList(@NotNull final String path);
    
    /**
     * Gets the {@link List} of {@link Map Maps} at the given path.
     * <p>
     * If the value exists at the given path but is not a {@link List} of
     * {@link Map Maps}, this will attempt to cast any values into a
     * {@link Map}, if possible. It may miss on any values that are not
     * compatible.
     * <p>
     * If the value does not exist at the given path, but a default value has
     * been set, this will check that value and return similar to the above.
     * <p>
     * If the value does not exist at the given path and no default value has
     * been set, this will return an empty {@link List}.
     * 
     * @param path The path of the {@link List} of {@link Map Maps} to retrieve.
     * @return The requested {@link List} of {@link Map Maps}.
     */
    @NotNull
    List<Map<?, ?>> getMapList(@NotNull final String path);
}
