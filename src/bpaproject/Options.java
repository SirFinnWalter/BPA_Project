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

public class Options {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private static final File CONFIG_FILE = new File("config");
    private static StringBuilder sb = new StringBuilder();

    /**
     * Writes the {@code setting} and {@code value} to a StringBuilder. No settings
     * are saved on disk until the writeToFile() function is called.
     * 
     * @param setting The setting
     * @param value   The associated value
     */
    public static void write(Setting setting, String value) {
        LOGGER.log(Level.FINE, "Writing " + setting.getValue() + " with a value of " + value);

        if (!sb.toString().contains(setting.getValue())) {
            sb.append(setting.getValue() + ":" + value + "\n");
        } else {
            String[] configs = sb.toString().split("\n");
            sb = new StringBuilder();
            for (String s : configs) {
                if (!s.contains(setting.getValue()))
                    sb.append(s + "\n");
            }
            sb.append(setting.getValue() + ":" + value + "\n");
        }
    }

    /**
     * Overwrites the changes in {@code CONFIG_FILE} with the new settings.
     */
    public static void writeToFile() {
        LOGGER.log(Level.FINE, "Writing settings to " + CONFIG_FILE.getName());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    /**
     * Reads the {@code CONFIG_FILE} for the setting character offset in the file.
     * 
     * @param setting The setting is seek for
     * @return The offset count or -1 if {@code setting} is not found
     */
    private static int getOffset(Setting setting) {
        int offset = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String s = reader.readLine();
            while (s != null) {
                if (s.contains(setting.getValue()))
                    return offset;

                offset += s.length() + 1;
                s = reader.readLine();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        } finally {
            LOGGER.log(Level.WARNING, "Could not find " + setting.toString());
            return -1;
        }
    }

    /**
     * Reads the {@code CONFIG_FILE} and gets the value associated with a setting.
     * 
     * @param setting The setting to retrieve value for
     * @return The setting associated or "1" on error or no value associated
     */
    public static String getValue(Setting setting) {
        int offset = getOffset(setting);
        if (offset != -1) {
            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                reader.skip(offset);
                String s = reader.readLine();
                s = s.substring(setting.getValue().length() + 1);

                return s;
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, ex.toString(), ex);
            }
        }
        return "1";
    }

    /**
     * All the settings currently supported.
     */
    public enum Setting {
        SCALE("scale"), VOLUME("volume");

        private String value;
        private static Map<String, Setting> map = new HashMap<>();

        private Setting(String value) {
            this.value = value;
        }

        static {
            for (Setting setting : Setting.values()) {
                map.put(setting.value, setting);
            }
        }

        public String getValue() {
            return value;
        }
    }
}