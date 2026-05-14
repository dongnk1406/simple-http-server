package com.stephen;

import com.stephen.config.Configuration;
import com.stephen.config.ConfigurationManager;

/**
 * Driver class for the HTTP server.
 */
public class Main {
    public static void main(String[] args) {
        ConfigurationManager.getInstance().loadConfiguration("src/main/resources/http.json");
        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

        System.out.println("Starting HTTP server on port " + config.getPort());
        System.out.println("Web root: " + config.getWebRoot());
    }
}