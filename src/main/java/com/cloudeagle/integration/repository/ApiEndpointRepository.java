package com.cloudeagle.integration.repository;

import com.cloudeagle.integration.entity.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, Long> {
    Optional<ApiEndpoint> findByIntegrationIdAndEndpointName(Long integrationId, String endpointName);
}
