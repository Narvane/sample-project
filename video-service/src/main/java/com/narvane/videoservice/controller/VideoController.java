package com.narvane.videoservice.controller;

import com.narvane.videoservice.dto.request.UpdateVideoRequestDTO;
import com.narvane.videoservice.dto.response.UpdateVideoResponseDTO;
import com.narvane.videoservice.model.Video;
import com.narvane.videoservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService service;

    @PostMapping("/upload")
    public void upload(@RequestParam("title") String title,
                       @RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {
        var video = new Video(title);

        video.setUpload(
                multipartFile.getInputStream(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType()
        );

        service.save(video);
    }

    @PutMapping("/update/{id}")
    public UpdateVideoResponseDTO update(@PathVariable("id") UUID id, UpdateVideoRequestDTO updateDTO) {
        var videoUpdate = new Video(updateDTO.getTitle(), updateDTO.getProcessStatus());

        var updatedVideo = service.update(id, videoUpdate);

        return new UpdateVideoResponseDTO(
                updatedVideo.getTitle(),
                updatedVideo.getProcessStatus()
        );
    }

}
