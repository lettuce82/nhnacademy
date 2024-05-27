package com.nhnacademy.nhnmart.repository.user;

import com.nhnacademy.nhnmart.db.DbConnectionThreadLocal;
import com.nhnacademy.nhnmart.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final DbConnectionThreadLocal dbConnectionThreadLocal;

    public UserRepositoryImpl(DbConnectionThreadLocal dbConnectionThreadLocal) {
        this.dbConnectionThreadLocal = dbConnectionThreadLocal;
    }

    @Override
    public boolean match(String id, String password) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_user WHERE id = ? AND password = ?");
            statement.setString(1, id);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public User getUser(String id) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_user WHERE id = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToUser(resultSet);
            }
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
        return null;
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        User.Role role = User.Role.valueOf(resultSet.getString("role"));
        return new User(id, password, name, role);
    }
}