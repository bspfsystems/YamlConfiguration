/* 
 * This file is part of YamlConfiguration.
 * 
 * Implementation of SnakeYAML to be easy to use with files.
 * 
 * Copyright (C) 2010-2014 The Bukkit Project (https://bukkit.org/)
 * Copyright (C) 2014-2023 SpigotMC Pty. Ltd. (https://www.spigotmc.org/)
 * Copyright (C) 2020-2023 BSPF Systems, LLC (https://bspfsystems.org/)
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 * An implementation of {@link Configuration} which saves all files in
 * {@link Yaml}. Please note that this implementation is not synchronized.
 * <p>
 * Synchronized with the commit on 14-Mar-2023.
 */
public final class YamlConfiguration extends FileConfiguration {
    
    private final YamlConstructor yamlConstructor;
    private final YamlRepresenter yamlRepresenter;
    private final DumperOptions dumperOptions;
    private final LoaderOptions loaderOptions;
    private final Yaml yaml;
    
    /**
     * Creates an empty {@link YamlConfiguration} with no default values.
     * 
     * @see FileConfiguration#FileConfiguration()
     */
    public YamlConfiguration() {
        super();
        
        
        
        this.dumperOptions = new DumperOptions();
        this.dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        this.loaderOptions = new LoaderOptions();
        
        this.yamlConstructor = new YamlConstructor(this.loaderOptions);
        
        this.yamlRepresenter = new YamlRepresenter(this.dumperOptions);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        this.yaml = new Yaml(this.yamlConstructor, this.yamlRepresenter, this.dumperOptions, this.loaderOptions);
    }
    
    /**
     * Creates an empty {@link YamlConfiguration} using the specified
     * {@link Configuration} as a source for all default values.
     *
     * @param defs A {@link Configuration} containing the values to use as
     *             defaults.
     * @see FileConfiguration#FileConfiguration(Configuration)
     */
    public YamlConfiguration(@Nullable final Configuration defs) {
        super(defs);
        
        this.dumperOptions = new DumperOptions();
        this.dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        this.loaderOptions = new LoaderOptions();
        
        this.yamlConstructor = new YamlConstructor(this.loaderOptions);
        
        this.yamlRepresenter = new YamlRepresenter(this.dumperOptions);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        
        this.yaml = new Yaml(this.yamlConstructor, this.yamlRepresenter, this.dumperOptions, this.loaderOptions);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String saveToString() {
        
        this.dumperOptions.setIndent(this.getOptions().getIndent());
        this.dumperOptions.setWidth(this.getOptions().getWidth());
        this.dumperOptions.setProcessComments(this.getOptions().getParseComments());
        
        final MappingNode mappingNode = this.toNodeTree(this);
        mappingNode.setBlockComments(this.getCommentLines(this.saveHeader(this.getOptions().getHeader()), CommentType.BLOCK));
        mappingNode.setEndComments(this.getCommentLines(this.getOptions().getFooter(), CommentType.BLOCK));
        
        final StringWriter writer = new StringWriter();
        if (mappingNode.getBlockComments().isEmpty() && mappingNode.getEndComments().isEmpty() && mappingNode.getValue().isEmpty()) {
            writer.write("");
        } else {
            if (mappingNode.getValue().isEmpty()) {
                mappingNode.setFlowStyle(DumperOptions.FlowStyle.FLOW);
            }
            this.yaml.serialize(mappingNode, writer);
        }
        
        return writer.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFromString(@NotNull final String data) throws InvalidConfigurationException {
        
        this.loaderOptions.setMaxAliasesForCollections(this.getOptions().getMaxAliases());
        this.loaderOptions.setCodePointLimit(this.getOptions().getCodePointLimit());
        this.loaderOptions.setProcessComments(this.getOptions().getParseComments());
        
        final MappingNode mappingNode;
        try (final Reader reader = new UnicodeReader(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)))) {
            mappingNode = (MappingNode) this.yaml.compose(reader);
        } catch (YAMLException | IOException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.", e);
        }
        
        this.map.clear();
        if (mappingNode != null) {
            
            this.adjustNodeComments(mappingNode);
            this.getOptions().setHeader(this.loadHeader(this.getCommentLines(mappingNode.getBlockComments())));
            this.getOptions().setFooter(this.getCommentLines(mappingNode.getEndComments()));
            this.fromNodeTree(mappingNode, this);
        }
    }
    
