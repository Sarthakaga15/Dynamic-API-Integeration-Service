package com.cloudeagle.integration.service;

import com.cloudeagle.integration.entity.*;
import com.cloudeagle.integration.repository.*;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class GenericAPIClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IntegrationConfigRepository integrationRepo;

    @Autowired
    private ApiEndpointRepository endpointRepo;

    @Autowired
    private AuthConfigRepository authRepo;

    @Autowired
    private FieldMappingRepository fieldMappingRepo;

    /**
     * THE CORE METHOD - Works for any API!
     * 
     * @param integrationName - e.g., "calendly", "dropbox"
     * @param endpointName    - e.g., "get_current_user"
     * @return List of users mapped to our standard format
     */
    public List<Map<String, Object>> fetchUsers(String integrationName, String endpointName) {

        log.info("Fetching users from integration: {}, endpoint: {}", integrationName, endpointName);

        // Step 1: Load configuration from database
        IntegrationConfig integration = integrationRepo.findByIntegrationName(integrationName)
                .orElseThrow(() -> new RuntimeException("Integration not found: " + integrationName));

        ApiEndpoint endpoint = endpointRepo.findByIntegrationIdAndEndpointName(
                integration.getId(), endpointName)
                .orElseThrow(() -> new RuntimeException("Endpoint not found: " + endpointName));

        AuthConfig auth = authRepo.findByIntegrationId(integration.getId())
                .orElseThrow(() -> new RuntimeException("Auth config not found"));

        List<FieldMapping> fieldMappings = fieldMappingRepo.findByIntegrationId(integration.getId());

        // Step 2: Build the HTTP request dynamically
        String url = integration.getBaseUrl() + endpoint.getEndpointPath();
        HttpHeaders headers = buildHeaders(auth);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        log.debug("Making {} request to: {}", endpoint.getHttpMethod(), url);

        // Step 3: Make the API call
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.valueOf(endpoint.getHttpMethod()),
                    entity,
                    String.class);
        } catch (Exception e) {
            log.error("Error calling API: {}", e.getMessage());
            throw new RuntimeException("API call failed: " + e.getMessage());
        }

        // Step 4: Parse the response using configured data path
        String jsonResponse = response.getBody();
        log.debug("Raw JSON Response: {}", jsonResponse);

        Object data = extractData(jsonResponse, endpoint.getResponseDataPath());

        // Step 5: Convert to list if needed
        List<Map<String, Object>> rawUsers = convertToList(data, endpoint.getIsArrayResponse());
        log.info("Converted to list, size: {}", rawUsers.size());

        // Step 6: Map fields dynamically
        List<Map<String, Object>> mappedUsers = mapFields(rawUsers, fieldMappings);

        log.info("Successfully fetched {} users", mappedUsers.size());
        return mappedUsers;
    }

    /**
     * Build authentication headers from config
     */
    private HttpHeaders buildHeaders(AuthConfig auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build auth header dynamically
        String authValue = auth.getAuthToken();
        if (auth.getAuthHeaderPrefix() != null && !auth.getAuthHeaderPrefix().isEmpty()) {
            authValue = auth.getAuthHeaderPrefix() + " " + authValue;
        }

        headers.set(auth.getAuthHeaderName(), authValue);

        return headers;
    }

    /**
     * Extract data from JSON response using configured path
     * Example: "resource" for Calendly, "data" for others
     */
    private Object extractData(String jsonResponse, String dataPath) {
        // If dataPath is empty, we want the root object ($)
        String path = (dataPath == null || dataPath.isEmpty()) ? "$" : "$." + dataPath;

        try {
            return JsonPath.read(jsonResponse, path);
        } catch (Exception e) {
            log.warn("Could not extract data path: {}, returning full response", path);
            return jsonResponse;
        }
    }

    /**
     * Convert response to list format
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> convertToList(Object data, Boolean isArray) {
        if (data == null) {
            log.warn("Data is null");
            return new ArrayList<>();
        }

        log.info("Data type: {}", data.getClass().getName());

        if (isArray) {
            if (data instanceof List) {
                return (List<Map<String, Object>>) data;
            } else {
                log.warn("Expected array response but got: {}", data.getClass().getSimpleName());
                return new ArrayList<>();
            }
        } else {
            // Single object - wrap in list
            if (data instanceof Map) {
                return List.of((Map<String, Object>) data);
            } else {
                return new ArrayList<>();
            }
        }
    }

    /**
     * Map fields from source API format to our standard User format
     * This is the KEY to supporting multiple APIs!
     */
    private List<Map<String, Object>> mapFields(
            List<Map<String, Object>> rawUsers,
            List<FieldMapping> mappings) {

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> rawUser : rawUsers) {
            Map<String, Object> mappedUser = new HashMap<>();

            for (FieldMapping mapping : mappings) {
                Object value = extractFieldValue(rawUser, mapping.getSourceField());

                if (value != null) {
                    // Apply transformations if configured
                    value = applyTransformation(value, mapping.getTransformationRule());
                    mappedUser.put(mapping.getTargetField(), value);
                } else if (mapping.getDefaultValue() != null) {
                    mappedUser.put(mapping.getTargetField(), mapping.getDefaultValue());
                }
            }

            result.add(mappedUser);
        }

        return result;
    }

    /**
     * Extract field value using JSON path
     * Supports nested fields like "user.email" or "resource.name"
     */
    private Object extractFieldValue(Map<String, Object> data, String fieldPath) {
        if (fieldPath == null)
            return null;

        if (!fieldPath.contains(".")) {
            // Simple field
            return data.get(fieldPath);
        }

        // Nested field - traverse the path
        String[] parts = fieldPath.split("\\.");
        Object current = data;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else {
                return null;
            }
        }

        return current;
    }

    /**
     * Apply transformation rules (lowercase, trim, etc.)
     */
    private Object applyTransformation(Object value, String rule) {
        if (rule == null || value == null) {
            return value;
        }

        String strValue = value.toString();

        return switch (rule.toLowerCase()) {
            case "lowercase" -> strValue.toLowerCase();
            case "uppercase" -> strValue.toUpperCase();
            case "trim" -> strValue.trim();
            default -> value;
        };
    }
}
