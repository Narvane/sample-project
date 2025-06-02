package com.narvane.videoservice.dto.request;

import com.narvane.videoservice.model.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVideoRequestDTO {
    private String title;
    private ProcessStatus processStatus;
}
