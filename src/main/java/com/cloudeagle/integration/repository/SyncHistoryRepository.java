package com.cloudeagle.integration.repository;

import com.cloudeagle.integration.entity.SyncHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncHistoryRepository extends JpaRepository<SyncHistory, Long> {
}
