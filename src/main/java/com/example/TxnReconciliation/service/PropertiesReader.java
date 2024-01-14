package com.example.TxnReconciliation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    static final Logger logger = LoggerFactory.getLogger(String.valueOf(PropertiesReader.class));

    private static final PropertiesReader instance = new PropertiesReader();
    private final Properties properties;

    private PropertiesReader() {
        properties = new Properties();
        try (InputStream input = PropertiesReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            logger.error("Error reading properties file due to : {}", e.getMessage());
        }
    }

    public static PropertiesReader getInstance() {
        return instance;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
