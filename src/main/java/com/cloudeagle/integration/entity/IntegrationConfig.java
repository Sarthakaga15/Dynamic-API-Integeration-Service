package com.cloudeagle.integration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_config")
@Data
public class IntegrationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integration_name", unique = true, nullable = false)
    private String integrationName;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "auth_type", nullable = false)
    private String authType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
