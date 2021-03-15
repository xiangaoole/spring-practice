package com.haroldgao.projects.user.repository;

import com.haroldgao.projects.function.ThrowableFunction;
import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.context.ComponentContext;
import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.sql.DBConnectionManager;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


/**
 * 数据库型 {@link UserRepository} 实现
 *
 * @since 1.0
 */
public class DatabaseUserRepository implements UserRepository {

    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER =
            e -> Logger.error(e.getMessage());

    @Resource(name = "bean/DBConnectionManager")
    private DBConnectionManager dbConnectionManager;

    private Connection getConnection() {
        return dbConnectionManager.getConnection();
    }

    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
                "(?,?,?,?)";
        int result = executeUpdate(sql,
                user.getName(), user.getPassword(), user.getEmail(),
                user.getPhoneNumber());
        return result != 0;
    }

    @Override
    public boolean deleteById(Long userId) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        String sql = "SELECT id,name,password,email,phoneNumber FROM users WHERE id=?";
        ThrowableFunction<ResultSet, User> function = resultSet -> {
            User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
            }
            return user;
        };

        return executeQuery(sql, function, userId);
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        String sql = "SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?";
        ThrowableFunction<ResultSet, User> function = resultSet -> {
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
                return user;
            } else {
                return null;
            }
        };

        return executeQuery(sql, function, userName, password);
    }

    private <T> T executeQuery(String sql, ThrowableFunction<ResultSet, T> function,
                               Object... args) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class<?> argType = arg.getClass();
                String methodName = preparedStatementMethodMappings.get(argType);
                Class<?> wrapperType = wrapperToPrimitiveMappings.get(argType);// Long.class -> long
                if (wrapperType == null) {
                    wrapperType = argType;
                }
                Method method = PreparedStatement.class.getMethod(methodName, int.class, wrapperType);
                method.invoke(preparedStatement, i + 1, args[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return function.apply(resultSet);
        } catch (Throwable e) {
            COMMON_EXCEPTION_HANDLER.accept(e);
        }
        return null;
    }

    private int executeUpdate(String sql, Object... args) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class<?> argType = arg.getClass();
                String methodName = preparedStatementMethodMappings.get(argType);
                Class<?> wrapperType = wrapperToPrimitiveMappings.get(argType);// Long.class -> long
                if (wrapperType == null) {
                    wrapperType = argType;
                }
                Method method = PreparedStatement.class.getMethod(methodName, int.class, wrapperType);
                method.invoke(preparedStatement, i + 1, args[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (Throwable e) {
            COMMON_EXCEPTION_HANDLER.accept(e);
        }
        return 0;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    private static Map<Class<?>, String> preparedStatementMethodMappings = new HashMap<>();
    private static Map<Class<?>, Class<?>> wrapperToPrimitiveMappings = new HashMap<>();

    static {
        preparedStatementMethodMappings.put(String.class, "setString");
        preparedStatementMethodMappings.put(Long.class, "setLong");

        wrapperToPrimitiveMappings.put(Long.class, long.class);
    }
}
