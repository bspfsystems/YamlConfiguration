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

import org.jetbrains.annotations.NotNull;

/**
 * Various settings for controlling the input and output of a
 * {@link Configuration}.
 * <p>
 * Synchronized with the commit on 13-March-2019.
 */
public class ConfigurationOptions {
    
    private final Configuration configuration;
    
    private char pathSeparator;
    private boolean copyDefaults;
    
    /**
     * Constructs a new set of {@link ConfigurationOptions}.
     * 
     * @param configuration The {@link Configuration} to create the
     *                      {@link ConfigurationOptions} for.   
     */
    protected ConfigurationOptions(@NotNull final Configuration configuration) {
        this.configuration = configuration;
        this.pathSeparator = '.';
        this.copyDefaults = false;
    }
    
    /**
     * Gets the {@link Configuration} that this {@link ConfigurationOptions}
     * controls.
     * 
     * @return The {@link Configuration} that this {@link ConfigurationOptions}
     *         controls.
     */
    @NotNull
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link ConfigurationOptions#getConfiguration()} instead; it
     * provides the same functionality.
     * 
     * @return The {@link Configuration} that this {@link ConfigurationOptions}
     *         controls.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link ConfigurationOptions#getConfiguration()} instead.
     * @see ConfigurationOptions#getConfiguration()
     */
    @Deprecated
    @NotNull
    public Configuration configuration() {
        return this.getConfiguration();
    }
    
    /**
     * Gets the {@code char} that will be used to separate
     * {@link ConfigurationSection ConfigurationSections}.
     * <p>
     * This value does not affect how the {@link Configuration} is stored,
     * only in how you access the data.
     * <p>
     * The default value is {@code .}.
     * 
     * @return The {@code char} used to separate
     *         {@link ConfigurationSection ConfigurationSections}.
     */
    public final char getPathSeparator() {
        return this.pathSeparator;
    }
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link ConfigurationOptions#getPathSeparator()} instead; it
     * provides the same functionality.
     * 
     * @return The {@code char} used to separate
     *         {@link ConfigurationSection ConfigurationSections}.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link ConfigurationOptions#getPathSeparator()} instead.
     * @see ConfigurationOptions#getPathSeparator()
     */
    @Deprecated
    public final char pathSeparator() {
        return this.getPathSeparator();
    }
    
    /**
     * Sets the {@code char} that will be used to separate
     * {@link ConfigurationSection ConfigurationSections}.
     * <p>
     * This value does not affect how the {@link Configuration} is stored,
     * only in how you access the data.
     * <p>
     * The default value is {@code .}.
     * 
     * @param pathSeparator The {@code char} used to separate
     *                      {@link ConfigurationSection ConfigurationSections}.
     * @return This {@link ConfigurationOptions}, for chaining.
     */
    @NotNull
    public ConfigurationOptions setPathSeparator(final char pathSeparator) {
        this.pathSeparator = pathSeparator;
        return this;
    }
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link ConfigurationOptions#setPathSeparator(char)} instead;
     * it provides the same functionality.
     * 
     * @param pathSeparator The {@code char} used to separate
     *                      {@link ConfigurationSection ConfigurationSections}.
     * @return This {@link ConfigurationOptions}, for chaining.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link ConfigurationOptions#setPathSeparator(char)} instead.
     * @see ConfigurationOptions#setPathSeparator(char)
     */
    @Deprecated
    @NotNull
    public ConfigurationOptions pathSeparator(final char pathSeparator) {
        return this.setPathSeparator(pathSeparator);
    }
    
    /**
     * Checks if the {@link Configuration} should copy values from its default
     * {@link Configuration} directly.
     * <p>
     * If this is {@code true}, all values in the default {@link Configuration}
     * will be directly copied, making it impossible to distinguish between
     * values that were set and values that are provided by default. As a
     * result, {@link ConfigurationSection#contains(String)} will always return
     * the same value as {@link ConfigurationSection#isSet(String)}.
     * <p>
     * The default value is {@code false}.
     * 
     * @return {@code true} if the default values should be copied,
     *         {@code false} otherwise.
     */
    public final boolean getCopyDefaults() {
        return this.copyDefaults;
    }
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link ConfigurationOptions#getCopyDefaults()} instead; it
     * provides the same functionality.
     * 
     * @return {@code true} if the default values should be copied,
     *         {@code false} otherwise.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link ConfigurationOptions#getCopyDefaults()} instead.
     * @see ConfigurationOptions#getCopyDefaults()
     */
    @Deprecated
    public final boolean copyDefaults() {
        return this.getCopyDefaults();
    }
    
    /**
     * Sets if the {@link Configuration} should copy values from its default
     * {@link Configuration} directly.
     * <p>
     * If this is {@link true}, all values in the default {@link Configuration}
     * will be directly copied, making it impossible to distinguish between
     * values that were set and values that are provided by default. As a
     * result, {@link ConfigurationSection#contains(String)} will always return
     * the same value as {@link ConfigurationSection#isSet(String)}.
     * <p>
     * The default value is {@code false}.
     * 
     * @param copyDefaults {@code true} if the default values should be copied,
     *                     {@code false} otherwise.
     * @return This {@link ConfigurationOptions}, for chaining.
     */
    @NotNull
    public ConfigurationOptions setCopyDefaults(final boolean copyDefaults) {
        this.copyDefaults = copyDefaults;
        return this;
    }
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link ConfigurationOptions#setCopyDefaults(boolean)} instead;
     * it provides the same functionality.
     * 
     * @param copyDefaults {@code true} if the default values should be copied,
     *                     {@code false} otherwise.
     * @return This {@link ConfigurationOptions}, for chaining.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link ConfigurationOptions#setCopyDefaults(boolean)}
     *             instead.
     * @see ConfigurationOptions#setCopyDefaults(boolean)
     */
    @Deprecated
    @NotNull
    public ConfigurationOptions copyDefaults(final boolean copyDefaults) {
        return this.setCopyDefaults(copyDefaults);
    }
}
