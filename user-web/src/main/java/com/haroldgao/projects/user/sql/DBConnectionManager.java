package com.haroldgao.projects.user.sql;

import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.repository.DatabaseUserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionManager {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void releaseConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    public static void main(String[] args) {
        // the database name
        String dbName = "/Users/gaoxiang/learn/derby/user-platform";
        String connectionURL = "jdbc:derby:" + dbName + ";create=true";
        // list drivers
        Enumeration<Driver> enumeration = DriverManager.getDrivers();
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }

        DBConnectionManager dbConnectionManager = new DBConnectionManager();
        DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository(dbConnectionManager);
        try (Connection connection = DriverManager.getConnection(connectionURL)) {
            log("Connected to database " + dbName);
            dbConnectionManager.setConnection(connection);

            User user = databaseUserRepository.getById((long) 6);
            log(user.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void log(String msg) {
        System.out.println(DBConnectionManager.class.getName() + " => " + msg);
    }
}

