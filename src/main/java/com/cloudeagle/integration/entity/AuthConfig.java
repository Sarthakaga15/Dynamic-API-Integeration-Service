package com.cloudeagle.integration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auth_config")
@Data
public class AuthConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integration_id", nullable = false)
    private Long integrationId;

    @Column(name = "auth_header_name")
    private String authHeaderName = "Authorization";

    @Column(name = "auth_header_prefix")
    private String authHeaderPrefix;

    @Column(name = "auth_token", nullable = false, columnDefinition = "TEXT")
    private String authToken;

    @Column(name = "auth_query_param_name")
    private String authQueryParamName;

    @Column(name = "oauth_token_url")
    private String oauthTokenUrl;

    @Column(name = "oauth_client_id")
    private String oauthClientId;

    @Column(name = "oauth_client_secret", columnDefinition = "TEXT")
    private String oauthClientSecret;

    @Column(name = "oauth_scope", columnDefinition = "TEXT")
    private String oauthScope;

    @Column(name = "oauth_refresh_token", columnDefinition = "TEXT")
    private String oauthRefreshToken;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
