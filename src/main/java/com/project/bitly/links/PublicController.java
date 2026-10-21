package com.project.bitly.links;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private LinkServiceImpl linkService;

    @GetMapping("/{url}")
    public ResponseEntity<Object> getShortLink(@PathVariable("url") String url) {
        String fullUrl=linkService.fullUrl(url);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(fullUrl))
                .build();
    }
}
