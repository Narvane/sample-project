package com.narvane.videoservice.controller;

import com.narvane.videoservice.model.Video;
import com.narvane.videoservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService service;

    @PostMapping("/upload")
    public void upload(@RequestParam("title") String title,
                       @RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {
        service.save(new Video(title,
                multipartFile.getInputStream(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType()
        ));
    }

}
