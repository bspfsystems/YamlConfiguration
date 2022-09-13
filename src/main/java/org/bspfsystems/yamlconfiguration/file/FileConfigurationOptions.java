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

package org.bspfsystems.yamlconfiguration.file;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfiguration;
import org.bspfsystems.yamlconfiguration.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Various settings for controlling the input and output of a
 * {@link FileConfiguration}.
 * <p>
 * Synchronized with the commit on 20-December-2021.
 */
public class FileConfigurationOptions extends MemoryConfigurationOptions {
    
    private List<String> header;
    private List<String> footer;
    private boolean parseComments;
    
    /**
     * Constructs a new set of {@link FileConfigurationOptions}.
     *
     * @param configuration The {@link MemoryConfiguration} to create the
     *                      {@link FileConfigurationOptions} for.
     * @see MemoryConfigurationOptions#MemoryConfigurationOptions(MemoryConfiguration)
     */
    protected FileConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
        super(configuration);
        this.header = Collections.emptyList();
        this.footer = Collections.emptyList();
        this.parseComments = true;
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
    @Deprecated
    @Override
    @NotNull
    public FileConfiguration configuration() {
        return (FileConfiguration) super.configuration();
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
    @Deprecated
    @Override
    @NotNull
    public FileConfigurationOptions pathSeparator(final char pathSeparator) {
        super.pathSeparator(pathSeparator);
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
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    @NotNull
    public FileConfigurationOptions copyDefaults(final boolean copyDefaults) {
        super.copyDefaults(copyDefaults);
        return this;
    }
    
    /**
     * Gets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link FileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, and empty {@link List} will be returned. A
     * {@code null} entry represents an empty line, and an empty {@link String}
     * represents an empty comment line.
     * <p>
     * The default header is no header.
     * 
     * @return An unmodifiable header as a {@link List}, where every entry
     *         represents one line.
     */
    @NotNull
    public final List<String> getHeader() {
        return this.header;
    }
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link FileConfigurationOptions#getHeader()} instead.
     * 
     * @return An unmodifiable header as a {@link List}, where every entry
     *         represents one line.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link FileConfigurationOptions#getHeader()} instead.
     * @see FileConfigurationOptions#getHeader()
     */
    @Deprecated
    @NotNull
    public final String header() {
        final StringBuilder builder = new StringBuilder();
        for (final String line : this.header) {
            builder.append(line == null ? "" : line).append("\n");
        }
        return builder.toString();
    }
    
    /**
     * Sets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link FileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty {@link List} will be returned. A
     * {@code null} entry represents an empty line, and an empty {@link String}
     * represents an empty comment line.
     * <p>
     * The default header is no header.
     * 
     * @param header The new header, where every entry represents one line.
     * @return This {@link FileConfigurationOptions}, for chaining.
     */
    @NotNull
    public FileConfigurationOptions setHeader(@Nullable final List<String> header) {
        this.header = (header == null) ? Collections.emptyList() : Collections.unmodifiableList(header);
        return this;
    }
    
    /**
     * This method exists for backwards compatibility, and it will be removed in
     * a future release.
     * <p>
     * Please use {@link FileConfigurationOptions#setHeader(List)} instead.
     *
     * @param header The new header, where every entry represents one line.
     * @return This {@link FileConfigurationOptions}, for chaining.
     * @deprecated This method exists for backwards compatibility. Please use
     *             {@link FileConfigurationOptions#setHeader(List)} instead.
     * @see FileConfigurationOptions#setHeader(List)
     */
    @Deprecated
    @NotNull
    public FileConfigurationOptions header(@Nullable final String header) {
        this.header = (header == null) ? Collections.emptyList() : Collections.unmodifiableList(Arrays.asList(header.split("\\n")));
        return this;
    }
    
    /**
     * Gets the footer that will be applied to the bottom of the saved output.
     * <p>
     * This footer will be commented out and applied directly at the bottom of
     * the generated output of the {@link FileConfiguration}. It is not required
     * to include a newline at the beginning of the footer as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty {@link List} will be returned. A
     * {@link null} entry represents an empty line and an empty
     * {@link String} represents an empty comment line.
     * <p>
     * The default footer is no footer.
     * 
     * @return An unmodifiable footer, where every entry represents one line.
     */
    @NotNull
    public final List<String> getFooter() {
        return this.footer;
    }
    
    /**
     * Sets the footer that will be applied to the bottom of the saved output.
     * <p>
     * This footer will be commented out and applied directly at the bottom of
     * the generated output of the {@link FileConfiguration}. It is not required
     * to include a newline at the beginning of the footer as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty {@link List} will be returned. A {@code null}
     * entry represents an empty line and an empty {@link String} represents an
     * empty comment line.
     * <p>
     * The default footer is no footer.
     * 
     * @param footer The new footer, where every entry represents one line.
     * @return This {@link FileConfigurationOptions}, for chaining.
     */
    @NotNull
    public FileConfigurationOptions setFooter(@Nullable final List<String> footer) {
        this.footer = (footer == null) ? Collections.emptyList() : Collections.unmodifiableList(footer);
        return this;
    }
    
    /**
     * Gets whether the comments in a {@link FileConfiguration} should be loaded
     * and saved.
     * <p>
     * If this is {@code true}, and if a default {@link FileConfiguration} is
     * passed to {@link Configuration#setDefaults(Configuration)}, then upon
     * saving, the default comments will be parsed from the passed default
     * {@link FileConfiguration}, instead of the ones provided in here.
     * <p>
     * If no default is set on the {@link Configuration}, or the default is not
     * a {@link FileConfiguration}, or that {@link Configuration} has no
     * comments ({@link FileConfigurationOptions#getHeader()} returns an empty
     * {@link List}), then the header specified in this {@link Configuration}
     * will be used.
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
     * This method only exists for backwards compatibility, and it will be
     * removed in a future release.
     * <p>
     * Please use {@link FileConfigurationOptions#getParseComments()} instead;
     * it provides the same functionality.
     *
     * @return {@code true} if the comments are to be parsed, {@code false}
     *         otherwise.
     * @deprecated This method only exists for backwards compatibility. Please
     *             use {@link FileConfigurationOptions#getParseComments()}
     *             instead.
     * @see FileConfigurationOptions#getParseComments()
     */
    @Deprecated
    public final boolean copyHeader() {
        return this.parseComments;
    }
    
    /**
     * Sets whether the comments in a {@link FileConfiguration} should be loaded
     * and saved.
     * <p>
     * If this is {@code true}, and if a default {@link FileConfiguration} is
     * passed to {@link Configuration#setDefaults(Configuration)}, then upon
     * saving, the default comments will be parsed from the passed default
     * {@link FileConfiguration}, instead of the ones provided in here.
     * <p>
     * If no default is set on the {@link Configuration}, or the default is not
     * a {@link FileConfiguration}, or that {@link Configuration} has no
     * comments ({@link FileConfigurationOptions#getHeader()} returns an empty
     * {@link List}), then the header specified in this {@link Configuration}
     * will be used.
     * <p>
     * The default value is {@code true}.
     * 
     * @param parseComments {@code true} if the comments are to be parsed,
     *                      {@code false} otherwise.
     * @return This {@link FileConfigurationOptions}, for chaining.
     */
    @NotNull
    public FileConfigurationOptions setParseComments(final boolean parseComments) {
        this.parseComments = parseComments;
        return this;
    }
    
    /**
     * This method only exists for backwards compatibility, and it will be
     * removed in a future release.
     * <p>
     * Please use {@link FileConfigurationOptions#setParseComments(boolean)}
     * instead; it provides the same functionality.
     *
     * @param parseComments {@code true} if the comments are to be parsed,
     *                      {@code false} otherwise.
     * @return This {@link FileConfigurationOptions}, for chaining.
     * @deprecated This method only exists for backwards compatibility. Please
     *             use
     *             {@link FileConfigurationOptions#setParseComments(boolean)}
     *             instead.
     * @see FileConfigurationOptions#setParseComments(boolean)
     */
    @Deprecated
    @NotNull
    public FileConfigurationOptions copyHeader(final boolean parseComments) {
        this.parseComments = parseComments;
        return this;
    }
}
