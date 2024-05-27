package com.nhnacademy.nhnmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class File {
    private int id; //autoincrement
    private String name;
    private String contentType;
    private byte[] data;
    private int inquiryId;
    public File(String name, String contentType, byte[] data) {
        this.name = name;
        this.contentType = contentType;
        this.data = data;
    }
}