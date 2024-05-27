package com.nhnacademy.nhnmart.service;

import com.nhnacademy.nhnmart.converter.FileConverter;
import com.nhnacademy.nhnmart.domain.File;
import com.nhnacademy.nhnmart.repository.file.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FileConverter fileConverter;
    private static final String UPLOAD_DIR = "uploads/";

    public FileService(FileRepository fileRepository, FileConverter fileConverter) {
        this.fileRepository = fileRepository;
        this.fileConverter = fileConverter;
    }

    public List<File> findByInquiry(int inquiryId) {
        return fileRepository.findByInquiry(inquiryId);
    }

    public boolean hasFile(int inquiryId) {
        return fileRepository.findByInquiry(inquiryId) != null;
    }

    public void save(List<MultipartFile> attachedFiles, int inquiryId) throws IOException {
        Path uploadDir = java.nio.file.Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        for (MultipartFile multipartFile : attachedFiles) {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                File file = fileConverter.convert(multipartFile);
                if (file != null) {
                    byte[] fileBytes = multipartFile.getBytes();
                    Path filePath = uploadDir.resolve(multipartFile.getOriginalFilename());
                    Files.write(filePath, fileBytes);
                    file.setInquiryId(inquiryId);
                    fileRepository.addFile(file);
                }
            }
        }
    }
}
