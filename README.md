# Tickets API

The Ticket Management API allows you to retrieve and manage registered tickets. It provides various methods for handling data, including `POST`, `GET`, `DELETE`, `PATCH`, and `PUT`.

## Ticket Retrieval Endpoint

**URL**: `/tickets/`

**Method**: `GET`

### 1. Retrieve All Tickets

- **URL**: `/tickets/`
- **Method**: `GET`
- **Query Parameters**: None

**Example Request**:
```http
GET /tickets/ HTTP/1.1
Host: localhost:8080
```

**Success Response**:
- **Code**: `200 OK`
- **Content Example**:
```json
[
    {
        "id": 1,
        "title": "First ticket",
        "description": "This is the first ticket",
        "author": "Author1"
    },
    {
        "id": 2,
        "title": "Second ticket",
        "description": "This is the second ticket",
        "author": "Author2"
    }
]
```

**Error Response**: None

### 2. Filter Tickets by Parameters

- **URL**: `/tickets/`
- **Method**: `GET`
- **Query Parameters**:
    - `title` (optional)
    - `author` (optional)
    - `description` (optional)

**Example Request**:
```http
GET /tickets/?author=lorenzo&title=malfunction HTTP/1.1
Host: localhost:8080
```

**Success Response**:
- **Code**: `200 OK`
- **Content Example**:
```json
[
    {
        "id": 3,
        "title": "malfunction",
        "description": "images not loading on homepage",
        "author": "lorenzo"
    },
    {
        "id": 10,
        "title": "malfunction",
        "description": "login not working",
        "author": "lorenzo"
    }
]
```

**Error Response**:
- **Code**: `404 NOT FOUND`
- **Content Example**:
```json
{
  "message": "ticket not found"
}
```

### 3. Retrieve a Single Ticket by ID

- **URL**: `/tickets/{id}`
- **Method**: `GET`
- **Path Parameters**:
    - `id`: The ID of the ticket to retrieve

**Example Request**:
```http
GET /tickets/2 HTTP/1.1
Host: localhost:8080
```

**Success Response**:
- **Code**: `200 OK`
- **Content Example**:
```json
{
    "id": 2,
    "title": "Title",
    "description": "Ticket description",
    "author": "Staff"
}
```

**Error Response**:
- **Condition**: If the provided ID is not found
- **Code**: `404 NOT FOUND`
- **Content Example**:
```json
{
  "message": "ticket not found"
}
```
- **Condition**: If the provided ID is not an integer
- **Code**: `405 BAD REQUEST`
- **Content Example**:
```json
{
  "message": "Invalid ticket ID"
}
```

### 4. Update an Existing Ticket (PUT)

**URL**: `/tickets/{id}`
**Method**: `PUT`

This endpoint allows updating an existing ticket. You must provide all fields of the ticket (`title`, `description`, `author`) for a full update. For partial updates, use the `PATCH` method.

#### Request Content-Type
- `application/json`
- `application/x-www-form-urlencoded`

#### Path Parameters
- `id`: The ID of the ticket to update (included in the URL)

#### Body Parameters (required for both Content-Types)
- `title` (string): The updated title of the ticket
- `description` (string): The updated description of the ticket
- `author` (string): The updated author of the ticket

**Example Request (JSON)**:
```http
PUT /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "title": "Updated title",
    "description": "Updated description",
    "author": "Updated author"
}
```

**Example Request (Form-urlencoded)**:
```http
PUT /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=Updated+title&description=Updated+description&author=Updated+author
```

**Success Response**:
- **Code**: `200 OK`
- **Content Example**:
```json
{
  "message": "Ticket updated",
  "id": 1
}
```

**Error Response**:
- **Condition**: If the provided ID is not found
- **Code**: `404 NOT FOUND`
- **Content Example**:
```json
{
  "message": "ticket not found"
}
```
- **Condition**: If the provided ID is invalid
- **Code**: `404 NOT FOUND`
- **Content Example**:
```json
{
  "message": "Invalid ticket ID"
}
```
- **Condition**: If the provided Content-Type is unsupported
- **Code**: `415 UNSUPPORTED MEDIA TYPE`
- **Content Example**:
```json
{
  "message": "Unsupported Content-Type"
}
```
- **Condition**: If a required field is missing (e.g., `title`, `description`, or `author`)
- **Code**: `400 BAD REQUEST`
- **Content Example**:
```json
{
  "message": "Missing a field to update, use Patch to update partially a ticket"
}
```

### 5. Delete One or More Tickets (DELETE)

This endpoint allows you to delete one or more tickets. You can delete a specific ticket by ID or delete tickets that match certain search parameters such as `title`, `description`, or `author`.

