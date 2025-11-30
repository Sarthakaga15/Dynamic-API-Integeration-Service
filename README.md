# Dynamic API Integration Service

A Spring Boot application that allows you to integrate with any third-party API dynamically through database configuration, without changing code or redeploying.

## Features

-   **Dynamic Integration**: Configure new API integrations (URL, auth, endpoints) via database.
-   **Field Mapping**: Map external API fields to a standardized internal user model.
-   **No-Code Updates**: Add or modify integrations at runtime.
-   **H2 Database**: Uses an in-memory database for easy setup and testing.

## Prerequisites

-   Java 17 or higher
-   Maven 3.6+

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Sarthakaga15/Dynamic-API-Integeration-Service.git
    cd Dynamic-API-Integeration-Service
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```

3.  **Run the application:**
    ```bash
    java -jar target/dynamic-integration-service-0.0.1-SNAPSHOT.jar
    ```
    The application will start on port `8083`.

## Usage

### 1. Configure an Integration (Example: JSONPlaceholder)

You can use `curl` or Postman to configure the integration.

**Create Integration:**
```bash
curl -X POST http://localhost:8083/api/config/integrations \
  -H "Content-Type: application/json" \
  -d '{
    "integrationName": "jsonplaceholder",
    "displayName": "JSON Placeholder",
    "baseUrl": "https://jsonplaceholder.typicode.com",
    "authType": "none"
  }'
```

**Create Endpoint:**
```bash
curl -X POST http://localhost:8083/api/config/endpoints \
  -H "Content-Type: application/json" \
  -d '{
    "integrationId": 1,
    "endpointName": "list_users",
    "endpointPath": "/users",
    "httpMethod": "GET",
    "isArrayResponse": true
  }'
```

**Create Auth Config:**
```bash
curl -X POST http://localhost:8083/api/config/auth \
  -H "Content-Type: application/json" \
  -d '{ "integrationId": 1, "authHeaderName": "None", "authToken": "None" }'
```

**Create Field Mapping (Map `name` to `name`):**
```bash
curl -X POST http://localhost:8083/api/config/mappings \
  -H "Content-Type: application/json" \
  -d '{
    "integrationId": 1,
    "endpointId": 1,
    "sourceField": "name",
    "targetField": "name",
    "isRequired": true
  }'
```

### 2. Trigger Sync

```bash
curl -X POST "http://localhost:8083/api/integrations/jsonplaceholder/sync?endpointName=list_users"
```

### 3. Fetch Data

```bash
curl -X GET http://localhost:8083/api/users
```

## Database Console

Access the H2 Console at: `http://localhost:8083/h2-console`
-   **JDBC URL**: `jdbc:h2:mem:integration_db`
-   **User**: `sa`
-   **Password**: `password`
