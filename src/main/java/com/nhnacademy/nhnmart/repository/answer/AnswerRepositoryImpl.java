package com.nhnacademy.nhnmart.repository.answer;

import com.nhnacademy.nhnmart.db.DbConnectionThreadLocal;
import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.repository.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;

@Repository
public class AnswerRepositoryImpl implements AnswerRepository {
    private final DbConnectionThreadLocal dbConnectionThreadLocal;

    public AnswerRepositoryImpl(DbConnectionThreadLocal dbConnectionThreadLocal, UserRepository userRepository) {
        this.dbConnectionThreadLocal = dbConnectionThreadLocal;
    }

    @Override
    public Answer findByAnswerId(int answerId) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_answer WHERE id = ?");
            statement.setInt(1, answerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapToAnswer(resultSet);
            }
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Answer addByInquiryId(Answer answer) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO nhn_answer (content, admin_id) VALUES (?, ?)",
            Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, answer.getContent());
            statement.setString(2, answer.getAdminId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                answer.setId(id);
            }
            return answer;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    private Answer mapToAnswer(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String content = resultSet.getString("content");
        LocalDateTime date = resultSet.getTimestamp("created_at").toLocalDateTime();
        String adminId = resultSet.getString("admin_id");

        return new Answer(id, content, date, adminId);
    }
}