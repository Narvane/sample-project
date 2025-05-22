package com.narvane.videoservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class Video {
    private String title;
    private InputStream inputStream;
    private String fileName;
    private String contentType;
}
