package com.haroldgao.projects.user.sql;

import com.haroldgao.projects.function.ThrowableFunction;
import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.context.ComponentContext;
import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.repository.DatabaseUserRepository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Enumeration;
import java.util.logging.Level;

public class DBConnectionManager {
    public Connection getConnection() {
        ComponentContext context = ComponentContext.getInstance();
        // 依赖查找
        DataSource dataSource = context.getComponent("jdbc/UserPlatformDB");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }

        if (connection != null) {
            Logger.info("JNDI 获取数据库连接成功");
        }
        return connection;
    }

    public static void main(String[] args) {
        long userId = 6;

        String databaseUrl = "jdbc:derby:/Users/gaoxiang/learn/derby/user-platform;create=true";
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            if (connection != null) {
                Logger.warning("Get connection success!");
            }
            String sql = "SELECT id,name,password,email,phoneNumber FROM users WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
            }
            Logger.info(user.toString());
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }

    }
}

