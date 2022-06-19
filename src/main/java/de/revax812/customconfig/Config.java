/*
 * Project: CustomConfig
 * Author:  Revax812
 * Version: 1.0
 * Last Change:
 *    by:   Revax812
 *    date: 19.06.2022, 11:15
 * Copyright (c): Revax812, 2022
 */

package de.revax812.customconfig;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.plugin.Plugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.Reader;
import java.io.IOException;
import java.util.*;

/**
 * Allows to create fully customizable custom configs for Minecraft Spigot.<br>
 * Bugs: none known
 * <br><br>
 * Copyright (c): Revax812, 2022<br>
 *
 * @author Revax812 <br>
 * @version 1.0
 */

public class Config {

    /**
     * The public {@link File} config file
     */
    public File file;
    /**
     * The private {@link File} file path or parentFile
     */
    private File filePath;
    /**
     * The private {@link String} pathName
     */
    private String pathName;
    /**
     * The private {@link YamlConfiguration} config
     */
    private YamlConfiguration config;
    /**
     * The private {@link Boolean} useCustomPath
     */
    private boolean useCustomPath = false;
    /**
     * The final {@link Plugin} plugin
     */
    private final Plugin plugin;
    /**
     * The final {@link String} filename
     */
    private final String filename;

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin and {@link String} name.
     * <br><br>
     * Redirects to {@link #Config(Plugin, String, File)}.<br>
     *
     * @param plugin  represents the {@link Plugin} plugin like the Main class<br>
     * @param name    represents the {@link String} name of the config<br>
     * @see           #Config(Plugin, String, File)
     */
    public Config(Plugin plugin, String name) {
        this(plugin, name, plugin.getDataFolder());
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} name and {@link Boolean} copyDefaults.
     * <br><br>
     * Redirects to {@link #Config(Plugin, String, File, boolean)}.<br>
     *
     * @param plugin        represents the {@link Plugin} plugin like the Main class<br>
     * @param name          represents the {@link String} name of the config<br>
     * @param copyDefaults  specifies whether the config should save a pre-saved file<br>
     * @see                 #Config(Plugin, String, File, boolean)
     */
    public Config(Plugin plugin, String name, boolean copyDefaults) {
        this(plugin, name, plugin.getDataFolder(), copyDefaults);
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} name, {@link Boolean} copyDefaults and {@link Boolean} replace.
     * <br><br>
     * Redirects to {@link #Config(Plugin, String, File, boolean, boolean)}.<br>
     *
     * @param plugin        represents the {@link Plugin} plugin like the Main class<br>
     * @param name          represents the {@link String} name of the config<br>
     * @param copyDefaults  specifies whether the config should save a pre-saved file<br>
     * @param replace       specifies whether the config should be fully replaced by the pre-saved file<br>
     * @see                 #Config(Plugin, String, File, boolean, boolean)
     */
    public Config(Plugin plugin, String name, boolean copyDefaults, boolean replace) {
        this(plugin, name, plugin.getDataFolder(), copyDefaults, replace);
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} name and {@link File} parent.
     * <br><br>
     * First, a new {@link File} is created and it is checked whether the {@link File} filePath already exists.
     * If this is not the case, it will be created.<br>
     * Then the same process with the {@link File} config-file takes place.<br>
     * Finally, the created {@link Config} config is loaded.<br>
     *
     * @param plugin  represents the {@link Plugin} plugin like the Main class<br>
     * @param name    represents the {@link String} name of the config<br>
     * @param parent  represents the {@link File} filePath of the config
     */
    public Config(Plugin plugin, String name, File parent) {
        this.plugin = plugin;
        this.filename = name;
        filePath = parent;
        file = new File(filePath, name);

        if (!filePath.exists()) filePath.mkdirs();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} name, {@link File} parent and {@link Boolean} copyDefaults.
     * <br><br>
     * Redirects to {@link #Config(Plugin, String, File, boolean, boolean)}.<br>
     *
     * @param plugin        represents the {@link Plugin} plugin like the Main class<br>
     * @param name          represents the {@link String} name of the config<br>
     * @param parent        represents the {@link File} filePath of the config
     * @param copyDefaults  specifies whether the config should save a pre-saved file<br>
     * @see                 #Config(Plugin, String, File, boolean, boolean)
     */
    public Config(Plugin plugin, String name, File parent, boolean copyDefaults) {
        this(plugin, name, parent, copyDefaults, false);
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} name, {@link File} parent,
     * {@link Boolean} copyDefaults and {@link Boolean} replace.
     * <br><br>
     * First, a new {@link File} is created and it is checked whether the {@link File} filePath already exists.<br>
     * If this is not the case, it will be created.<br>
     * After that, it is checked the value of {@link Boolean} replace and if {@link Boolean} copyDefaults is true.<br>
     * If that is the case, the pre-saved file gets copied to the config file.<br>
     * If {@link Boolean} replace is true, the old config file gets completely replaced.<br>
     * If {@link Boolean} copyDefaults is false, the same process like in<br>{@link #Config(Plugin, String, File)} takes place with the
     * {@link File} config-file.<br>
     * Finally, the created {@link Config} config is loaded.<br>
     *
     * @param plugin        represents the {@link Plugin} plugin like the Main class<br>
     * @param name          represents the {@link String} name of the config<br>
     * @param parent        represents the {@link File} filePath of the config<br>
     * @param copyDefaults  specifies whether the config should save a pre-saved file<br>
     * @param replace       specifies whether the config should be fully replaced by the pre-saved file
     */
    public Config(Plugin plugin, String name, File parent, boolean copyDefaults, boolean replace) {
        this.plugin = plugin;
        this.filename = name;
        filePath = parent;
        file = new File(filePath, name);

        if (!filePath.exists()) filePath.mkdirs();

        if (copyDefaults) {
            if (!file.exists()) this.plugin.saveResource(name, false);
            else if (replace) this.plugin.saveResource(name, true);
        } else {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} pathName and {@link String} name.
     * <br><br>
     * First, a new {@link File} is created out of {@link String} pathName and {@link String} name.<br>
     * After that, the same process like in<br>{@link #Config(Plugin, String, File)} takes place.<br>
     *
     * @param plugin    represents the {@link Plugin} plugin like the Main class<br>
     * @param pathName  represents the {@link File} filePath of the config<br>
     * @param name      represents the {@link String} name of the config<br>
     * @see             #Config(Plugin, String, File)
     */
    public Config(Plugin plugin, String pathName, String name) {
        this.plugin = plugin;
        this.pathName = pathName;
        this.filename = name;
        filePath = new File(plugin.getDataFolder(), pathName);
        useCustomPath = true;
        file = new File(filePath, name);

        if (!filePath.exists()) filePath.mkdirs();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} pathName, {@link String} name and {@link Boolean} copyDefaults.
     * <br><br>
     * Redirects to {@link #Config(Plugin, String, String, boolean, boolean)}.<br>
     *
     * @param plugin        represents the {@link Plugin} plugin like the Main class<br>
     * @param pathName      represents the {@link File} filePath of the config<br>
     * @param name          represents the {@link String} name of the config<br>
     * @param copyDefaults  specifies whether the config should save a pre-saved file<br>
     * @see                 #Config(Plugin, String, String, boolean, boolean)
     */
    public Config(Plugin plugin, String pathName, String name, boolean copyDefaults) {
        this(plugin, pathName, name, copyDefaults, false);
    }

    /**
     * Creates a new {@link Config} config with {@link Plugin} plugin, {@link String} pathName, {@link String} name,
     * {@link Boolean} copyDefaults and {@link Boolean} replace.
     * <br><br>
     * First, a new {@link File} is created out of {@link String} pathName and {@link String} name.<br>
     * After that, the same process like in<br>{@link #Config(Plugin, String, File, boolean, boolean)} takes place.<br>
     *
     * @param plugin    represents the {@link Plugin} plugin like the Main class<br>
     * @param pathName  represents the {@link File} filePath of the config<br>
     * @param name      represents the {@link String} name of the config<br>
     * @see             #Config(Plugin, String, File, boolean, boolean)
     */
    public Config(Plugin plugin, String pathName, String name, boolean copyDefaults, boolean replace) {
        this.plugin = plugin;
        this.pathName = pathName;
        this.filename = name;
        filePath = new File(plugin.getDataFolder(), pathName);
        useCustomPath = true;
        file = new File(filePath, name);

        if (!filePath.exists()) filePath.mkdirs();

        if (copyDefaults) {
            if (!file.exists()) this.plugin.saveResource(pathName + name, false);
            else if (replace) this.plugin.saveResource(pathName + name, true);
        } else {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    /**
     * Reloads the {@link Config} config.
     * <br><br>
     * First, it is checked if filePath and file exist, and if not they are created.<br>
     * After that, the config is loaded again.<br>
     *
     * @return the {@link YamlConfiguration} config
     */
    public YamlConfiguration reload() {
        if (filePath == null && useCustomPath) filePath = new File(plugin.getDataFolder(), pathName);
        else if (filePath == null) filePath = new File(plugin.getDataFolder(), plugin.getDataFolder().getName());
        if (file == null) file = new File(filePath, filename);
        config = YamlConfiguration.loadConfiguration(file);
        save();
        return config;
    }

    /**
     * Loads the {@link Config} config with {@link File} file.
     * <br><br>
     *
     * @param file represents the {@link File} file that should be loaded
     */
    public void load(File file) {
        try {
            config.load(file);
            save();
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the {@link Config} config with {@link String} file.
     * <br><br>
     *
     * @param file represents the {@link String} file that should be loaded
     */
    public void load(String file) {
        try {
            config.load(file);
            save();
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the {@link Config} config with {@link Reader} reader.
     * <br><br>
     *
     * @param reader represents the {@link Reader} reader that should be loaded
     */
    public void load(Reader reader) {
        try {
            config.load(reader);
            save();
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the {@link Config} config with {@link File} file.
     * <br><br>
     *
     * @param file represents the {@link File} file that should be loaded<br>
     * @return the {@link YamlConfiguration} config
     */
    public YamlConfiguration loadConfiguration(File file) {
        config = YamlConfiguration.loadConfiguration(file);
        save();
        return config;
    }

    /**
     * Loads the {@link Config} config with {@link Reader} reader.
     * <br><br>
     *
     * @param reader represents the {@link Reader} reader that should be loaded<br>
     * @return the {@link YamlConfiguration} config
     */
    public YamlConfiguration loadConfiguration(Reader reader) {
        config = YamlConfiguration.loadConfiguration(reader);
        save();
        return config;
    }

    /**
     * Loads from {@link String} string.
     * <br><br>
     *
     * @param contents represents the {@link String} contents that should be loaded
     */
    public void loadFromString(String contents) {
        try {
            config.loadFromString(contents);
            save();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves to {@link String} string.
     * <br><br>
     *
     * @return the {@link String} string
     */
    public String saveToString() {
        String string = config.saveToString();
        save();
        return string;
    }

    /**
     * Saves the {@link Config} config.
     * <br><br>
     * Saves the {@link File} config-file.
     */
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the {@link Config} config with {@link File} file.
     * <br><br>
     *
     * @param file represents the {@link File} config-file that should be saved
     */
    public void save(File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the {@link Config} config with {@link String} file.
     * <br><br>
     *
     * @param file represents the {@link String} file that should be saved
     */
    public void save(String file) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the defaults for the {@link Config} config.
     * <br><br>
     * Redirects to {@link #setDefaults(boolean)}
     */
    public void setDefaults() {
        setDefaults(false);
    }

    /**
     * Sets the defaults for the {@link Config} config.
     * <br><br>
     * First, it is checked if filePath and file exist and if not, they will be created after setting the defaults.<br>
     *
     * @param replace specifies whether the config should be fully replaced by the pre-saved file
     */
    public void setDefaults(boolean replace) {
        if (!filePath.exists()) filePath.mkdirs();
        if (pathName != null) {
            if (!file.exists()) this.plugin.saveResource(pathName + filename, false);
            else if (replace) this.plugin.saveResource(pathName + filename, true);
        } else {
            if (!file.exists()) this.plugin.saveResource(filename, false);
            else if (replace) this.plugin.saveResource(filename, true);
        }
        save();
    }

    /**
     * Sets the defaults for the {@link Config} config.
     * <br><br>
     *
     * @param defaults represents the {@link Configuration} configuration that should be set
     */
    public void setDefaults(Configuration defaults) {
        config.setDefaults(defaults);
        save();
    }

    /**
     * Adds defaults to the {@link Config} config.
     * <br><br>
     *
     * @param path  represents the {@link String} path of the defaults that should be added<br>
     * @param value represents the {@link Object} value that should be added
     */
    public void addDefault(String path, Object value) {
        config.addDefault(path, value);
        save();
    }

    /**
     * Adds defaults to the {@link Config} config.
     * <br><br>
     *
     * @param defaults represents the {@link Map}<{@link String}, {@link Object}> defaults that should be added
     */
    public void addDefaults(Map<String, Object> defaults) {
        config.addDefaults(defaults);
        save();
    }

    /**
     * Adds defaults to the {@link Config} config.
     * <br><br>
     *
     * @param defaults represents the {@link Configuration} defaults that should be added
     */
    public void addDefaults(Configuration defaults) {
        config.addDefaults(defaults);
        save();
    }

    /**
     * Gets the {@link Configuration} defaults.
     * <br><br>
     *
     * @return  the {@link Configuration} defaults
     */
    public Configuration getDefaults() {
        return config.getDefaults();
    }

    /**
     * Sets if the configuration should copy values from its default Configuration directly.
     * <br><br>
     *
     * @param value whether or not defaults are directly copied<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions copyDefaults(boolean value) {
        YamlConfigurationOptions options = config.options().copyDefaults(value);
        save();
        return options;
    }

    /**
     * Gets the {@link Boolean} copyDefault.
     * <br><br>
     * Redirects to {@link #copyDefaults()}.<br>
     *
     * @return  the {@link Boolean} copyDefault
     */
    public Boolean getCopyDefaults() {
        return copyDefaults();
    }

    /**
     * Gets the value of copyDefaults.
     * <br><br>
     *
     * @return whether or not defaults are directly copied
     */
    public Boolean copyDefaults() {
        return config.options().copyDefaults();
    }

    /**
     * Sets a specific header to the {@link Config} config.
     * <br><br>
     *
     * @param header represents the {@link String} header that should be set<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    @Deprecated
    public YamlConfigurationOptions setHeader(String header) {
        YamlConfigurationOptions options = config.options().header(header);
        save();
        return options;
    }

    /**
     * Sets a specific header to the {@link Config} config.
     * <br><br>
     *
     * @param value represents the {@link List<String>} value that should be set<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions setHeader(List<String> value) {
        YamlConfigurationOptions options = config.options().setHeader(value);
        save();
        return options;
    }

    /**
     * Sets a specific header to the {@link Config} config.
     * <br><br>
     *
     * @param values represents the {@link String}... values that should be set<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions setHeader(String... values) {
        List<String> headerValues = new ArrayList<>();
        Collections.addAll(headerValues, values);
        YamlConfigurationOptions options = config.options().setHeader(headerValues);
        save();
        return options;
    }

    /**
     * Gets the {@link String} header.
     * <br><br>
     *
     * @return  the {@link String} header
     */
    @Deprecated
    public String getHeader() {
        return config.options().header();
    }

    /**
     * Gets the {@link String} header.
     * <br><br>
     *
     * @return  the {@link String} header
     */
    @Deprecated
    public String header() {
        return config.options().header();
    }

    /**
     * Gets the {@link YamlConfigurationOptions} header.
     * <br><br>
     *
     * @param value  represents the {@link String} value that is returned by default<br>
     * @return       the {@link YamlConfigurationOptions} options
     */
    @Deprecated
    public YamlConfigurationOptions header(String value) {
        return config.options().header(value);
    }

    /**
     * Sets if the configuration should parse the header.
     * <br><br>
     *
     * @param value whether or not comments should parsed<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    @Deprecated
    public YamlConfigurationOptions copyHeader(boolean value) {
        YamlConfigurationOptions options = config.options().copyHeader(value);
        save();
        return options;
    }

    /**
     * Gets the {@link Boolean} copyHeader.
     * <br><br>
     * Redirects to {@link #copyHeader()}.<br>
     *
     * @return  whether or not comments are parsed
     */
    @Deprecated
    public Boolean getCopyHeader() {
        return copyHeader();
    }

    /**
     * Gets whether or not comments should be loaded and saved.<br>
     * Default is true.
     * <br><br>
     *
     * @return whether or not comments are parsed.
     */
    @Deprecated
    public Boolean copyHeader() {
        return config.options().copyHeader();
    }

    /**
     * Sets a specific footer to the {@link Config} config.
     * <br><br>
     *
     * @param value represents the {@link List<String>} value that should be set<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions setFooter(List<String> value) {
        YamlConfigurationOptions options = config.options().setFooter(value);
        save();
        return options;
    }

    /**
     * Sets a specific footer to the {@link Config} config.
     * <br><br>
     *
     * @param values represents the {@link String}... values that should be set<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions setFooter(String... values) {
        List<String> footerValues = new ArrayList<>();
        Collections.addAll(footerValues, values);
        YamlConfigurationOptions options = config.options().setFooter(footerValues);
        save();
        return options;
    }

    /**
     * Gets the {@link List<String>} footer.
     * <br><br>
     *
     * @return  the {@link List<String>} footer
     */
    public List<String> getFooter() {
        return config.options().getFooter();
    }

    /**
     * Sets comments to a path of the {@link Config} config.
     * <br><br>
     *
     * @param path     represents the {@link String} path that should be selected<br>
     * @param comments represents the {@link List<String>} comments that should be set
     */
    public void setComments(String path, List<String> comments) {
        config.setComments(path, comments);
        save();
    }

    /**
     * Sets comments to a path of the {@link Config} config.
     * <br><br>
     *
     * @param path      represents the {@link String} path that should be selected<br>
     * @param comments  represents the {@link String}... comments that should be set
     */
    public void setComments(String path, String... comments) {
        List<String> commentList = new ArrayList<>();
        Collections.addAll(commentList, comments);
        config.setComments(path, commentList);
        save();
    }

    /**
     * Gets the requested {@link List<String>} comment list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<String>} comment list
     */
    public List<String> getComments(String path) {
        return !contains(path) ? null : config.getComments(path);
    }

    /**
     * Sets inlineComments to a path of the {@link Config} config.
     * <br><br>
     *
     * @param path     represents the {@link String} path that should be selected<br>
     * @param comments represents the {@link List<String>} inlineComments that should be set
     */
    public void setInlineComments(String path, List<String> comments) {
        config.setInlineComments(path, comments);
        save();
    }

    /**
     * Sets inlineComments to a path of the {@link Config} config.
     * <br><br>
     *
     * @param path      represents the {@link String} path that should be selected<br>
     * @param comments  represents the {@link String}... inlineComments that should be set
     */
    public void setInlineComments(String path, String... comments) {
        List<String> commentList = new ArrayList<>();
        Collections.addAll(commentList, comments);
        config.setInlineComments(path, commentList);
        save();
    }

    /**
     * Gets the requested {@link List<String>} inlineComment list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<String>} inlineComment list
     */
    public List<String> getInlineComments(String path) {
        return !contains(path) ? null : config.getInlineComments(path);
    }

    /**
     * Sets whether or not {@link List<String>} comments should be loaded and saved.<br>
     * Defaults is true.
     * <br><br>
     * Redirects to {@link #parseComments(boolean)}.<br>
     *
     * @param value  represents the {@link Boolean} value that is returned by default<br>
     * @return       the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions setParseComments(boolean value) {
        return parseComments(value);
    }

    /**
     * Sets whether or not {@link List<String>} comments should be loaded and saved.<br>
     * Defaults is true.
     * <br><br>
     *
     * @param value  represents the {@link Boolean} value that is returned by default<br>
     * @return  the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions parseComments(boolean value) {
        YamlConfigurationOptions options = config.options().parseComments(value);
        save();
        return options;
    }

    /**
     * Gets the {@link Boolean} parseComments.<br>
     * Default is true.
     * <br><br>
     * Redirects to {@link #parseComments()}.<br>
     *
     * @return  whether or not comments are parsed
     */
    public Boolean getParseComments() {
        return parseComments();
    }

    /**
     * Gets the {@link Boolean} parseComments.<br>
     * Default is true.
     * <br><br>
     *
     * @return  whether or not comments are parsed
     */
    public Boolean parseComments() {
        return config.options().parseComments();
    }

    /**
     * Sets how much spaces should be used to indent each line.<br>
     * The minimum value this may be is 2, and the maximum is 9.
     * <br><br>
     *
     * @param indent represents the new {@link int} indent<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions indent(int indent) {
        YamlConfigurationOptions options = config.options().indent(indent);
        save();
        return options;
    }

    /**
     * Gets the {@link Integer} indent.
     * <br><br>
     *
     * @return  the {@link Integer} indent
     */
    public int getIndent() {
        return config.options().indent();
    }

    /**
     * Sets how long the line can be before it gets split.
     * <br><br>
     *
     * @param width represents the new {@link int} width
     */
    public YamlConfigurationOptions setWidth(int width) {
        YamlConfigurationOptions options = config.options().width(width);
        save();
        return options;
    }

    /**
     * Gets the {@link Integer} width.
     * <br><br>
     *
     * @return  the {@link Integer} width
     */
    public int getWidth() {
        return config.options().width();
    }

    /**
     * Sets the char that will be used to separate ConfigurationSections.<br>
     * This value does not affect how the Configuration is stored, only in how you access the data.<br>
     * The default value is '.'.
     * <br><br>
     *
     * @param separator represents the new {@link char} separator<br>
     * @return the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions setPathSeparator(char separator) {
        YamlConfigurationOptions options = config.options().pathSeparator(separator);
        save();
        return options;
    }

    /**
     * Gets the {@link Character} pathSeparator.
     * <br><br>
     *
     * @return  the {@link Character} pathSeparator
     */
    public char getPathSeparator() {
        return config.options().pathSeparator();
    }

    /**
     * Clears the whole {@link Config} config.
     * <br><br>
     * Redirects to {@link #clear(boolean)}.<br>
     *
     * @see #clear(boolean)
     */
    public void clear() {
        clear(true);
    }

    /**
     * Clears the whole {@link Config} config.<br>
     * If deep is set to false, then this will contain only the keys and values of any direct children, and not their own children.
     * <br><br>
     *
     * @param deep whether or not to get a deep list, as opposed to a shallow list.
     */
    public void clear(boolean deep) {
        Map<String, Object> configValues = config.getValues(deep);
        for (Map.Entry<String, Object> entry : configValues.entrySet()) {
            config.set(entry.getKey(), null);
        }
        save();
    }

    /**
     * Clears a specific path of the {@link Config} config.
     * <br><br>
     *
     * @param path represents the path that should be cleared
     */
    public void clearPath(String path) {
        config.set(path, null);
        save();
    }

    /**
     * Creates a path.
     * <br><br>
     *
     * @param section   represents the {@link ConfigurationSection} section to create a path for<br>
     * @param pathName  represents the {@link String} pathName that should be set<br>
     * @return          the {@link String} string
     */
    public String createPath(ConfigurationSection section, String pathName) {
        String string = MemorySection.createPath(section, pathName);
        save();
        return string;
    }

    /**
     * Creates a path.
     * <br><br>
     *
     * @param section     represents the {@link ConfigurationSection} section to create a path for<br>
     * @param pathName    represents the {@link String} pathName that should be set<br>
     * @param relativeTo  represents the {@link ConfigurationSection} to create the path relative to<br>
     * @return            the {@link String} string
     */
    public String createPath(ConfigurationSection section, String pathName, ConfigurationSection relativeTo) {
        String string = MemorySection.createPath(section, pathName, relativeTo);
        save();
        return string;
    }

    /**
     * Gets the {@link String} currentPath.
     * <br><br>
     *
     * @return  the {@link String} currentPath
     */
    public String getCurrentPath() {
        return config.getCurrentPath();
    }

    /**
     * Checks if the specified {@link String} path has a value.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Boolean} hasValue
     */
    public Boolean isSet(String path) {
        return config.isSet(path);
    }

    /**
     * Checks if the specified {@link String} path is a String.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified {@link String} path is a {@link String}
     */
    public Boolean isString(String path) {
        return config.isString(path);
    }

    /**
     * Gets the requested {@link String} string by path.
     * <br><br>
     * Redirects to {@link #getString(String)}.<br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link String} string
     */
    public String getPath(String path) {
        return getString(path);
    }

    /**
     * Gets the requested {@link String} string by path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link String} string
     */
    public String getString(String path) {
        return !contains(path) ? null : config.getString(path);
    }

    /**
     * Gets the requested {@link String} string by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param string  represents the {@link String} string that is returned by default<br>
     * @return       the {@link String} string
     */
    public String getString(String path, String string) {
        return !contains(path) ? null : config.getString(path, string);
    }

    /**
     * Gets the requested {@link List<String>} string list by path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<String>} string list
     */
    public List<String> getStringList(String path) {
        return !contains(path) ? null : config.getStringList(path);
    }

    /**
     * Creates a section in the {@link Config} config.
     * <br><br>
     *
     * @param path   represents the {@link String} path that should be selected<br>
     * @return       the {@link ConfigurationSection} section
     */
    public ConfigurationSection createSection(String path) {
        ConfigurationSection section = config.createSection(path);
        save();
        return section;
    }

    /**
     * Creates a section in the {@link Config} config.
     * <br><br>
     *
     * @param path    represents the {@link String} path that should be selected<br>
     * @param values  represents the {@link Map}<?, ?> values that should be set<br>
     * @return        the {@link ConfigurationSection} section
     */
    public ConfigurationSection createSection(String path, Map<?, ?> values) {
        ConfigurationSection section = config.createSection(path, values);
        save();
        return section;
    }

    /**
     * Checks if the specified {@link String} path is a {@link ConfigurationSection} section.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link ConfigurationSection} section
     */
    public Boolean isConfigurationSection(String path) {
        return config.isConfigurationSection(path);
    }

    /**
     * Gets the requested {@link ConfigurationSection} sectiom by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link ConfigurationSection} section
     */
    public ConfigurationSection getConfigurationSection(String path) {
        return !contains(path) ? null : config.getConfigurationSection(path);
    }

    /**
     * Gets the {@link ConfigurationSection} defaultSection.
     * <br><br>
     *
     * @return  the {@link ConfigurationSection} defaultSection
     */
    public ConfigurationSection getDefaultSection() {
        return config.getDefaultSection();
    }

    /**
     * Gets the {@link ConfigurationSection} parent.
     * <br><br>
     *
     * @return  the {@link ConfigurationSection} parent
     */
    public ConfigurationSection getParent() {
        return config.getParent();
    }

    /**
     * Sets a value to a path of the {@link Config} config.
     * <br><br>
     *
     * @param path  represents the {@link String} path that should be selected<br>
     * @param value represents the {@link Object} value that should be set
     */
    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    //Getter
    /**
     * Checks if the {@link Config} config contains the given path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that should be checked<br>
     * @return      true if this section contains the requested path
     */
    public Boolean contains(String path) {
        return config.contains(path);
    }

    /**
     * Checks if the {@link Config} config contains the given path.
     * <br><br>
     *
     * @param path           represents the {@link String} path that should be checked<br>
     * @param ignoreDefault  represents the {@link Boolean} whether or not to ignore if a default value for the specified path exists<br>
     * @return               true if this section contains the requested path
     */
    public Boolean contains(String path, boolean ignoreDefault) {
        return config.contains(path, ignoreDefault);
    }

    /**
     * Gets the requested {@link Object} object by {@link String} path out of the {@link Config} config.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Object} object
     */
    public Object get(String path) {
        return !contains(path) ? null : config.get(path);
    }

    /**
     * Gets the requested {@link Object} value by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Object} value that is returned by default<br>
     * @return       the {@link Object} object
     */
    public Object get(String path, Object value) {
        return !contains(path) ? null : config.get(path, value);
    }

    /**
     * Gets the requested {@link Object} object by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Object} value that is returned by default<br>
     * @return       the {@link Object} object
     */
    public Object getObject(String path, Class<Object> value) {
        return !contains(path) ? null : config.getObject(path, value);
    }

    /**
     * Gets the requested {@link Object} object by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param type   represents the {@link Object} type that is selected for the config<br>
     * @param def    represents the {@link Object} def that is returned by default<br>
     * @return       the {@link Object} object
     */
    public <T> T getObject(String path, Class<T> type, T def) {
        return !contains(path) ? null : config.getObject(path, type, def);
    }

    /**
     * Gets the  {@link Set<String>} keys of the {@link Config} config.
     * <br><br>
     * Redirects to {@link #getKeys(boolean)}.<br>
     *
     * @return  the {@link Set<String>} string set
     */
    public Set<String> getKeys() {
        return getKeys(true);
    }

    /**
     * Gets the  {@link Set<String>} keys of the {@link Config} config.
     * <br><br>
     *
     * @param deep  whether or not to get a deep list, as opposed to a shallow list<br>
     * @return      the {@link Set<String>} string set
     */
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    /**
     * Gets the requested {@link ConfigurationSerializable} serializable by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param type   represents the {@link ConfigurationSerializable} type<br>
     * @return       the {@link ConfigurationSerializable} serializable
     */
    public ConfigurationSerializable getSerializable(String path, Class<ConfigurationSerializable> type) {
        return !contains(path) ? null : config.getSerializable(path, type);
    }

    /**
     * Gets the requested {@link ConfigurationSerializable} serializable by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param type   represents the {@link ConfigurationSerializable} type<br>
     * @param def    represents the {@link T} def that is returned by default<br>
     * @return       the {@link ConfigurationSerializable} serializable
     */
    public <T extends ConfigurationSerializable> T getSerializable(String path, Class<T> type, T def) {
        return !contains(path) ? null : config.getSerializable(path, type, def);
    }

    /**
     * Gets the values of {@link Map}<{@link String}, {@link Object}> map.
     * <br><br>
     * Redirects to {@link #getValues(boolean)}.<br>
     *
     * @return  the {@link Map}<{@link String}, {@link Object}> map
     */
    public Map<String, Object> getValues() {
        return getValues(true);
    }

    /**
     * Gets the values of {@link Map}<{@link String}, {@link Object}> map.
     * <br><br>
     *
     * @param deep  whether or not to get a deep list, as opposed to a shallow list<br>
     * @return      the {@link Map}<{@link String}, {@link Object}> map
     */
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    /**
     * Checks if the specified path is a {@link Boolean} boolean.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Boolean} boolean
     */
    public Boolean isBoolean(String path) {
        return config.isBoolean(path);
    }

    /**
     * Gets the requested {@link Boolean} boolean by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Boolean} boolean
     */
    public Boolean getBoolean(String path) {
        return !contains(path) ? null : config.getBoolean(path);
    }

    /**
     * Gets the requested {@link Boolean} boolean by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Boolean} value that is returned by default<br>
     * @return       the {@link Boolean} boolean
     */
    public Boolean getBoolean(String path, boolean value) {
        return !contains(path) ? null : config.getBoolean(path, value);
    }

    /**
     * Gets the requested {@link List<Boolean>} boolean list by path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Boolean>} boolean list
     */
    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    /**
     * Checks if the specified {@link String} path is a {@link Integer} integer.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Integer} integer
     */
    public Boolean isInt(String path) {
        return config.isInt(path);
    }

    /**
     * Gets the requested {@link Integer} integer by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Integer} integer
     */
    public Integer getInt(String path) {
        return !contains(path) ? null : config.getInt(path);
    }

    /**
     * Gets the requested {@link Integer} integer by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Integer} value that is returned by default<br>
     * @return       the {@link Integer} integer
     */
    public Integer getInt(String path, int value) {
        return !contains(path) ? null : config.getInt(path, value);
    }

    /**
     * Gets the requested {@link List<Integer>} integer list by path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Integer>} integer list
     */
    public List<Integer> getIntegerList(String path) {
        return !contains(path) ? null : config.getIntegerList(path);
    }

    /**
     * Checks if the specified {@link String} path is a {@link Long} long.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Long} long
     */
    public Boolean isLong(String path) {
        return config.isLong(path);
    }

    /**
     * Gets the requested {@link Long} long by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Long} long
     */
    public Long getLong(String path) {
        return !contains(path) ? null : config.getLong(path);
    }

    /**
     * Gets the requested {@link Long} long by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Long} value that is returned by default<br>
     * @return       the {@link Long} long
     */
    public Long getLong(String path, long value) {
        return !contains(path) ? null : config.getLong(path, value);
    }

    /**
     * Gets the requested {@link List<Long>} long list by path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Long>} long list
     */
    public List<Long> getLongList(String path) {
        return !contains(path) ? null : config.getLongList(path);
    }

    /**
     * Checks if the specified {@link String} path is a {@link Double} double.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Double} double
     */
    public Boolean isDouble(String path) {
        return config.isDouble(path);
    }

    /**
     * Gets the requested {@link Double} double by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Double} double
     */
    public Double getDouble(String path) {
        return !contains(path) ? null : config.getDouble(path);
    }

    /**
     * Gets the requested {@link Double} double by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Double} value that is returned by default<br>
     * @return       the {@link Double} double
     */
    public Double getDouble(String path, double value) {
        return !contains(path) ? null : config.getDouble(path, value);
    }

    /**
     * Gets the requested {@link List<Double>} double list by path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Double>} double list
     */
    public List<Double> getDoubleList(String path) {
        return !contains(path) ? null : config.getDoubleList(path);
    }

    /**
     * Gets the requested {@link List<Float>} float list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Float>} float list
     */
    public List<Float> getFloatList(String path) {
        return !contains(path) ? null : config.getFloatList(path);
    }

    /**
     * Gets the requested {@link List<Short>} short list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Short>} short list
     */
    public List<Short> getShortList(String path) {
        return !contains(path) ? null : config.getShortList(path);
    }

    /**
     * Gets the requested {@link List<Byte>} byte list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Byte>} byte list
     */
    public List<Byte> getByteList(String path) {
        return config.getByteList(path);
    }

    /**
     * Gets the requested {@link List<Character>} character list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Character>} character list
     */
    public List<Character> getCharacterList(String path) {
        return config.getCharacterList(path);
    }

    /**
     * Checks if the specified {@link String} path is a {@link List} list.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link List} list
     */
    public Boolean isList(String path) {
        return config.isList(path);
    }

    /**
     * Gets the requested {@link List} list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List} list
     */
    public List<?> getList(String path) {
        return !contains(path) ? null : config.getList(path);
    }

    /**
     * Gets the requested {@link List} list by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link List} value that is returned by default<br>
     * @return       the {@link List} list
     */
    public List<?> getList(String path, List<?> value) {
        return !contains(path) ? null : (config.getList(path, value));
    }

    /**
     * Gets the requested {@link List<Map>} map list by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link List<Map>} map list
     */
    public List<Map<?, ?>> getMapList(String path) {
        return !contains(path) ? null : config.getMapList(path);
    }

    /**
     * Checks if the specified {@link String} path is a {@link OfflinePlayer} offlinePlayer.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link OfflinePlayer} offlinePlayer
     */
    public Boolean isOfflinePlayer(String path) {
        return config.isOfflinePlayer(path);
    }

    /**
     * Gets the requested {@link OfflinePlayer} offlinePlayer by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link OfflinePlayer} offlinePlayer
     */
    public OfflinePlayer getOfflinePlayer(String path) {
        return !contains(path) ? null : config.getOfflinePlayer(path);
    }

    /**
     * Gets the requested {@link OfflinePlayer} offlinePlayer by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param def  represents the {@link OfflinePlayer} def that is returned by default<br>
     * @return       the {@link OfflinePlayer} offlinePlayer
     */
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return !contains(path) ? null : config.getOfflinePlayer(path, def);
    }

    /**
     * Checks if the specified {@link String} path is a {@link ItemStack} itemStack.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link ItemStack} itemStack
     */
    public Boolean isItemStack(String path) {
        return config.isItemStack(path);
    }

    /**
     * Gets the requested {@link ItemStack} itemStack by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link ItemStack} itemStack
     */
    public ItemStack getItemStack(String path) {
        return !contains(path) ? null : config.getItemStack(path);
    }

    /**
     * Gets the requested {@link ItemStack} itemStack by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link ItemStack} value that is returned by default<br>
     * @return       the {@link ItemStack} itemStack
     */
    public ItemStack getItemStack(String path, ItemStack value) {
        return !contains(path) ? null : (config.getItemStack(path, value));
    }

    /**
     * Checks if the specified {@link String} path is a {@link Location} location.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Location} location
     */
    public Boolean isLocation(String path) {
        return config.isLocation(path);
    }

    /**
     * Gets the requested {@link Location} location by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Location} location
     */
    public Location getLocation(String path) {
        return !contains(path) ? null : config.getLocation(path);
    }

    /**
     * Gets the requested {@link Location} location by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param def  represents the {@link Location} def that is returned by default<br>
     * @return       the {@link Location} location
     */
    public Location getLocation(String path, Location def) {
        return !contains(path) ? null : config.getLocation(path, def);
    }

    /**
     * Checks if the specified {@link String} path is a {@link Vector} vector.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Vector} vector
     */
    public Boolean isVector(String path) {
        return config.isVector(path);
    }

    /**
     * Gets the requested {@link Vector} vector by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Vector} vector
     */
    public Vector getVector(String path) {
        return !contains(path) ? null : config.getVector(path);
    }

    /**
     * Gets the requested {@link Vector} vector by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Vector} value that is returned by default<br>
     * @return       the {@link Vector} vector
     */
    public Vector getVector(String path, Vector value) {
        return !contains(path) ? null : config.getVector(path, value);
    }

    /**
     * Checks if the specified {@link String} path is a {@link Color} color.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      whether or not the specified path is a {@link Color} color
     */
    public Boolean isColor(String path) {
        return config.isColor(path);
    }

    /**
     * Gets the requested {@link Color} color by {@link String} path.
     * <br><br>
     *
     * @param path  represents the {@link String} path that is selected as config path<br>
     * @return      the {@link Color} color
     */
    public Color getColor(String path) {
        return !contains(path) ? null : config.getColor(path);
    }

    /**
     * Gets the requested {@link Color} color by {@link String} path out of the {@link Config} config,
     * returning a default value if not found.
     * <br><br>
     *
     * @param path   represents the {@link String} path that is selected as config path<br>
     * @param value  represents the {@link Color} value that is returned by default<br>
     * @return       the {@link Color} color
     */
    public Color getColor(String path, Color value) {
        return !contains(path) ? null : (config.getColor(path, value));
    }

    /**
     * Gets the {@link String} name
     * <br><br>
     * Redirects to {@link #getConfigName()}.<br>
     *
     * @return  the {@link String} name
     */
    public String getName() {
        return getConfigName();
    }

    /**
     * Gets the {@link String} config name
     * <br><br>
     *
     * @return  the {@link String} config name
     */
    public String getConfigName() {
        return config.getName();
    }

    /**
     * Gets the {@link FileConfigurationOptions} options.
     * <br><br>
     * Redirects to {@link #getOptions()}.<br>
     *
     * @return  the {@link FileConfigurationOptions} options
     */
    public FileConfigurationOptions options() {
        return getOptions();
    }

    /**
     * Gets the {@link YamlConfigurationOptions} options.
     * <br><br>
     *
     * @return  the {@link YamlConfigurationOptions} options
     */
    public YamlConfigurationOptions getOptions() {
        return config.options();
    }

    /**
     * Gets the {@link Configuration} root
     * <br><br>
     *
     * @return  the {@link Configuration} root
     */
    public Configuration getRoot() {
        return config.getRoot();
    }

    /**
     * Gets the config as a {@link String} string.
     * <br><br>
     *
     * @return  the {@link String} string
     */
    public String toString() {
        return config.toString();
    }
}