#### 1. Delete Tickets by Parameters

- **URL**: `/tickets/`
- **Method**: `DELETE`
- **Query Parameters**:
    - `title` (optional): The title of the ticket(s) to delete
    - `description` (optional): The description of the ticket(s) to delete
    - `author` (optional): The author of the ticket(s) to delete

**Example Request**:
```http
DELETE /tickets/?author=lorenzo&title=bug HTTP/1.1
Host: localhost:8080
```

**Success Response**:
- **Code**: `200 OK`
- **Content Example**:
```json
{
  "message": "Tickets deleted",
  "deletedCount": 2
}
```

**Error Response**:
- **Condition**: If no tickets match the provided parameters
- **Code**: `404 NOT FOUND`
- **Content Example**:
```json
{
  "message": "No tickets found for the given parameters"
}
```

#### 2. Delete a Ticket by ID

- **URL**: `/tickets/{id}`
- **Method**: `DELETE`
- **Path Parameters**:
    - `id`: The ID of the ticket to delete

**Example Request**:
```http
DELETE /tickets/1 HTTP/1.1
Host: localhost:8080
```

**Success Response**:
- **Code**: `204 NO CONTENT`
- **Content Example**: No content (empty response)

**Error Response**:
- **Condition**: If the provided ID is not found
- **Code**: `404 NOT FOUND`
- **Content Example**:
```json
{
  "message": "ticket not found"
}
```
- **Condition**: If the provided ID is invalid
- **Code**: `404 NOT FOUND`
- **Content Example**:
```json
{
  "message": "Invalid ticket ID"
}
```

### 6. Create a New Ticket (POST)

This endpoint allows you to create a new ticket. You can send the ticket data either in `JSON` format or as `application/x-www-form-urlencoded`.

**URL**: `/tickets/`
**Method**: `POST`

#### Request Content-Type
- `application/json`
- `application/x-www-form-urlencoded`

#### Body Parameters
- `title` (string): The title of the new ticket
- `description` (string): The description of the new ticket
- `author` (string): The author of the new ticket

**Example Request (JSON)**:
```http
POST /tickets/ HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "title": "New ticket",
    "description": "Description of the new ticket",
    "author": "Ticket author"
}
```

**Example Request (Form-urlencoded)**:
```http
POST /tickets/ HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=New+ticket&description=Description+of+the+new+ticket&author=Ticket+author
```

**Success Response**:
- **Code**: `201 CREATED`
- **Content Example**:
```json
{
  "message": "Ticket created",
  "id": 1
}
```

**Error Response**:
- **Condition**: If the provided Content-Type is unsupported
- **Code**: `415 UNSUPPORTED MEDIA TYPE`
- **Content Example**:
```json
{
  "message": "Unsupported Content-Type"
}
```
- **Condition**: If the ticket being created already exists
- **Code**:

`409 CONFLICT`
- **Content Example**:
```json
{
  "message": "Ticket already exists"
}
```
### 4. Update an Existing Ticket (PUT)

**URL**: `/tickets/{id}`

**Method**: `PATCH`

This endpoint allows updating an existing ticket. Unlike `PUT`, it is not required to provide all fields of the ticket (`title`, `description`, `author`). This method will only update the fields specified in the request, leaving the other fields unchanged.

#### Request Content-Type
- `application/json`
- `application/x-www-form-urlencoded`

#### Path Parameters
- `id`: The ID of the ticket to update (included in the URL)

#### Body Parameters (optional)
- `title` (string): The updated title of the ticket
- `description` (string): The updated description of the ticket
- `author` (string): The updated author of the ticket

#### Example Request (JSON)

```http
PATCH /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "title": "New title",
    "description": "New description",
    "author": "Updated author"
}
```

#### Example Request (Form-urlencoded)

```http
PATCH /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=New+title&description=New+description&author=Updated+author
```

**Success Response**:
- **Code**: `200 OK`
- **Content Example**:

```json
{
  "message": "Ticket updated",
  "id": 1
}
```

**Error Response**:

- **Condition**: If the provided ID is not found
- **Code**: `404 NOT FOUND`
- **Content Example**:

```json
{
  "message": "ticket not found"
}
```

- **Condition**: If the provided ID is invalid
- **Code**: `404 NOT FOUND`
- **Content Example**:

```json
{
  "message": "Invalid ticket ID"
}
```

- **Condition**: If the provided Content-Type is unsupported (neither `application/json` nor `application/x-www-form-urlencoded`)
- **Code**: `415 UNSUPPORTED MEDIA TYPE`
- **Content Example**:

```json
{
  "message": "Unsupported Content-Type"
}
```