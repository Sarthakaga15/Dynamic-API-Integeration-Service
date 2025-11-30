package com.cloudeagle.integration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "fetched_users")
@Data
public class FetchedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integration_name", nullable = false)
    private String integrationName;

    @Column(name = "external_user_id")
    private String externalUserId;

    private String email;
    private String name;
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String status;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String timezone;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "raw_response", columnDefinition = "TEXT")
    private String rawResponse;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt = LocalDateTime.now();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
