/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2024 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2025 BSPF Systems, LLC (https://bspfsystems.org/)
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

import java.util.Collections;
import java.util.List;
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfiguration;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Represents the various settings for controlling the input and output of a
 * file configuration.
 * <p>
 * Synchronized with the commit on 24-November-2024.
 */
public class FileConfigurationOptions extends MemoryConfigurationOptions {
    
    public static final List<String> DEFAULT_HEADER = Collections.emptyList();
    public static final List<String> DEFAULT_FOOTER = Collections.emptyList();
    public static final boolean DEFAULT_PARSE_COMMENTS = true;
    
    private List<String> header;
    private List<String> footer;
    private boolean parseComments;
    
    /**
     * Constructs a set of file configuration options.
     *
     * @param configuration The file configuration to create the options for.
     * @see MemoryConfigurationOptions#MemoryConfigurationOptions(MemoryConfiguration)
     */
    protected FileConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
        super(configuration);
        this.header = DEFAULT_HEADER;
        this.footer = DEFAULT_FOOTER;
        this.parseComments = DEFAULT_PARSE_COMMENTS;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public FileConfiguration getConfiguration() {
        return (FileConfiguration) super.getConfiguration();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public FileConfigurationOptions setPathSeparator(final char pathSeparator) {
        super.setPathSeparator(pathSeparator);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public FileConfigurationOptions setCopyDefaults(final boolean copyDefaults) {
        super.setCopyDefaults(copyDefaults);
        return this;
    }
    
    /**
     * Gets the header comments that will be saved at the top of the output file
     * configuration. If no header comments exist, an empty list will be
     * returned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty header comment line ({@code #} and nothing else). Each entry in the
     * list represents 1 line of header comments.
     * <p>
     * The list cannot be modified. The returned list represents a snapshot of
     * the header comments at the time the list was returned; any changes to the
     * actual header comments will not be reflected in this list.
     * 
     * @return The header comments for the file configuration controlled by this
     *         file configuration options.
     */
    @NotNull
    @UnmodifiableView
    public final List<String> getHeader() {
        return this.header;
    }
    
    /**
     * Assigns the given header comments to these file configuration options. If
     * the given list is {@code null}, an empty list will be assigned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty header comment line ({@code #} and nothing else). Each entry in the
     * list represents 1 line of header comments.
     * <p>
     * The given list will not be directly saved; instead, a snapshot will be
     * taken and used to create an unmodifiable copy internally. Further updates
     * to the given list will not result in changes to the inline comments
     * stored in these file configuration options after this method completes.
     * <p>
     * Any existing header comments will be replaced, regardless of their
     * value(s) compared to the new header comments.
     * 
     * @param header The header comments to assign to these file configuration
     *               options.
     * @return These file configuration options, for chaining.
     */
    @NotNull
    public FileConfigurationOptions setHeader(@Nullable final List<String> header) {
        this.header = (header == null) ? DEFAULT_HEADER : Collections.unmodifiableList(header);
        return this;
    }
    
    /**
     * Gets the footer comments that will be saved at the top of the output file
     * configuration. If no footer comments exist, an empty list will be
     * returned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty footer comment line ({@code #} and nothing else). Each entry in the
     * list represents 1 line of footer comments.
     * <p>
     * The list cannot be modified. The returned list represents a snapshot of
     * the footer comments at the time the list was returned; any changes to the
     * actual footer comments will not be reflected in this list.
     * 
     * @return The footer comments for the file configuration controlled by this
     *         file configuration options.
     */
    @NotNull
    @UnmodifiableView
    public final List<String> getFooter() {
        return this.footer;
    }
    
    /**
     * Assigns the given footer comments to these file configuration options. If
     * the given list is {@code null}, an empty list will be assigned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty footer comment line ({@code #} and nothing else). Each entry in the
     * list represents 1 line of footer comments.
     * <p>
     * The given list will not be directly saved; instead, a snapshot will be
     * taken and used to create an unmodifiable copy internally. Further updates
     * to the given list will not result in changes to the inline comments
     * stored in these file configuration options after this method completes.
     * <p>
     * Any existing footer comments will be replaced, regardless of their
     * value(s) compared to the new footer comments.
     * 
     * @param footer The footer comments to assign to these file configuration
     *               options.
     * @return These file configuration options, for chaining.
     */
    @NotNull
    public FileConfigurationOptions setFooter(@Nullable final List<String> footer) {
        this.footer = (footer == null) ? DEFAULT_FOOTER : Collections.unmodifiableList(footer);
        return this;
    }
    
    /**
     * Gets whether the comments (header, block, inline, and/or footer) in a
     * file configuration should be loaded and saved.
     * <p>
     * If this returns {@code true}, and if a default file configuration is
     * passed to {@link Configuration#setDefaults(Configuration)}, then upon
     * saving, the default comments will be parsed from the passed default file
     * configuration, instead of the ones provided in here.
     * <p>
     * If no default is set on the configuration, or the default is not a file
     * configuration, or that configuration has no comments, then the comments
     * specified in the original configuration will be used.
     * <p>
     * The default value is {@code true}.
     * 
     * @return {@code true} if the comments are to be parsed, {@code false}
     *         otherwise.
     */
    public final boolean getParseComments() {
        return this.parseComments;
    }
    
    /**
     * Sets whether the comments (header, block, inline, and/or footer) in a
     * file configuration should be loaded and saved.
     * <p>
     * If this is {@code true}, and if a default file configuration is passed to
     * {@link Configuration#setDefaults(Configuration)}, then upon saving, the
     * default comments will be parsed from the passed default file
     * configuration, instead of the ones provided in here.
     * <p>
     * If no default is set on the configuration, or the default is not a file
     * configuration, or that configuration has no comments, then the comments
     * specified in the original configuration will be used.
     * <p>
     * The default value is {@code true}.
     * 
     * @param parseComments {@code true} if the comments are to be parsed,
     *                      {@code false} otherwise.
     * @return This file configuration options, for chaining.
     */
    @NotNull
    public FileConfigurationOptions setParseComments(final boolean parseComments) {
        this.parseComments = parseComments;
        return this;
    }
}
