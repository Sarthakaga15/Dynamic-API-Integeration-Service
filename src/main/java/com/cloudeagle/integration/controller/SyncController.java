package com.cloudeagle.integration.controller;

import com.cloudeagle.integration.service.UserSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integrations")
public class SyncController {

    @Autowired
    private UserSyncService userSyncService;

    @PostMapping("/{integrationName}/sync")
    public ResponseEntity<String> syncUsers(
            @PathVariable String integrationName,
            @RequestParam(defaultValue = "get_current_user") String endpointName) {

        // In a real app, we might want to run this asynchronously
        userSyncService.syncUsers(integrationName, endpointName);

        return ResponseEntity.ok("Sync triggered for " + integrationName);
    }
}
