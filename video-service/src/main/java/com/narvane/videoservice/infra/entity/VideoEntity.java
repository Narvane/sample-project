package com.narvane.videoservice.infra.entity;

import com.narvane.videoservice.model.ProcessStatus;
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
    private ProcessStatus processStatus;

    private String title;
    private String tempPath;
    private String fileName;
    private String contentType;

    public VideoEntity(UUID id,  String title, ProcessStatus processStatus) {
        this.id = id;
        this.title = title;
        this.processStatus = processStatus;
    }
}