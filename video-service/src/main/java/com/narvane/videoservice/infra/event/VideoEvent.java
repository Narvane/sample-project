package com.narvane.videoservice.infra.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VideoEvent {
    private String tempPath;
}
