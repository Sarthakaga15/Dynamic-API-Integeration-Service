package com.cloudeagle.integration.service;

import com.cloudeagle.integration.entity.FetchedUser;
import com.cloudeagle.integration.entity.SyncHistory;
import com.cloudeagle.integration.repository.FetchedUserRepository;
import com.cloudeagle.integration.repository.SyncHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserSyncService {

    @Autowired
    private GenericAPIClient apiClient;

    @Autowired
    private FetchedUserRepository userRepository;

    @Autowired
    private SyncHistoryRepository syncHistoryRepository;

    @Transactional
    public void syncUsers(String integrationName, String endpointName) {
        log.info("Starting sync for integration: {}", integrationName);

        SyncHistory history = new SyncHistory();
        history.setIntegrationName(integrationName);
        history.setEndpointName(endpointName);
        history.setSyncStatus("IN_PROGRESS");
        history.setStartedAt(LocalDateTime.now());
        syncHistoryRepository.save(history);

        try {
            List<Map<String, Object>> users = apiClient.fetchUsers(integrationName, endpointName);

            int count = 0;
            for (Map<String, Object> userData : users) {
                saveUser(integrationName, userData);
                count++;
            }

            history.setSyncStatus("SUCCESS");
            history.setRecordsFetched(count);
            history.setCompletedAt(LocalDateTime.now());
            history.setDurationMs(
                    java.time.Duration.between(history.getStartedAt(), history.getCompletedAt()).toMillis());

        } catch (Exception e) {
            log.error("Sync failed: {}", e.getMessage());
            history.setSyncStatus("FAILED");
            history.setErrorMessage(e.getMessage());
            history.setCompletedAt(LocalDateTime.now());
        } finally {
            syncHistoryRepository.save(history);
        }
    }

    private void saveUser(String integrationName, Map<String, Object> userData) {
        String externalId = (String) userData.get("externalUserId");
        if (externalId == null) {
            log.warn("Skipping user without externalUserId: {}", userData);
            return;
        }

        Optional<FetchedUser> existingUser = userRepository.findByIntegrationNameAndExternalUserId(integrationName,
                externalId);
        FetchedUser user = existingUser.orElse(new FetchedUser());

        user.setIntegrationName(integrationName);
        user.setExternalUserId(externalId);
        user.setEmail((String) userData.get("email"));
        user.setName((String) userData.get("name"));
        user.setUsername((String) userData.get("username"));
        user.setFirstName((String) userData.get("firstName"));
        user.setLastName((String) userData.get("lastName"));
        user.setStatus((String) userData.get("status"));
        user.setAvatarUrl((String) userData.get("avatarUrl"));
        user.setTimezone((String) userData.get("timezone"));

        // Handle dates if present (assuming string for now, might need parsing)
        // user.setCreatedDate(...)

        user.setLastSyncedAt(LocalDateTime.now());

        // Store raw response if needed, for now just toString
        user.setRawResponse(userData.toString());

        userRepository.save(user);
    }
}
