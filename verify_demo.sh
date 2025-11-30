# 1. Create JSONPlaceholder Integration
echo "Creating Integration..."
curl -X POST http://localhost:8082/api/config/integrations \
  -H "Content-Type: application/json" \
  -d '{
    "integrationName": "jsonplaceholder",
    "displayName": "JSON Placeholder",
    "baseUrl": "https://jsonplaceholder.typicode.com",
    "authType": "none",
    "description": "Public test API"
  }'

# 2. Create Endpoint
echo "\nCreating Endpoint..."
# We need to find the ID first, but for H2 it should be 1 since it's fresh
# Let's assume ID 1 for simplicity in this demo script
curl -X POST http://localhost:8082/api/config/endpoints \
  -H "Content-Type: application/json" \
  -d '{
    "integrationId": 1,
    "endpointName": "list_users",
    "endpointPath": "/users",
    "httpMethod": "GET",
    "requiresAuth": false,
    "responseDataPath": "",
    "isArrayResponse": true
  }'

# 3. Create Auth Config
echo "\nCreating Auth Config..."
curl -X POST http://localhost:8082/api/config/auth \
  -H "Content-Type: application/json" \
  -d '{
    "integrationId": 1,
    "authHeaderName": "None",
    "authToken": "None"
  }'

# 4. Create Field Mappings
echo "\nCreating Field Mappings..."
curl -X POST http://localhost:8082/api/config/mappings \
  -H "Content-Type: application/json" \
  -d '{
    "integrationId": 1,
    "endpointId": 1,
    "sourceField": "id",
    "targetField": "externalUserId",
    "isRequired": true
  }'

curl -X POST http://localhost:8082/api/config/mappings \
  -H "Content-Type: application/json" \
  -d '{
    "integrationId": 1,
    "endpointId": 1,
    "sourceField": "name",
    "targetField": "name",
    "isRequired": true
  }'

curl -X POST http://localhost:8082/api/config/mappings \
  -H "Content-Type: application/json" \
  -d '{
    "integrationId": 1,
    "endpointId": 1,
    "sourceField": "email",
    "targetField": "email",
    "isRequired": true
  }'

# 5. Trigger Sync
echo "\nTriggering Sync..."
curl -X POST "http://localhost:8082/api/integrations/jsonplaceholder/sync?endpointName=list_users"

# 6. Verify Users
echo "\nVerifying Users..."
curl -X GET http://localhost:8082/api/users
