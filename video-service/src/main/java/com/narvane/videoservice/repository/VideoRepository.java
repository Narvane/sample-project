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

    public Video findById(UUID id) {
        var entityFind = mongoRepository.findById(id);

        return entityFind.map(foundEntity ->
                new Video(foundEntity.getTitle(), foundEntity.getProcessStatus())
        ).orElse(null);
    }

    public UUID save(Video video, String tempPath) {
        return mongoRepository.save(new VideoEntity(
                UUID.randomUUID(),
                UserContextHolder.getUserId(),
                video.getProcessStatus(),
                video.getTitle(),
                tempPath,
                video.getFileName(),
                video.getContentType()
        )).getId();
    }

    public Video update(UUID id, Video updateVideo) {
        var updatedEntity = mongoRepository.save(
                new VideoEntity(
                    id,
                    updateVideo.getTitle(),
                    updateVideo.getProcessStatus()
                )
        );

        return new Video(updatedEntity.getTitle(), updatedEntity.getProcessStatus());
    }

}
