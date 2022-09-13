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

import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents data that may be stored in a {@link MemoryConfiguration}, along
 * with any comments that may be associated with it.
 * <p>
 * Synchronized with the commit on 20-December-2021.
 */
final class SectionPathData {
    
    private Object data;
    private List<String> comments;
    private List<String> inLineComments;
    
    /**
     * Creates a {@link SectionPathData} with the given {@link Object} data and
     * no comments.
     * 
     * @param data The data for the {@link SectionPathData}.
     */
    SectionPathData(@Nullable final Object data) {
        this.data = data;
        this.comments = Collections.emptyList();
        this.inLineComments = Collections.emptyList();
    }
    
    /**
     * Gets the data stored in this {@link SectionPathData}.
     * 
     * @return The stored data.
     */
    @Nullable
    Object getData() {
        return this.data;
    }
    
    /**
     * Assigns the data stored in this {@link SectionPathData} as the given
     * {@link Object}.
     * 
     * @param data The {@link Object} to store.
     */
    void setData(@Nullable final Object data) {
        this.data = data;
    }
    
    /**
     * Gets the comments on the {@link ConfigurationSection} entry.
     * <p>
     * If no comments exist, an empty {@link List} will be returned. A
     * {@code null} entry in the {@link List} represents an empty line and an
     * empty {@link String} represents an empty comment line.
     * 
     * @return An unmodifiable {@link List} of the requested comments, where
     *         every entry represents one line.
     */
    @NotNull
    List<String> getComments() {
        return this.comments;
    }
    
    /**
     * Assigns the given comments to a {@link ConfigurationSection} entry.
     * <p>
     * If an empty {@link List} is provided, then no comments will be added. A
     * {@code null} entry in the {@link List} represents an empty comment line,
     * while an empty {@link String} represents an empty comment line.
     * <p>
     * Any existing comments will be replaced, regardless of the value of the
     * new comments.
     * 
     * @param comments The comments to assign to this {@link SectionPathData}.
     */
    void setComments(@Nullable final List<String> comments) {
        this.comments = (comments == null) ? Collections.emptyList() : Collections.unmodifiableList(comments);
    }
    
    /**
     * Gets the inline comments on the {@link ConfigurationSection} entry.
     * <p>
     * If no inline comments exist, an empty {@link List} will be returned. A
     * {@code null} entry in the {@link List} represents an empty line and an
     * empty {@link String} represents an empty comment line.
     * 
     * @return An unmodifiable {@link List} of the requested inline comments,
     *         where every entry represents one line.
     */
    @NotNull
    List<String> getInLineComments() {
        return this.inLineComments;
    }
    
    /**
     * Assigns the given inline comments to a {@link ConfigurationSection}
     * entry.
     * <p>
     * If an empty {@link List} is provided, then no inline comments will be
     * added. A {@code null} entry in the {@link List} represents an empty
     * inline comment line, while an empty {@link String} represents an empty
     * inline comment line.
     * <p>
     * Any existing inline comments will be replaced, regardless of the value of
     * the new inline comments.
     * 
     * @param inLineComments The inline comments to assign to this
     *                       {@link SectionPathData}.
     */
    void setInLineComments(@Nullable final List<String> inLineComments) {
        this.inLineComments = (inLineComments == null) ? Collections.emptyList() : Collections.unmodifiableList(inLineComments);
    }
}
