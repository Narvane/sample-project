package com.narvane.videoservice.service;

import com.narvane.videoservice.infra.event.VideoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VideoProcessor {

    public static final String VIDEOS_TO_PROCESS_TOPIC = "videos-to-process";

    private final KafkaTemplate<String, VideoEvent> kafkaTemplate;

    public void send(UUID videoId, String tempPath) {
        VideoEvent event = new VideoEvent();
        event.setVideoId(videoId);
        event.setTempPath(tempPath);
        kafkaTemplate.send(VIDEOS_TO_PROCESS_TOPIC, event);
    }

}
