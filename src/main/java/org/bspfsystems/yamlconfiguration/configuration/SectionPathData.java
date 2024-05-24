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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Represents data that may be stored in a memory section/memory configuration,
 * along with any comments that may be associated with it.
 * <p>
 * Synchronized with the commit on 20-December-2021.
 */
final class SectionPathData {
    
    private Object data;
    private List<String> comments;
    private List<String> inLineComments;
    
    /**
     * Constructs a basic section path data with the given data and no comments.
     * 
     * @param data The data for the new section path data.
     */
    SectionPathData(@Nullable final Object data) {
        this.data = data;
        this.comments = Collections.emptyList();
        this.inLineComments = Collections.emptyList();
    }
    
    /**
     * Gets the data stored in this section path data.
     * 
     * @return The data stored in this section path data.
     */
    @Nullable
    Object getData() {
        return this.data;
    }
    
    /**
     * Sets the data stored in this section path data.
     * <p>
     * This will override any previously-set value, regardless of what it was.
     * 
     * @param data The updated data to store.
     */
    void setData(@Nullable final Object data) {
        this.data = data;
    }
    
    /**
     * Gets the comments on this section path data as a list of strings. If no
     * comments exist, an empty list will be returned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty comment line ({@code #} and nothing else). Each entry in the list
     * represents 1 line of comments.
     * <p>
     * The list cannot be modified. The returned list represents a snapshot of
     * the comments at the time the list was returned; any changes to the
     * actual comments will not be reflected in this list.
     * 
     * @return The comments for this section path data, where each list entry
     *         represents 1 line.
     */
    @NotNull
    @UnmodifiableView
    List<String> getComments() {
        return this.comments;
    }
    
    /**
     * Assigns the given comments to this section path data. If the given list
     * is {@code null}, an empty list will be assigned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty comment line ({@code #} and nothing else). Each entry in the list
     * represents 1 line of comments.
     * <p>
     * The given list will not be directly saved; instead, a snapshot will be
     * taken and used to create an unmodifiable copy internally. Further updates
     * to the given list will not result in changes to the comments stored in
     * this section path data after this method completes.
     * <p>
     * Any existing comments will be replaced, regardless of their value(s)
     * compared to the new comments.
     * 
     * @param comments The comments to assign to this section path data.
     */
    void setComments(@Nullable final List<String> comments) {
        this.comments = (comments == null) ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(comments));
    }
    
    /**
     * Gets the inline comments on this section path data as a list of strings.
     * If no inline comments exist, an empty list will be returned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty inline comment line ({@code #} and nothing else). Each entry in the
     * list represents 1 line of inline comments.
     * <p>
     * The list cannot be modified. The returned list represents a snapshot of
     * the inline comments at the time the list was returned; any changes to the
     * actual inline comments will not be reflected in this list.
     * 
     * @return The inline comments for this section path data, where each list
     *         entry represents 1 line.
     */
    @NotNull
    List<String> getInlineComments() {
        return this.inLineComments;
    }
    
    /**
     * Assigns the given inline comments to this section path data. If the given
     * list is {@code null}, an empty list will be assigned.
     * <p>
     * For the individual string entries in the list; a {@code null} entry
     * represents an empty line, whereas an empty string entry represents an
     * empty inline comment line ({@code #} and nothing else). Each entry in the
     * list represents 1 line of inline comments.
     * <p>
     * The given list will not be directly saved; instead, a snapshot will be
     * taken and used to create an unmodifiable copy internally. Further updates
     * to the given list will not result in changes to the inline comments
     * stored in this section path data after this method completes.
     * <p>
     * Any existing inline comments will be replaced, regardless of their
     * value(s) compared to the new inline comments.
     * 
     * @param inLineComments The inline comments to assign to this section path
     *                       data.
     */
    void setInlineComments(@Nullable final List<String> inLineComments) {
        this.inLineComments = inLineComments == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(inLineComments));
    }
}
