package com.narvane.videoservice.dto.response;

import com.narvane.videoservice.model.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class UpdateVideoResponseDTO {
    private String title;
    private ProcessStatus processStatus;
}
