package com.cloudeagle.integration.repository;

import com.cloudeagle.integration.entity.AuthConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthConfigRepository extends JpaRepository<AuthConfig, Long> {
    Optional<AuthConfig> findByIntegrationId(Long integrationId);
}
