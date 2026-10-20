package com.project.bitly.links;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "link")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Link {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, unique = true)
    private String shortUrl;

    private String description;
}