package bpaproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file Options.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 23 January, 2019
 */

/**
 * The class {@code Options} is to get or write values for the various settings
 * supported from a config file. If no config file exists, then creates one with
 * all settings default values.
 * <p>
 * New values written are not saved to the disk until the Options.writeToFile()
 * function is called. However, writing new values will make changes for the
 * current sessions if applicable.
 */
public final class Options {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private static final File CONFIG_FILE = new File("config");
    private static StringBuilder configs = null;

    static {
        if (configs == null) {
            configs = new StringBuilder();
            if (!CONFIG_FILE.exists()) {
                LOGGER.log(Level.INFO,
                        CONFIG_FILE.getName() + " does not exist! Creating new file with default settings.");
                try {
                    Setting.map.forEach((k, v) -> {
                        write(k, v);
                    });

                    CONFIG_FILE.createNewFile();
                    writeToFile();
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, ex.toString(), ex);
                }
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                    String s = reader.readLine();
                    while (s != null) {
                        configs.append(s + "\n");
                        s = reader.readLine();
                    }
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, ex.toString(), ex);

                    configs = new StringBuilder();
                    Setting.map.forEach((k, v) -> {
                        write(k, v);
                    });
                }
            }
        }
    }

    /**
     * Writes the {@code setting} with its {@code value} for the session. If the
     * setting already has a value, then overwrites it. No settings are saved on
     * disk until the {@link #writeToFile()} function is called.
     * 
     * @param setting The setting
     * @param value   The associated value
     */
    public static void write(Setting setting, String value) {
        LOGGER.log(Level.FINER, "Writing " + setting.toString() + " with a value of " + value);

        if (!configs.toString().contains(setting.toString())) {
            configs.append(setting.toString() + ":" + value + "\n");
        } else {
            String[] split = configs.toString().split("\n");
            configs = new StringBuilder();
            for (String s : split) {
                if (!s.contains(setting.toString()))
                    configs.append(s + "\n");
            }
            configs.append(setting.toString() + ":" + value + "\n");
        }
    }

    /**
     * Writes the {@code setting} with its {@code value} for the session. If the
     * setting already has a value, then overwrites it. No settings are saved on
     * disk until the writeToFile() function is called.
     * 
     * @param setting The setting
     * @param value   The associated value
     */
    public static void write(Setting setting, int value) {
        write(setting, Integer.toString(value));
    }

    /**
     * Writes all the written settings in the {@code CONFIG_FILE}.
     */
    public static void writeToFile() {
        LOGGER.log(Level.FINE, "Writing settings to " + CONFIG_FILE.getName());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write(configs.toString());
            writer.flush();
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        }
    }

    /**
     * Searches for the setting and returns the value associated. If the setting is
     * not found then return the setting default value.
     * 
     * @param setting The setting to retrieve value for
     * @return The value associated or the default value if none found
     */
    public static String getValue(Setting setting) {
        if (configs.toString().contains(setting.toString())) {
            String[] split = configs.toString().split("\n");
            for (String s : split) {
                if (s.contains(setting.toString()))
                    return s.substring(setting.toString().length() + 1);

            }
        }
        LOGGER.log(Level.FINE, "Could not find " + setting.toString() + ", returning default value.");
        return setting.getDefaultValue();
    }

    /**
     * All the settings currently supported with default values.
     */
    public enum Setting {
        SCALE("2"), VOLUME("50"), LOGGING_LEVEL("500");

        private String value;
        private static Map<Setting, String> map = new HashMap<>();

        static {
            for (Setting setting : Setting.values()) {
                map.put(setting, setting.value);
            }
        }

        private Setting(String value) {
            this.value = value;
        }

        public String getDefaultValue() {
            return value;
        }
    }
}