package com.narvane.videoservice.service;

import com.narvane.videoservice.model.Video;
import com.narvane.videoservice.repository.VideoRepository;
import com.narvane.videoservice.util.VideoUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository repository;
    private final VideoProcessor processor;

    public void save(Video video) throws IOException {
        var tempPath = VideoUploadUtil.saveInputStreamToTempFile(video.getBytes(), "upload_", ".tmp").getAbsolutePath();

        repository.save(video, tempPath);

        processor.send(tempPath);
    }

}
