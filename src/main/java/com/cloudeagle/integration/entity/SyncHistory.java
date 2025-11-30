package com.cloudeagle.integration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sync_history")
@Data
public class SyncHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integration_name", nullable = false)
    private String integrationName;

    @Column(name = "endpoint_name")
    private String endpointName;

    @Column(name = "sync_status", nullable = false)
    private String syncStatus;

    @Column(name = "records_fetched")
    private Integer recordsFetched = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration_ms")
    private Long durationMs;
}
