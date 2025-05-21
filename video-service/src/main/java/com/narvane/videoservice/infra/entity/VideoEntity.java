package com.narvane.videoservice.infra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "videos")
public class VideoEntity {

    @Id
    private UUID id;
    private String userId;

    private String title;
    private String tempPath;
    private String fileName;
    private String contentType;

}