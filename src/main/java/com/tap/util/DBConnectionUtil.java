//package com.tap.util;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
///**
// * Central JDBC connection provider.
// * Uses plain DriverManager (no external pooling library required) but
// * is structured so it can be swapped for a DataSource / HikariCP later
// * without touching any DAO code.
// *
// * Update DB_URL / DB_USER / DB_PASSWORD to match your local MySQL setup.
// */
//public class DBConnectionUtil {
//
//    private static final String DB_URL =
//            "jdbc:mysql://localhost:3306/tummyfull?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "Akhila@123";
//
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("MySQL JDBC Driver not found. Add mysql-connector-j jar to WEB-INF/lib.", e);
//        }
//    }
//
//    private DBConnectionUtil() {}
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//    }
//
//    public static void close(Connection conn) {
//        if (conn != null) {
//            try { conn.close(); } catch (SQLException ignored) {}
//        }
//    }
//
//    public static void close(java.sql.Statement stmt) {
//        if (stmt != null) {
//            try { stmt.close(); } catch (SQLException ignored) {}
//        }
//    }
//
//    public static void close(java.sql.ResultSet rs) {
//        if (rs != null) {
//            try { rs.close(); } catch (SQLException ignored) {}
//        }
//    }
//
//    public static void closeAll(Connection conn, java.sql.Statement stmt, java.sql.ResultSet rs) {
//        close(rs);
//        close(stmt);
//        close(conn);
//    }
//}



package com.tap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central JDBC connection provider.
 * Uses plain DriverManager (no external pooling library required) but
 * is structured so it can be swapped for a DataSource / HikariCP later
 * without touching any DAO code.
 *
 * Update DB_URL / DB_USER / DB_PASSWORD to match your local MySQL setup.
 */
public class DBConnectionUtil {

    // Falls back to your local dev values if the environment variables aren't
    // set - this lets the exact same code run unchanged locally in Eclipse
    // AND in a deployed environment (Railway/Render/etc.) where you set
    // DB_URL / DB_USER / DB_PASSWORD as environment variables instead of
    // committing real credentials to source control.
	private static final String DB_URL = System.getenv().getOrDefault(
	        "DB_URL", "jdbc:mysql://localhost:3306/tummyfull?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "Akhila@123");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. Add mysql-connector-j jar to WEB-INF/lib.", e);
        }
    }

    private DBConnectionUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    public static void close(java.sql.Statement stmt) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) {}
        }
    }

    public static void close(java.sql.ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
    }

    public static void closeAll(Connection conn, java.sql.Statement stmt, java.sql.ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }
}
