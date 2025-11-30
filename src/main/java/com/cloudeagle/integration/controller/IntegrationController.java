package com.cloudeagle.integration.controller;

import com.cloudeagle.integration.entity.*;
import com.cloudeagle.integration.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config")
public class IntegrationController {

    @Autowired
    private IntegrationConfigRepository integrationRepo;

    @Autowired
    private ApiEndpointRepository endpointRepo;

    @Autowired
    private AuthConfigRepository authRepo;

    @Autowired
    private FieldMappingRepository fieldMappingRepo;

    @GetMapping("/integrations")
    public List<IntegrationConfig> getAllIntegrations() {
        return integrationRepo.findAll();
    }

    @PostMapping("/integrations")
    public IntegrationConfig createIntegration(@RequestBody IntegrationConfig config) {
        return integrationRepo.save(config);
    }

    @PostMapping("/endpoints")
    public ApiEndpoint createEndpoint(@RequestBody ApiEndpoint endpoint) {
        return endpointRepo.save(endpoint);
    }

    @PostMapping("/auth")
    public AuthConfig createAuthConfig(@RequestBody AuthConfig authConfig) {
        return authRepo.save(authConfig);
    }

    @PostMapping("/mappings")
    public FieldMapping createFieldMapping(@RequestBody FieldMapping mapping) {
        return fieldMappingRepo.save(mapping);
    }
}
