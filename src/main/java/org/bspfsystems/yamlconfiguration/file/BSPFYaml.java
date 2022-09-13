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

package org.bspfsystems.yamlconfiguration.file;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentEventsCollector;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.serializer.Serializer;

/**
 * Represents a custom {@link Yaml} that allows working with comments.
 * <p>
 * Synchronized with the commit on 03-January-2022.
 */
final class BSPFYaml extends Yaml {
    
    private static final Field EVENTS;
    private static final Field BLOCK_COMMENTS_COLLECTOR;
    private static final Field IN_LINE_COMMENTS_COLLECTOR;
    
    /**
     * Gets the specified {@link Field} by name, setting it to be accessible.
     * 
     * @param name The name of the {@link Field} to retrieve and make
     *             accessible.
     * @return The requested {@link Field}.
     */
    private static Field getEmitterField(@NotNull final String name) {
        Field field = null;
        try {
            field = Emitter.class.getDeclaredField(name);
            field.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            Logger.getLogger(BSPFYaml.class.getName()).log(Level.SEVERE, "Unable to access Field by name: " + name, e);
        }
        return field;
    }
    
    static {
        EVENTS = BSPFYaml.getEmitterField("events");
        BLOCK_COMMENTS_COLLECTOR = BSPFYaml.getEmitterField("blockCommentsCollector");
        IN_LINE_COMMENTS_COLLECTOR = BSPFYaml.getEmitterField("inlineCommentsCollector");
    }
    
    /**
     * Creates a new {@link BSPFYaml}.
     * 
     * @see Yaml#Yaml(BaseConstructor, Representer, DumperOptions, LoaderOptions)
     */
    BSPFYaml(@NotNull final BaseConstructor baseConstructor, @NotNull final Representer representer, @NotNull final DumperOptions dumperOptions, @NotNull final LoaderOptions loaderOptions) {
        super(baseConstructor, representer, dumperOptions, loaderOptions);
    }
    
    /**
     * Serializes the given {@link Node} to the given {@link Writer} with
     * {@link CommentLine} support.
     * 
     * @param node The {@link Node} to write out.
     * @param writer The {@link Writer} the {@link Node} will be written to.
     * @throws RuntimeException If an error occurs while writing.
     */
    @Override
    public void serialize(@NotNull final Node node, @NotNull final Writer writer) throws RuntimeException {
        
        final Emitter emitter = new Emitter(writer, this.dumperOptions);
        if (BSPFYaml.EVENTS != null && BSPFYaml.BLOCK_COMMENTS_COLLECTOR != null && BSPFYaml.IN_LINE_COMMENTS_COLLECTOR != null) {
            
            final Queue<Event> newEvents = new ArrayDeque<Event>(100);
            try {
                BSPFYaml.EVENTS.set(emitter, newEvents);
                BSPFYaml.BLOCK_COMMENTS_COLLECTOR.set(emitter, new CommentEventsCollector(newEvents, CommentType.BLANK_LINE, CommentType.BLOCK));
                BSPFYaml.IN_LINE_COMMENTS_COLLECTOR.set(emitter, new CommentEventsCollector(newEvents, CommentType.IN_LINE));
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Cannot update Yaml event queue.", e);
            }
        }
        
        final Serializer serializer = new Serializer(emitter, this.resolver, this.dumperOptions, null);
        try {
            serializer.open();
            serializer.serialize(node);
            serializer.close();
        } catch (IOException e) {
            throw new YAMLException(e);
        }
    }
}
