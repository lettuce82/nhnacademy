package com.nhnacademy.nhnmart.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
public class DbConnectionThreadLocal {
    private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> sqlErrorThreadLocal = ThreadLocal.withInitial(() -> false);

    private final DataSource dataSource;

    public DbConnectionThreadLocal(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialize() {
        try {
            Connection connection = dataSource.getConnection();
            connectionThreadLocal.set(connection);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Error initializing database connection", e);
        }
    }

    public Connection getConnection() {
        return connectionThreadLocal.get();
    }

    public void setSqlError(boolean sqlError) {
        sqlErrorThreadLocal.set(sqlError);
    }

    public boolean getSqlError() {
        return sqlErrorThreadLocal.get();
    }

    public void reset() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                if (getSqlError()) {
                    connection.rollback();
                } else {
                    connection.commit();
                }
            } catch (SQLException e) {
                log.error("Error committing or rolling back transaction", e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Error closing database connection", e);
                }
                connectionThreadLocal.remove();
            }
        }
    }
}