package com.haroldgao.projects.user.sql;

import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.context.ComponentContext;
import com.haroldgao.projects.user.domain.User;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.*;

public class DBConnectionManager {

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    @Resource(name = "jdbc/UserPlatformDB")
    private DataSource dataSource;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public Connection getConnection() {
        // 依赖查找
//        ComponentContext context = ComponentContext.getInstance();
//        DataSource dataSource = context.getComponent("jdbc/UserPlatformDB");
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
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

