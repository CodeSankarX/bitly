package com.project.bitly.links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LinkRepository extends JpaRepository<Link, UUID> {

    @Query("SELECT l FROM Link l WHERE l.url = ?1")
    Optional<Link> findByUrl(String shortUrl);

    @Query("SELECT l FROM Link l WHERE l.shortUrl = ?1")
    Optional<Link> findByShortUrl(String shortUrl);

}
