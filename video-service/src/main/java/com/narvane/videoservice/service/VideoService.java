package com.narvane.videoservice.service;

import com.narvane.videoservice.model.Video;
import com.narvane.videoservice.repository.VideoRepository;
import com.narvane.videoservice.util.VideoUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository repository;
    private final VideoProcessor processor;

    @Transactional(rollbackFor = Exception.class)
    public void save(Video video) throws IOException {
        var tempFile = VideoUploadUtil.saveInputStreamToTempFile(video.getBytes(), "upload_", ".tmp");
        var tempPath = tempFile.getAbsolutePath();

        try {
            repository.save(video, tempPath);
            processor.send(tempPath);
        } catch (Exception e) {
            Files.deleteIfExists(tempFile.toPath());
            throw e;
        }
    }

}
