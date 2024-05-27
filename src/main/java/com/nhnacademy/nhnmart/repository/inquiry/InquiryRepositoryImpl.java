package com.nhnacademy.nhnmart.repository.inquiry;

import com.nhnacademy.nhnmart.db.DbConnectionThreadLocal;
import com.nhnacademy.nhnmart.domain.Inquiry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class InquiryRepositoryImpl implements InquiryRepository {
    private final DbConnectionThreadLocal dbConnectionThreadLocal;

    public InquiryRepositoryImpl(DbConnectionThreadLocal dbConnectionThreadLocal) {
        this.dbConnectionThreadLocal = dbConnectionThreadLocal;
    }

    @Override
    public boolean hasAnswer(int inquiryId) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT answer_id FROM nhn_inquiry WHERE id = ?");
            statement.setInt(1, inquiryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.getInt("answer_id");
                return !resultSet.wasNull();
            }
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Inquiry findByInquiryId(int inquiryId) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_inquiry WHERE id = ?");
            statement.setInt(1, inquiryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToInquiry(resultSet);
            }
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Inquiry addInquiry(Inquiry inquiry) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO nhn_inquiry (title, content, category, customer_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, inquiry.getTitle());
            statement.setString(2, inquiry.getContent());
            statement.setString(3, String.valueOf(inquiry.getCategory()));
            statement.setString(4, inquiry.getCustomerId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                inquiry.setId(id);
            }
            return inquiry;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Inquiry addAnswer(int inquiryId, int answerId) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE nhn_inquiry SET answer_id = ? WHERE id = ?");
            statement.setInt(1, answerId);
            statement.setInt(2, inquiryId);
            statement.executeUpdate();
            Inquiry inquiry = findByInquiryId(inquiryId);
            inquiry.setAnswerId(answerId);
            return inquiry;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inquiry> findUnansweredInquiries() {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_inquiry WHERE answer_id IS NULL ORDER BY created_at DESC");
            ResultSet resultSet = statement.executeQuery();
            List<Inquiry> inquiries = new ArrayList<>();
            while (resultSet.next()) {
                inquiries.add(mapToInquiry(resultSet));
            }
            return inquiries;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inquiry> findAllByUserId(String userId) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_inquiry WHERE customer_id = ? ORDER BY created_at DESC");
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Inquiry> inquiries = new ArrayList<>();
            while (resultSet.next()) {
                inquiries.add(mapToInquiry(resultSet));
            }
            return inquiries;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inquiry> findAllByUserIdAndCategory(String userId, Inquiry.InquiryCategory category) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_inquiry WHERE customer_id = ? AND category = ? ORDER BY created_at DESC");
            statement.setString(1, userId);
            statement.setString(2, category.name());
            ResultSet resultSet = statement.executeQuery();
            List<Inquiry> inquiries = new ArrayList<>();
            while (resultSet.next()) {
                inquiries.add(mapToInquiry(resultSet));
            }
            return inquiries;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    private Inquiry mapToInquiry(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");
        Inquiry.InquiryCategory category = Inquiry.InquiryCategory.valueOf(resultSet.getString("category"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        String customerId = resultSet.getString("customer_id");
        int answerId = resultSet.getInt("answer_id");

        return new Inquiry(id, title, content, category, createdAt, customerId, answerId);
    }
}