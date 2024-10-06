# Tickets API

L'API per la gestione dei ticket consente di recuperare informazioni sui ticket registrati. Può essere utilizzata per gestire dei ticket, l'api mette a disposizione diversi metodi per la gestione dei dati tra cui POST, GET, DELETE, PATCH e PUT

## Endpoint per il recupero dei ticket

**URL** : `/tickets/`

**Method** : `GET`

### 1. Recupera tutti i ticket

- **URL** : `/tickets/`

- **Method** : `GET`

- **Query Parameters** : Nessuno

**Example Request** :
```http
GET /tickets/ HTTP/1.1
Host: localhost:8080
```
**Success Response**:
- Code : `200 OK`

**Content Example**:

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
**Error response**:
nessuno

### 2. Filtra i ticket in base ai parametri

- **URL** : `/tickets/`

- **Method** : `GET`

- **Query Parameters** :
  `title` (facoltativo)
  `author` (facoltativo)
  `description` (facoltativo)

**Example Request** :
```http
GET /tickets/?author=lorenzo&title=malfunzionamento HTTP/1.1
Host: localhost:8080
```
**Success Response**:
- Code : `200 OK`

**Content Example**:

```json
[
  
    {
      "title": "malfunzionamento",
      "description": "non carica le immagini nella home",
      "author": "lorenzo",
      "id": 3
    },
  {
    "title": "malfunzionamento",
    "description": "non funziona il login",
    "author": "lorenzo",
    "id": 10
  }
  
]
```
**Error response**:
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "ticket not found"
}
```
### 3. Recupera un singolo ticket in base all'ID

- **URL** : `/tickets/{id}`

- **Method** : `GET`

- **Query Parameters**:
  `id`: id del ticket da recuperare

**Example Request** :
```http
GET /tickets/2 HTTP/1.1
Host: localhost:8080
```
**Success Response**:
- Code : `200 OK`

**Content Example**:

```json

{
  "title": "Titolo",
  "description": "Descrizione del ticket",
  "author": "Staff",
  "id": 2
}
```
**Error response**:
- Condition: Se l'id fornito non è presente nel database
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "ticket not found"
}
```
**Error response**:
- Condition: Se l'id fornito non è un intero
- Code : `405 BAD REQUEST`

**Content Example**:

```json
{
  "message": "Invalid ticket ID"
}
```

### 4. Aggiorna un ticket esistente (PUT)

**URL** : `/tickets/{id}`

**Method** : `PUT`

L'endpoint permette di aggiornare un ticket esistente. È richiesto di fornire tutti i campi del ticket (`title`, `description`, `author`) per eseguire un aggiornamento completo. Per aggiornamenti parziali, è consigliato utilizzare il metodo `PATCH`.

#### Request Content-Type
- `application/json`
- `application/x-www-form-urlencoded`

#### Query Parameters
- `id` : ID del ticket da aggiornare (incluso nell'URL)

#### Body Parameters (necessari per entrambi i Content-Type)
- `title` (stringa): Titolo aggiornato del ticket
- `description` (stringa): Descrizione aggiornata del ticket
- `author` (stringa): Autore aggiornato del ticket

#### Example Request (JSON)

```http
PUT /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "title": "Nuovo titolo",
    "description": "Nuova descrizione",
    "author": "Autore aggiornato"
}
```
#### Example Request (Form-urlencoded)
```http
PUT /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=Nuovo+titolo&description=Nuova+descrizione&author=Autore+aggiornato
```

**Success Response**:
- Code : `200 OK`

**Content Example**:

```json

{
  "message": "Ticket updated",
  "id": 1
}
```
**Error response**:
- Condition: Se l'id fornito non è presente nel database
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "ticket not found"
}
```
**Error response**:
- Condition: Se l'id fornito non è valido
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "Invalid ticket ID"
}
```

**Error response**:
- Condition: Se il Content-Type fornito non è supportato (né application/json né application/x-www-form-urlencoded).
- Code : `415 UNSUPPORTED MEDIA TYPE`

**Content Example**:

```json
{
  "message": "Unsupported Content-Type"
}
```
**Error response**:
- Condition: Se manca un campo obbligatorio nel corpo della richiesta (title, description, o author), o se si tenta di aggiornare un ticket con un corpo incompleto (invece di usare PATCH per aggiornamenti parziali).
- Code : `400 BAD REQUEST`

**Content Example**:

```json
{
  "message": "Missing a field to update, use Patch to update partially a ticket"
}
```
### 5. Elimina uno o più ticket (DELETE)

L'endpoint permette di eliminare uno o più ticket. Può essere utilizzato per eliminare un ticket specifico tramite il suo ID o per eliminare ticket filtrati in base a parametri come `title`, `description`, o `author`.

**URL** : `/tickets/`

**Method** : `DELETE`

#### 1. Elimina uno o più ticket in base ai parametri

