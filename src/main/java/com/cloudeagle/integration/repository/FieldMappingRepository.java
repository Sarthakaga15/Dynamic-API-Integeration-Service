package com.cloudeagle.integration.repository;

import com.cloudeagle.integration.entity.FieldMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldMappingRepository extends JpaRepository<FieldMapping, Long> {
    List<FieldMapping> findByIntegrationId(Long integrationId);
}
