package com.project.bitly.links;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/short")
public class LinkController {

    @Autowired
    private LinkServiceImpl linkServiceImpl;

    @PostMapping
    public ResponseEntity<Model.ShortLinkResponse> shortenLink(@RequestParam("url") String url) {
        Link link=linkServiceImpl.shortUrl(url);
        return ResponseEntity.ok(Model.ShortLinkResponse.Mapper(link));
    }


    @GetMapping
    public ResponseEntity<Model.ShortLinkResponse> getShortLinkObject(@RequestParam("url") String url) {
        return ResponseEntity.ok(Model.ShortLinkResponse.Mapper(linkServiceImpl.getUrl(url)));
    }
}
