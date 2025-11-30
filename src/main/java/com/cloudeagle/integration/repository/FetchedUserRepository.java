package com.cloudeagle.integration.repository;

import com.cloudeagle.integration.entity.FetchedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FetchedUserRepository extends JpaRepository<FetchedUser, Long> {
    Optional<FetchedUser> findByIntegrationNameAndExternalUserId(String integrationName, String externalUserId);
}
