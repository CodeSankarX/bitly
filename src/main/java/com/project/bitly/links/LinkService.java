package com.project.bitly.links;

import com.project.bitly.exception.LinkException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LinkService implements LinkServiceImpl {

    @Autowired
    LinkRepository linkRepository;

    private final EntityManager entityManager;

    @Autowired
    private RedisTemplate<String, String> linkRedisTemplate;

    public LinkService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Link shortUrl(String url) {
        boolean existingLink = existsByUrl(url);
        if (existingLink) {
            throw new LinkException("Link already exists");
        }
        UUID id = UUID.randomUUID();
        String shortCode;
        int maxRetries = 10;
        do {
            shortCode = ShortCode.generate();
            maxRetries--;
        } while (existsByUrl(shortCode) && maxRetries > 0 );
        if (maxRetries <= 0) {
            return null;
        }
        Link link = new Link(id, url, shortCode, null);
        entityManager.persist(link);
        linkRedisTemplate.opsForValue().set(shortCode, link.getUrl());
        linkRedisTemplate.opsForValue().set(url, shortCode);
        return link;
    }

    @Override
    public boolean existsByUrl(String url) {
        String cachedLink = linkRedisTemplate.opsForValue().get(url);
        if (cachedLink==null) {
            Optional<Link> link = linkRepository.findByUrl(url);
            return link.isPresent();
        }
        return true;
    }

    @Transactional
    @Override
    public String fullUrl(String url) {
        String fullUrl = linkRedisTemplate.opsForValue().get(url);
        if (fullUrl!=null) {
            return fullUrl;
        }
        Optional<Link> byShortUrl = linkRepository.findByShortUrl(url);
        return byShortUrl.isPresent() ? byShortUrl.get().getUrl() : url;
    }

    @Override
    public Link getUrl(String url) {
        Optional<Link> byShortUrl = linkRepository.findByUrl(url);
        return byShortUrl.orElse(null);
    }


}
