package com.stephen.httpserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stephen.httpserver.config.Configuration;
import com.stephen.httpserver.config.ConfigurationManager;
import com.stephen.httpserver.core.ServerListenerThread;

/**
 * Driver class for the HTTP server.
 */
public class HttpServer {
    private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        logger.info("Starting HTTP server...");

        ConfigurationManager.getInstance().loadConfiguration("src/main/resources/http.json");
        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

        logger.info("Server configuration loaded: port={}, webRoot={}", config.getPort(), config.getWebRoot());
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(config.getPort(), config.getWebRoot());
            serverListenerThread.start();
        } catch (IOException e) {
            logger.error("Error occurred while starting the server", e);
        }
    }
}