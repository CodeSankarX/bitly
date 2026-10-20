package com.project.bitly.links;

public interface LinkServiceImpl {
    Link shortUrl(String url);

    boolean existsByUrl(String url);

    String fullUrl(String url);

    Link getUrl(String url);
}
