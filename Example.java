package me.gush3l.MagnumBot;

import lombok.Getter;
import lombok.Setter;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.file.FileConfiguration;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class Config {

    @Getter
    @Setter
    private static FileConfiguration config;

    public static void initialize() {
        config = new YamlConfiguration();
        try {
            Util.log("Loading config.yml for the jar...", Level.INFO);
            config.load(createConfig());
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public static File createConfig() {
        try{
            String configPath = getProgramPath()+"/config.yml";
            File configFile = new File(configPath);
            if (configFile.createNewFile()){
                Util.log("The config hasn't been found in the folder where the jar is running, so a new one has been created!",Level.WARNING);
                InputStream jarConfigFile = Config.class.getResourceAsStream("/config.yml");
                try (FileOutputStream outputStream = new FileOutputStream(configFile)) {
                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = jarConfigFile.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                }
            }
            else{
                Util.log("Config has been found!",Level.INFO);
            }
            return configFile;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getProgramPath() {
        String currentdir = System.getProperty("user.dir");
        currentdir = currentdir.replace("\\", "/");
        return currentdir;
    }

}