È possibile eliminare uno o più ticket che corrispondono a determinati parametri di ricerca.

**URL** : `/tickets/`

**Method** : `DELETE`

**Query Parameters**:
- `title`(facoltativo): Titolo del ticket.
- `description`(facoltativo): Descrizione del ticket.
- `author`(facoltativo): Autore del ticket.

**Example Request**:
```http
DELETE /tickets/?author=lorenzo&title=bug HTTP/1.1
Host: localhost:8080
```
**Success Response**:
- Code : `200 OK`

**Content Example**:

```json

{
  "message": "Tickets deleted",
  "deletedCount": 2
}
```
**Error response**:
- Condition: Se nessun ticket corrisponde ai parametri forniti.
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "No tickets found for the given parameters"
}
```
#### 2. Eliminare un ticket in base all'id

È possibile eliminare un ticket specifico in base al suo ID.

**URL** : `/tickets/{id}`

**Method** : `DELETE`
-**Path Parameters**:
id: L'ID del ticket da eliminare.

-**Query Parameters**:
none

**Example Request**:
```http
DELETE /tickets/1 HTTP/1.1
Host: localhost:8080
```
**Success Response**:
- Code : `204 NO CONTENT`

**Content Example**:
Nessun contenuto (risposta vuota)


**Error response**:
- Condition: Se l'id fornito non è presente nel database
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "Ticket not found"
}
```
**Error response**:
- Condition: Se l'id fornito non è valido
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "Invalid ticket ID"
}
```
### 6. Crea un nuovo ticket (POST)

L'endpoint consente di creare un nuovo ticket. Supporta sia la creazione di ticket inviando i dati in formato JSON, sia utilizzando il formato `application/x-www-form-urlencoded`.

**URL** : `/tickets/`

**Method** : `POST`

#### Request Content-Type
- `application/json`
- `application/x-www-form-urlencoded`

#### Body Parameters
- `title` (stringa): Titolo del ticket.
- `description` (stringa): Descrizione del ticket.
- `author` (stringa): Autore del ticket.

#### Example Request (JSON)

```http
POST /tickets/ HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "title": "Nuovo ticket",
    "description": "Descrizione del nuovo ticket",
    "author": "Autore del ticket"
}
```
#### Example Request (Form-urlencoded)
```http
POST /tickets/ HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=Nuovo+ticket&description=Descrizione+del+nuovo+ticket&author=Autore+del+ticket
```
**Success Response**:
- Code : `201 CREATED`

**Content Example**:
```json
{
"message": "Ticket created",
"id": 1
}
```

**Error response**:
- Condition: Se viene fornito un Content-Type non valido (né application/json né application/x-www-form-urlencoded).
- Code : `415 UNSUPPORTED MEDIA TYPE`

**Content Example**:

```json
{
  "message": "Unsupported Content-Type"
}
```
**Error response**:
- Condition: Se il ticket che si sta tentando di creare esiste già.
- Code : `409 CONFLICT`

**Content Example**:

```json
{
  "message": "Ticket already exists"
}
```
### 4. Aggiorna un ticket esistente (PUT)

**URL** : `/tickets/{id}`

**Method** : `PUT`

L'endpoint permette di aggiornare un ticket esistente. a differenza del PUT, non è richiesto di fornire tutti i campi del ticket (`title`, `description`, `author`). questo metodo aggiornerà esclusivamente i campi segnalati nella richiesta, lasciano invariati gli altri

#### Request Content-Type
- `application/json`
- `application/x-www-form-urlencoded`

#### Query Parameters
- `id` : ID del ticket da aggiornare (incluso nell'URL)

#### Body Parameters (facoltativi)
- `title` (stringa): Titolo aggiornato del ticket
- `description` (stringa): Descrizione aggiornata del ticket
- `author` (stringa): Autore aggiornato del ticket

#### Example Request (JSON)

```http
PUT /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "title": "Nuovo titolo",
    "description": "Nuova descrizione",
    "author": "Autore aggiornato"
}
```
#### Example Request (Form-urlencoded)
```http
PUT /tickets/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=Nuovo+titolo&description=Nuova+descrizione&author=Autore+aggiornato
```

**Success Response**:
- Code : `200 OK`

**Content Example**:

```json

{
  "message": "Ticket updated",
  "id": 1
}
```
**Error response**:
- Condition: Se l'id fornito non è presente nel database
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "ticket not found"
}
```
**Error response**:
- Condition: Se l'id fornito non è valido
- Code : `404 NOT FOUND`

**Content Example**:

```json
{
  "message": "Invalid ticket ID"
}
```

**Error response**:
- Condition: Se il Content-Type fornito non è supportato (né application/json né application/x-www-form-urlencoded).
- Code : `415 UNSUPPORTED MEDIA TYPE`

**Content Example**:

```json
{
  "message": "Unsupported Content-Type"
}
```

