package com.nhnacademy.nhnmart.converter;

import com.nhnacademy.nhnmart.domain.File;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FileConverter implements Converter<MultipartFile, File> {
    public File convert(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        try {
            return new File(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                    multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file data.", e);
        }
    }
}