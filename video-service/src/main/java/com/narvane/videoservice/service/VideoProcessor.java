package com.narvane.videoservice.service;

import com.narvane.videoservice.infra.event.VideoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoProcessor {

    public static final String VIDEOS_TO_PROCESS_TOPIC = "videos-to-process";

    private final KafkaTemplate<String, VideoEvent> kafkaTemplate;

    public void send(String tempPath) {
        VideoEvent event = new VideoEvent();
        event.setTempPath(tempPath);
        kafkaTemplate.send(VIDEOS_TO_PROCESS_TOPIC, event);
    }

}
