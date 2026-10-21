package com.project.bitly.links;

import com.project.bitly.user.AppUser;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    AppUser user;
}