package com.cloudeagle.integration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "field_mapping")
@Data
public class FieldMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integration_id", nullable = false)
    private Long integrationId;

    @Column(name = "endpoint_id")
    private Long endpointId;

    @Column(name = "source_field", nullable = false)
    private String sourceField;

    @Column(name = "source_data_type")
    private String sourceDataType;

    @Column(name = "target_field", nullable = false)
    private String targetField;

    @Column(name = "is_required")
    private Boolean isRequired = false;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "transformation_rule")
    private String transformationRule;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
