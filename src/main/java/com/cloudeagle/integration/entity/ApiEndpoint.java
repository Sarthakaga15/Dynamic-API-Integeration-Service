package com.cloudeagle.integration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_endpoint")
@Data
public class ApiEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integration_id", nullable = false)
    private Long integrationId;

    @Column(name = "endpoint_name", nullable = false)
    private String endpointName;

    @Column(name = "endpoint_path", nullable = false)
    private String endpointPath;

    @Column(name = "http_method")
    private String httpMethod = "GET";

    @Column(name = "requires_auth")
    private Boolean requiresAuth = true;

    @Column(name = "response_data_path")
    private String responseDataPath;

    @Column(name = "is_array_response")
    private Boolean isArrayResponse = false;

    // Pagination fields
    @Column(name = "supports_pagination")
    private Boolean supportsPagination = false;

    @Column(name = "pagination_type")
    private String paginationType;

    @Column(name = "page_param_name")
    private String pageParamName;

    @Column(name = "page_size_param_name")
    private String pageSizeParamName;

    @Column(name = "default_page_size")
    private Integer defaultPageSize = 20;

    @Column(name = "next_page_token_path")
    private String nextPageTokenPath;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
