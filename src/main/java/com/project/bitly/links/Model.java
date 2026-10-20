package com.project.bitly.links;


public class Model {

    public record ShortLinkResponse(String id, String shortUrl,String originalUrl) {
        public static ShortLinkResponse Mapper(Link link) {
            return new ShortLinkResponse(link.getId().toString(),link.getShortUrl(),link.getUrl());
        }
    }
}
