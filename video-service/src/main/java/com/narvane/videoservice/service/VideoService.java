package com.narvane.videoservice.service;

import com.narvane.videoservice.model.Video;
import com.narvane.videoservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;

import static com.narvane.videoservice.util.LocalStorageUtil.storeTemporarily;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository repository;
    private final VideoProcessor processor;

    @Transactional(rollbackFor = Exception.class)
    public void save(Video video) throws IOException {
        var tempFile = storeTemporarily(video.getInputStream(), "upload_", ".tmp");
        var tempPath = tempFile.getAbsolutePath();

        try {
            var id = repository.save(video, tempPath);
            processor.send(id, tempPath);
        } catch (Exception e) {
            Files.deleteIfExists(tempFile.toPath());
            throw e;
        }
    }

}
