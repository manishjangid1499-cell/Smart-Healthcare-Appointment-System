package com.manish.healthcare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    private static Connection conn;

    public static Connection getConnection() throws Exception {
        if (conn != null && !conn.isClosed()) return conn;

        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new RuntimeException("application.properties not found. Copy application.properties.example and set credentials.");
            }
            props.load(in);
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        conn = DriverManager.getConnection(url, user, pass);
        return conn;
    }
}
