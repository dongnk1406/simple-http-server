package com.stephen.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.stephen.utils.Json;

public class ConfigurationManager {

    // Singleton instance of ConfigurationManager
    private static ConfigurationManager configurationManager;

    private static Configuration currentConfiguration;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    // Method to load configuration from a file (e.g., http.json)
    public void loadConfiguration(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException("Configuration file not found: " + filePath, e);
        }

        int i;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((i = fileReader.read()) != -1) {
                stringBuilder.append((char) i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException("Error reading configuration file: " + filePath, e);
        }

        JsonNode conf;
        try {
            conf = Json.parse(stringBuilder.toString());
        } catch (Exception e) {
            throw new HttpConfigurationException("Error parsing configuration file: " + filePath, e);
        }

        try {
            currentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (Exception e) {
            throw new HttpConfigurationException("Error mapping configuration to Configuration class: " + filePath, e);
        }

    }

    // Return the loaded configuration
    public Configuration getCurrentConfiguration() {
        if (currentConfiguration == null) {
            throw new HttpConfigurationException("Configuration not loaded. Please call loadConfiguration() first.");
        }
        return currentConfiguration;
    }
}
