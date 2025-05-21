package com.narvane.videoservice.repository;

import com.narvane.videoservice.infra.entity.VideoEntity;
import com.narvane.videoservice.infra.repository.VideoMongoRepository;
import com.narvane.videoservice.model.Video;
import com.narvane.videoservice.util.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class VideoRepository {

    private final VideoMongoRepository mongoRepository;

    public void save(Video video, String tempPath) {
        mongoRepository.save(new VideoEntity(
                UUID.randomUUID(),
                UserContextHolder.getUserId(),
                video.getTitle(),
                tempPath,
                video.getFileName(),
                video.getContentType()
        ));
    }
}
