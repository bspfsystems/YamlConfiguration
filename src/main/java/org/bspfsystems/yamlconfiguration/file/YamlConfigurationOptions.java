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

package org.bspfsystems.yamlconfiguration.file;

import java.util.List;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the various settings for controlling the input and output of a
 * YAML configuration.
 * <p>
 * Synchronized with the commit on 07-June-2022.
 */
public final class YamlConfigurationOptions extends FileConfigurationOptions {
    
    public static final int DEFAULT_INDENT = 2;
    public static final int DEFAULT_WIDTH = 80;
    public static final int DEFAULT_MAX_ALIASES = 50;
    public static final int DEFAULT_CODE_POINT_LIMIT = 3 * 1024 * 1024; // 3 MB
    
    private int indent;
    private int width;
    private int maxAliases;
    private int codePointLimit;
    
    /**
     * Constructs a set of YAML configuration options.
     * 
     * @param configuration The YAML configuration to create the options for.
     * @see FileConfigurationOptions#FileConfigurationOptions(MemoryConfiguration)
     */
    YamlConfigurationOptions(@NotNull final YamlConfiguration configuration) {
        super(configuration);
        this.indent = DEFAULT_INDENT;
        this.width = DEFAULT_WIDTH;
        this.maxAliases = DEFAULT_MAX_ALIASES;
        this.codePointLimit = DEFAULT_CODE_POINT_LIMIT;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public YamlConfiguration getConfiguration() {
        return (YamlConfiguration) super.getConfiguration();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public YamlConfigurationOptions setPathSeparator(final char pathSeparator) {
        super.setPathSeparator(pathSeparator);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public YamlConfigurationOptions setCopyDefaults(final boolean copyDefaults) {
        super.setCopyDefaults(copyDefaults);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public YamlConfigurationOptions setHeader(@Nullable final List<String> header) {
        super.setHeader(header);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public YamlConfigurationOptions setFooter(@Nullable final List<String> footer) {
        super.setFooter(footer);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public YamlConfigurationOptions setParseComments(final boolean parseComments) {
        super.setParseComments(parseComments);
        return this;
    }
    
    /**
     * Gets the number of spaces used to represent an indent.
     * <p>
     * The minimum value this may be is {@code 2}, and the maximum is {@code 9}.
     * <p>
     * The default value is {@code 2}.
     * 
     * @return The number of spaces used to represent an indent.
     */
    public int getIndent() {
        return this.indent;
    }
    
    /**
     * Sets the number of spaces used to represent an indent.
     * <p>
     * The minimum value this may be is {@code 2}, and the maximum is {@code 9}.
     * <p>
     * The default value is {@code 2}.
     * 
     * @param indent The number of spaces used to represent an indent.
     * @return These YAML configuration options, for chaining.
     * @throws IllegalArgumentException If the given value is less than
     *                                  {@code 2} or greater than {@code 9}.
     */
    @NotNull
    public YamlConfigurationOptions setIndent(final int indent) throws IllegalArgumentException {
        if (indent < 2) {
            throw new IllegalArgumentException("Indent must be at least 2 characters.");
        }
        if (indent > 9) {
            throw new IllegalArgumentException("Indent cannot be greater than 9 characters.");
        }
        this.indent = indent;
        return this;
    }
    
    /**
     * Gets how long a line can be before it gets split.
     * <p>
     * The minimum value this may be is {@code 8}, and the maximum is
     * {@code 1000}.
     * <p>
     * The default value is {@code 80}.
     * 
     * @return The number of characters a line can be before it gets split.
     */
    public int getWidth() {
        return this.width;
    }
    
    /**
     * Sets how long a line can be before it gets split.
     * <p>
     * The minimum value this may be is {@code 8}, and the maximum is
     * {@code 1000}.
     * <p>
     * The default value is {@code 80}.
     * 
     * @param width The number of characters a line can be before it gets split.
     * @return These YAML configuration options, for chaining.
     * @throws IllegalArgumentException If the given value is less than
     *                                  {@code 8} or greater than {@code 1000}.
     */
    @NotNull
    public YamlConfigurationOptions setWidth(final int width) {
        if (width < 8) {
            throw new IllegalArgumentException("Width must be at least 8 characters.");
        }
        if (width > 1000) {
            throw new IllegalArgumentException("Width cannot be greater than 1000 characters.");
        }
        this.width = width;
        return this;
    }
    
    /**
     * Gets the maximum number of aliases for collections.
     * <p>
     * The minimum value this may be is {@code 10}, and the maximum is
     * {@link Integer#MAX_VALUE}.
     * <p>
     * The default value is {@code 50}. It is recommended to keep this value as
     * low as possible for your use case as to prevent a Denial-of-Service known
     * <a href="https://en.wikipedia.org/wiki/Billion_laughs_attack">Billion Laughs Attack</a>.
     * 
     * @return The maximum number of aliases for collections.
     */
    public int getMaxAliases() {
        return this.maxAliases;
    }
    
    /**
     * Sets the maximum number of aliases for collections.
     * <p>
     * The minimum value this may be is {@code 10}, and the maximum is
     * {@link Integer#MAX_VALUE} (please use this wisely).
     * <p>
     * A recommended value is {@code 50}. It is recommended to keep this value
     * as low as possible for your use case as to prevent a Denial-of-Service
     * known
     * <a href="https://en.wikipedia.org/wiki/Billion_laughs_attack">Billion Laughs Attack</a>.
     * 
     * @param maxAliases The maximum number of aliases for collections.
     * @return These YAML configuration options, for chaining.
     * @throws IllegalArgumentException If the given value is less than {@code 10}.
     */
    @NotNull
    public YamlConfigurationOptions setMaxAliases(final int maxAliases) throws IllegalArgumentException {
        if (maxAliases < 10) {
            throw new IllegalArgumentException("Max aliases must be at least 10.");
        }
        this.maxAliases = maxAliases;
        return this;
    }
    
    /**
     * Gets the maximum number of code points that can be loaded in at one time.
     * <p>
     * The minimum is {@code 1kB (1024)}, and the maximum is
     * {@link Integer#MAX_VALUE} (please use this wisely).
     * <p>
     * A recommended value is {@code 3 MB (1024 * 1024 * 3)}.
     * 
     * @return The maximum number of code points.
     */
    public int getCodePointLimit() {
        return this.codePointLimit;
    }
    
    /**
     * Sets the maximum number of code points that can be loaded in at one time.
     * <p>
     * The minimum is {@code 1kB (1024)}, and the maximum is
     * {@link Integer#MAX_VALUE} (please use this wisely).
     * <p>
     * A recommended value is {@code 3 MB (1024 * 1024 * 3)}.
     * 
     * @param codePointLimit The maximum number of code points for loading.
     * @return These YAML configuration options, for chaining.
     * @throws IllegalArgumentException If the given value is less than
     *                                  {@code 1kB (1024)}.
     */
    @NotNull
    public YamlConfigurationOptions setCodePointLimit(final int codePointLimit) throws IllegalArgumentException {
        if (codePointLimit < 1024) {
            throw new IllegalArgumentException("Code point limit must be at least 1kB (1024).");
        }
        this.codePointLimit = codePointLimit;
        return this;
    }
}
