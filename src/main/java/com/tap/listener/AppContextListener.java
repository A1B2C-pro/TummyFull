package com.tap.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Application-wide startup/shutdown hook. Loads the MySQL JDBC driver
 * eagerly on startup so the first request isn't slowed down, and logs
 * lifecycle events.
 */
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[TummyFull] Application started. MySQL driver loaded.");
        } catch (ClassNotFoundException e) {
            System.err.println("[TummyFull] WARNING: MySQL driver not found on classpath: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[TummyFull] Application shutting down.");
    }
}
