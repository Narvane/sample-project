package com.narvane.videoservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

import static com.narvane.videoservice.model.ProcessStatus.TO_PROCESS;

@Getter
@NoArgsConstructor
public class Video {
    @Setter
    private String title;
    @Setter
    private ProcessStatus processStatus = TO_PROCESS;

    private InputStream inputStream;
    private String fileName;
    private String contentType;

    public Video(String title) {
        this.title = title;
    }

    public Video(String title, ProcessStatus processStatus) {
        this.title = title;
        this.processStatus = processStatus;
    }

    public void setUpload(InputStream inputStream,
                          String fileName,
                          String contentType) {
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.contentType = contentType;
    }

}
