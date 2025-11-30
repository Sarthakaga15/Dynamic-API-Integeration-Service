package com.cloudeagle.integration.repository;

import com.cloudeagle.integration.entity.IntegrationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntegrationConfigRepository extends JpaRepository<IntegrationConfig, Long> {
    Optional<IntegrationConfig> findByIntegrationName(String integrationName);
}
