package com.nhnacademy.nhnmart.repository.file;

import com.nhnacademy.nhnmart.db.DbConnectionThreadLocal;
import com.nhnacademy.nhnmart.domain.File;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileRepositoryImpl implements FileRepository {
    private final DbConnectionThreadLocal dbConnectionThreadLocal;

    public FileRepositoryImpl(DbConnectionThreadLocal dbConnectionThreadLocal) {
        this.dbConnectionThreadLocal = dbConnectionThreadLocal;
    }

    @Override
    public boolean exists(int fileId) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM nhn_file WHERE id = ?");
            statement.setInt(1, fileId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<File> findByInquiry(int inquiryId) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nhn_file WHERE inquiry_id = ?");
            statement.setInt(1, inquiryId);
            ResultSet resultSet = statement.executeQuery();
            List<File> files = new ArrayList<>();
            while (resultSet.next()) {
                files.add(mapToFile(resultSet));
            }
            return files;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public File addFile(File file) {
        try {
            Connection connection = dbConnectionThreadLocal.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO nhn_file (name, data, inquiry_id, content_type) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, file.getName());
            statement.setBytes(2, file.getData());
            statement.setInt(3, file.getInquiryId());
            statement.setString(4, file.getContentType());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                file.setId(generatedKeys.getInt(1));
            }
            return file;
        } catch (SQLException e) {
            dbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        }
    }

    private File mapToFile(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String contentType = resultSet.getString("content_type");
        byte[] data = resultSet.getBytes("data");
        int inquiryId = resultSet.getInt("inquiry_id");
        return new File(id, name, contentType, data, inquiryId);
    }
}