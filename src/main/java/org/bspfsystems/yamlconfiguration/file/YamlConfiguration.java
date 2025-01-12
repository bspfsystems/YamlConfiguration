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
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
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
 * Represents an implementation of configuration which saves all files in valid
 * YAML format. Please note that this implementation is not synchronized.
 * <p>
 * Synchronized with the commit on 24-November-2024.
 */
public final class YamlConfiguration extends FileConfiguration {
    
    private final YamlConstructor yamlConstructor;
    private final YamlRepresenter yamlRepresenter;
    private final DumperOptions dumperOptions;
    private final LoaderOptions loaderOptions;
    private final Yaml yaml;
    
    /**
     * Constructs an empty YAML configuration with no default values.
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
     * Constructs an empty YAML configuration using the given configuration as a
     * source for all default values.
     *
     * @param defs The default value provider configuration
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
     * Creates a mapping node containing a serialized representation of the data
     * in the given configuration section, and then returns the newly-generated
     * mapping node.
     * 
     * @param section The configuration section to serialize.
     * @return The serialized configuration section as a mapping node.
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
                keyNode.setInLineComments(this.getCommentLines(section.getInlineComments(entry.getKey()), CommentType.IN_LINE));
            } else {
                valueNode.setInLineComments(this.getCommentLines(section.getInlineComments(entry.getKey()), CommentType.IN_LINE));
            }
            
            nodeTuples.add(new NodeTuple(keyNode, valueNode));
        }
        
        return new MappingNode(Tag.MAP, nodeTuples, DumperOptions.FlowStyle.BLOCK);
    }
    
    /**
     * Gets a list of comment lines that are represented by the given list of
     * strings and are of the given comment type.
     * 
     * @param comments The list of strings to convert to comment lines.
     * @param commentType The type of comments to translate to.
     * @return A list of comment lines of the given type translated from the
     *         given list of strings.
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
     * Adds an empty line at the end of the header to separate it from any
     * further comments.
     * 
     * @param header The unformatted list of header comments (without the blank
     *               line).
     * @return The formatted list of header comments (with the blank line).
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
    public void loadFromString(@NotNull final String data) throws InvalidConfigurationException {
        
        this.loaderOptions.setMaxAliasesForCollections(this.getOptions().getMaxAliases());
        this.loaderOptions.setCodePointLimit(this.getOptions().getCodePointLimit());
        this.loaderOptions.setProcessComments(this.getOptions().getParseComments());
        
        final MappingNode mappingNode;
        try (final Reader reader = new UnicodeReader(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)))) {
            final Node rawNode = this.yaml.compose(reader);
            try {
                mappingNode = (MappingNode) rawNode;
            } catch (final ClassCastException e) {
                throw new InvalidConfigurationException("Top level is not a Map.", e);
            }
        } catch (final YAMLException | IOException | ClassCastException e) {
            throw new InvalidConfigurationException(e);
        }
        
        this.clear();
        if (mappingNode != null) {
            
            this.adjustNodeComments(mappingNode);
            this.getOptions().setHeader(this.loadHeader(this.getCommentLines(mappingNode.getBlockComments())));
            this.getOptions().setFooter(this.getCommentLines(mappingNode.getEndComments()));
            this.fromNodeTree(mappingNode, this);
        }
    }
    
    /**
     * This splits the header comments on the last empty line, and sets the
     * comments below the given mapping node line as comments for the first key
     * in the map.
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
     * Gets a list of strings that represent the given list of comment lines.
     * 
     * @param commentLines The list of comment lines to translate into strings.
     * @return A list of strings representing the given list of comment lines.
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
     * This removes the empty line at the end of the header that separates the
     * header from further comments. Additionally, it removes any empty lines at
     * the start of the header.
     * 
     * @param formattedHeader The formatted list of header comments (with the
     *                        blank line).
     * @return The unformatted list of header comments (without the blank
     *         line).
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
     * Fills the given configuration section with data, including applicable
     * children configuration sections, from the data in the given mapping node.
     * 
     * @param mappingNode The mapping node containing the serialized data.
     * @param section The configuration section to fill.
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
                section.setInlineComments(key, this.getCommentLines(keyNode.getInLineComments()));
            } else {
                section.setInlineComments(key, this.getCommentLines(valueNode.getInLineComments()));
            }
        }
    }
    
    /**
     * Checks if the given mapping node contains a configuration serializable,
     * and returns appropriately.
     * 
     * @param mappingNode The mapping node whose data will be checked.
     * @return {@code true} if the given mapping node contains a configuration
     *         serializable, {@code false} otherwise.
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
     * Creates a new YAML configuration, loading from the given file.
     * <p>
     * Any errors loading the YAML configuration will be logged and then
     * otherwise ignored. If the given input is not a valid YAML configuration,
     * an empty YAML configuration will be returned.
     * <p>
     * This will only load up to the default number of aliases
     * ({@link YamlConfigurationOptions#getMaxAliases()}) to prevent a Billion
     * Laughs Attack.
     * <p>
     * The encoding used may follow the system dependent default.
     * 
     * @param file The file to load.
     * @return The loaded YAML configuration.
     */
    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final File file) {
        
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            LoggerFactory.getLogger(YamlConfiguration.class).error("Cannot load config from file: " + file.getPath(), e);
        }
        
        return config;
    }
    
    /**
     * Creates a new YAML configuration, loading from the given reader.
     * <p>
     * Any errors loading the YAML configuration will be logged and then
     * otherwise ignored. If the given input is not a valid YAML configuration,
     * an empty YAML configuration will be returned.
     * <p>
     * This will only load up to the default number of aliases
     * ({@link YamlConfigurationOptions#getMaxAliases()}) to prevent a Billion
     * Laughs Attack.
     * <p>
     * The encoding used may follow the system dependent default.
     * 
     * @param reader The reader to load.
     * @return The loaded YAML configuration.
     */
    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final Reader reader) {
        
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            LoggerFactory.getLogger(YamlConfiguration.class).error("Cannot load config from reader.", e);
        }
        
        return config;
    }
}
