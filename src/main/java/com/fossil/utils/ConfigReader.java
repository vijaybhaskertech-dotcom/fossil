package com.fossil.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility to read framework configurations from config.properties.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String configPath = "src/test/resources/config.properties";
        try (InputStream input = new FileInputStream(configPath)) {
            properties.load(input);
        } catch (IOException e) {
            try (InputStream classPathInput = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (classPathInput != null) {
                    properties.load(classPathInput);
                } else {
                    System.err.println("Warning: config.properties could not be loaded from " + configPath);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getProperty(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp;
        }
        return properties.getProperty(key);
    }

    public static int getIntProperty(String key) {
        String val = getProperty(key);
        return val != null ? Integer.parseInt(val.trim()) : 15;
    }

    public static boolean getBooleanProperty(String key) {
        String val = getProperty(key);
        return val != null && Boolean.parseBoolean(val.trim());
    }
}