    /**
     * This splits the header on the last empty line, and sets the comments
     * below the given {@link MappingNode} line as comments for the first key on
     * the {@link Map}.
     * 
     * @param mappingNode The root {@link MappingNode} of the {@link Yaml}.
     */
    private void adjustNodeComments(@NotNull final MappingNode mappingNode) {
        
        if (mappingNode.getBlockComments() == null && !mappingNode.getValue().isEmpty()) {
            final Node node = mappingNode.getValue().get(0).getKeyNode();
            final List<CommentLine> comments = node.getBlockComments();
            if (comments != null) {
                int commentIndex = -1;
                for (int index = 0; index < comments.size(); index++) {
                    if (comments.get(index).getCommentType() == CommentType.BLANK_LINE) {
                        commentIndex = index;
                    }
                }
                if (commentIndex != -1) {
                    mappingNode.setBlockComments(comments.subList(0, commentIndex + 1));
                    node.setBlockComments(comments.subList(commentIndex + 1, comments.size()));
                }
            }
        }
    }
    
    /**
     * Fills the given {@link ConfigurationSection} with data, including
     * applicable children {@link ConfigurationSection ConfigurationSections},
     * from the data in the given {@link MappingNode}.
     * 
     * @param mappingNode The {@link MappingNode} containing the serialized
     *                    data.
     * @param section The {@link ConfigurationSection} to fill.
     */
    private void fromNodeTree(@NotNull final MappingNode mappingNode, @NotNull final ConfigurationSection section) {
        
        this.yamlConstructor.flattenMapping(mappingNode);
        for (final NodeTuple nodeTuple : mappingNode.getValue()) {
            
            final Node keyNode = nodeTuple.getKeyNode();
            final String key = String.valueOf(this.yamlConstructor.construct(keyNode));
            Node valueNode = nodeTuple.getValueNode();
            
            while (valueNode instanceof AnchorNode) {
                valueNode = ((AnchorNode) valueNode).getRealNode();
            }
            
            if (valueNode instanceof MappingNode && !this.hasSerializedTypeKey((MappingNode) valueNode)) {
                this.fromNodeTree((MappingNode) valueNode, section.createSection(key));
            } else {
                section.set(key, this.yamlConstructor.construct(valueNode));
            }
            
            section.setComments(key, this.getCommentLines(keyNode.getBlockComments()));
            if (valueNode instanceof MappingNode || valueNode instanceof SequenceNode) {
                section.setInLineComments(key, this.getCommentLines(keyNode.getInLineComments()));
            } else {
                section.setInLineComments(key, this.getCommentLines(valueNode.getInLineComments()));
            }
        }
    }
    
