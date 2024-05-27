package com.nhnacademy.nhnmart.repository.file;

import com.nhnacademy.nhnmart.domain.File;

import java.util.List;

public interface FileRepository {
    boolean exists(int fileId);
    List<File> findByInquiry(int inquiryId);
    File addFile(File file);
}
