package com.narvane.videoservice.infra.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class VideoEvent {
    private UUID videoId;
    private String tempPath;
}