    /**
     * Checks if the given {@link MappingNode} contains a
     * {@link ConfigurationSerializable}, and returns appropriately.
     * 
     * @param mappingNode The {@link MappingNode} whose data will be checked.
     * @return {@code true} if the given {@link MappingNode} contains a
     *         {@link ConfigurationSerializable}, {@code false} otherwise.
     */
    private boolean hasSerializedTypeKey(@NotNull final MappingNode mappingNode) {
        
        for (final NodeTuple nodeTuple : mappingNode.getValue()) {
            final Node keyNode = nodeTuple.getKeyNode();
            if (!(keyNode instanceof ScalarNode)) {
                continue;
            }
            final String key = ((ScalarNode) keyNode).getValue();
            if (key.equals(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Creates a {@link MappingNode} containing a serialized representation of
     * the data in the given {@link ConfigurationSection}, and then returns the
     * {@link MappingNode}.
     * 
     * @param section The {@link ConfigurationSection} to serialize.
     * @return The serialized {@link ConfigurationSection} as a
     *         {@link MappingNode}.
     */
    @NotNull
    private MappingNode toNodeTree(@NotNull final ConfigurationSection section) {
        
        final List<NodeTuple> nodeTuples = new ArrayList<NodeTuple>();
        for (final Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            
            final Node keyNode = this.yamlRepresenter.represent(entry.getKey());
            final Node valueNode;
            if (entry.getValue() instanceof ConfigurationSection) {
                valueNode = this.toNodeTree((ConfigurationSection) entry.getValue());
            } else {
                valueNode = this.yamlRepresenter.represent(entry.getValue());
            }
            
            keyNode.setBlockComments(this.getCommentLines(section.getComments(entry.getKey()), CommentType.BLOCK));
            if (valueNode instanceof MappingNode || valueNode instanceof SequenceNode) {
                keyNode.setInLineComments(this.getCommentLines(section.getInLineComments(entry.getKey()), CommentType.IN_LINE));
            } else {
                valueNode.setInLineComments(this.getCommentLines(section.getInLineComments(entry.getKey()), CommentType.IN_LINE));
            }
            
            nodeTuples.add(new NodeTuple(keyNode, valueNode));
        }
        
        return new MappingNode(Tag.MAP, nodeTuples, DumperOptions.FlowStyle.BLOCK);
    }
    
    /**
     * Gets a {@link List} of {@link String Strings} that represent the given
     * {@link List} of {@link CommentLine CommentLines}.
     * 
     * @param commentLines The {@link List} of {@link CommentLine CommentLines}
     *                     to translate into {@link String Strings}.
     * @return A {@link List} of {@link String Strings} representing the given
     *         {@link CommentLine CommentLines}.
     */
    @NotNull
    private List<String> getCommentLines(@Nullable final List<CommentLine> commentLines) {
        
        final List<String> comments = new ArrayList<String>();
        if (commentLines == null) {
            return comments;
        }
    
        for (final CommentLine commentLine : commentLines) {
            if (commentLine.getCommentType() == CommentType.BLANK_LINE) {
                comments.add(null);
            } else {
                String comment = commentLine.getValue();
                comment = comment.startsWith(" ") ? comment.substring(1) : comment;
                comments.add(comment);
            }
        }
    
        return comments;
    }
    
    /**
     * Gets a {@link List} of {@link CommentLine CommentLines} that are
     * represented by the given {@link List} of {@link String Strings} and the
     * given {@link CommentType}.
     * 
     * @param comments The {@link List} of {@link String Strings} to translate
     *                 into {@link CommentLine CommentLines}.
     * @param commentType The type of {@link CommentLine CommentLines} to
     *                    translate into.
     * @return A {@link List} of {@link CommentLine CommentLines} that are
     *         represented by the given {@link List} of {@link String Strings}
     *         and {@link CommentType}.
     */
    @NotNull
    private List<CommentLine> getCommentLines(@NotNull final List<String> comments, @NotNull final CommentType commentType) {
        
        final List<CommentLine> commentLines = new ArrayList<CommentLine>();
        for (final String comment : comments) {
            if (comment == null) {
                commentLines.add(new CommentLine(null, null, "", CommentType.BLANK_LINE));
            } else {
                String line = comment;
                line = line.isEmpty() ? line : " " + line;
                commentLines.add(new CommentLine(null, null, line, commentType));
            }
        }
        
        return commentLines;
    }
    
    /**
     * This removes the empty line at the end of the header that separates the
     * header from further comments. Additionally, it removes any empty lines at
     * the start of the header.
     * 
     * @param formattedHeader The formatted {@link List} of header comments
     *                        (with the blank line).
     * @return The unformatted {@link List} of header comments (without the
     *         blank line).
     */
    @NotNull
    private List<String> loadHeader(@NotNull final List<String> formattedHeader) {
        
        final LinkedList<String> header = new LinkedList<String>(formattedHeader);
        if (!header.isEmpty()) {
            header.removeLast();
        }
        while (!header.isEmpty() && header.peek() == null) {
            header.remove();
        }
        return header;
    }
    
    /**
     * Adds an empty line at the end of the header to separate it from any
     * further comments.
     * 
     * @param header The unformatted {@link List} of header comments (without
     *               the blank line).
     * @return The formatted {@link List} of header comments (with the blank
     *         line).
     */
    @NotNull
    private List<String> saveHeader(@NotNull final List<String> header) {
        
        final LinkedList<String> formattedHeader = new LinkedList<String>(header);
        if (!formattedHeader.isEmpty()) {
            formattedHeader.add(null);
        }
        return formattedHeader;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public YamlConfigurationOptions getOptions() {
        if (this.options == null) {
            this.options = new YamlConfigurationOptions(this);
        }
        return (YamlConfigurationOptions) this.options;
    }
    
    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    @NotNull
    public YamlConfigurationOptions options() {
        return this.getOptions();
    }
    
    /**
     * Creates a new {@link YamlConfiguration}, loading from the given
     * {@link File}.
     * <p>
     * Any errors loading the {@link YamlConfiguration} will be logged and then
     * ignored. If the specified input is not a valid {@link YamlConfiguration},
     * a blank {@link YamlConfiguration} will be returned.
     * <p>
     * This will only load up to the default number of aliases
     * ({@link YamlConfigurationOptions#getMaxAliases()}) to prevent a Billion
     * Laughs Attack.
     * <p>
     * The encoding used may follow the system dependent default.
     *
     * @param file The {@link File} to load.
     * @return The loaded {@link YamlConfiguration}.
     */
    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final File file) {
        
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load config from file: " + file.getPath(), e);
        }
        
        return config;
    }
    
    /**
     * Creates a new {@link YamlConfiguration}, loading from the given
     * {@link Reader}.
     * <p>
     * Any errors loading the {@link YamlConfiguration} will be logged and then
     * ignored. If the specified input is not a valid {@link YamlConfiguration},
     * a blank {@link YamlConfiguration} will be returned.
     * <p>
     * This will only load up to the default number of aliases
     * ({@link YamlConfigurationOptions#getMaxAliases()}) to prevent a Billion
     * Laughs Attack.
     * <p>
     * The encoding used may follow the system dependent default.
     *
     * @param reader The {@link Reader} to load.
     * @return The loaded {@link YamlConfiguration}.
     */
    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final Reader reader) {
        
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load config from reader.", e);
        }
        
        return config;
    }
}
