package com.narvane.videoservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("/videos")
public class VideoController {

    @PostMapping("/upload")
    public void upload(@RequestParam("title") String title,
                       @RequestParam("video") MultipartFile video,
                       Authentication authentication) {

        System.out.println(video);
    }

}
