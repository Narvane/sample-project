# Video Platform - Microservices Example

This project is a **study-oriented sample** demonstrating **microservices**, **DDD**, **hexagonal architecture**, **messaging (Kafka)**, **integration with external services**, **OAuth2 authentication**, **circuit breaker**, **tracing**, **monitoring**, **SQL and NoSQL databases**, and a minimal **React/Next.js** front-end.

The scope is intentionally **small**, focusing on a limited set of use cases while showcasing the **complexities** of each technology:
- **Multiple microservices** (Java + Spring Boot and Node.js + Express)
- **Relational database (PostgreSQL)** with **JPA** mappings (OneToOne, OneToMany, ManyToMany)
- **NoSQL (MongoDB)** for video metadata
- **Video streaming** and file uploads
- **Kafka** for asynchronous events
- **OAuth2** for authentication
- **Circuit Breaker**, **Tracing**, and **Monitoring** tools

---

## Table of Contents
1. [Overview of the Project](#overview-of-the-project)  
2. [Architecture & Microservices](#architecture--microservices)  
   - [Gateway](#1-gateway)  
   - [User Service (Java + Spring Boot + PostgreSQL)](#2-user-service-java--spring-boot--postgresql)  
   - [Video Service (Node.js + Express + MongoDB)](#3-video-service-nodejs--express--mongodb)  
   - [Playlist Service (Java + Spring Boot + PostgreSQL)](#4-playlist-service-java--spring-boot--postgresql)  
3. [Front-End (React/Next.js)](#5-front-end-reactnextjs)  
4. [Overall Flow Summary](#6-overall-flow-summary)  
5. [Folder Structure Example](#7-folder-structure-example)  
6. [Technologies and Concepts Checklist](#8-technologies-and-concepts-checklist)  
7. [Complex Relationships in Playlist Service](#9-complex-relationships-in-playlist-service)  
8. [Conclusion](#conclusion)

---

## Overview of the Project

We are building a **simple video sharing platform** with these main goals:

- **Minimal domain** and **minimal endpoints** in each microservice to remain small and finishable.
- **Multiple microservices**:
  1. **User Service (Java + Spring Boot + PostgreSQL)** for user registration, authentication, user profiles, etc.
  2. **Video Service (Node.js + Express + MongoDB)** for managing video uploads, metadata, and streaming.
  3. **Playlist Service (Java + Spring Boot + PostgreSQL)** for demonstrating **complex JPA relationships** (OneToOne, OneToMany, ManyToMany) in a small scope.
  4. **Gateway** (Spring Cloud Gateway or similar) for routing incoming requests and performing OAuth2 token checks, circuit breaking, etc.
- **React/Next.js Front-End** with a minimal interface (login, video listing, video upload, playlist creation, etc.).
- **Kafka** as a messaging system for asynchronous events (e.g., `VIDEO_UPLOADED`, `VIDEO_PROCESSED`).
- **Integration with an external service** (e.g., a fake video transcoding or thumbnail generation API).
- **Circuit Breaker, Tracing, Monitoring** (Resilience4j, Sleuth/Zipkin, Spring Boot Actuator, etc.).

---

## Architecture & Microservices

### 1. Gateway

- **Technology**:
  - Can be **Spring Cloud Gateway**, **Kong**, or **NGINX** (pick what you prefer).
- **Responsibilities**:
  1. **Receiving requests** from the front-end and routing them to:
     - `/api/users/**` → **User Service**  
     - `/api/videos/**` → **Video Service**  
     - `/api/playlists/**` → **Playlist Service**
  2. **Validating OAuth2 tokens** and propagating them to microservices.
  3. **Circuit Breaker** (via Resilience4j or Spring Cloud Circuit Breaker) to handle service unavailability.
  4. **Tracing**: propagate trace headers (Sleuth/Zipkin) for distributed tracing.

### 2. User Service (Java + Spring Boot + PostgreSQL)

#### 2.1 Scope / Domain

- **Main entity**: `User`
  - Basic attributes: `id`, `email`, `name`, `passwordHash` (if storing password locally; optionally rely on Keycloak or another IdP).
- **Minimal Endpoints**:
  - `POST /users` (optional if you want to allow direct registration).
  - `GET /me`: returns the currently authenticated user’s data (using OAuth2 token).
- **Database**:
  - **PostgreSQL** with a `users` table.
- **Authentication (OAuth2)**:
  - Use **Spring Security OAuth2** + JWT or **Keycloak** as an Authorization Server.
  - The **User Service** acts as a Resource Server validating tokens.

#### 2.2 Hexagonal Architecture / DDD

- **Domain Layer**: `User` entity, repository interface, domain services (e.g., “create user”, “find user”).
- **Adapters**:
  - **Controller** for REST endpoints.
  - **JPA Repository** for database interactions.

### 3. Video Service (Node.js + Express + MongoDB)

#### 3.1 Scope / Domain

- **Main entity**: `Video`
  - Fields: `videoId`, `ownerId` (the user ID from User Service), `title`, `description`, `status` (PROCESSING, READY), `createdAt`, etc.
  - Stored in **MongoDB** (NoSQL).
- **Functionalities**:
  1. **Upload** (`POST /videos`): saves metadata in MongoDB, stores the file locally or in a storage bucket (S3, etc.).
  2. **Listing** (`GET /videos?ownerId=...`): lists the user’s videos.
  3. **Streaming** (`GET /videos/:id/stream`): serves the video file in chunks (`Range` headers).

#### 3.2 External Service Integration

- E.g., call an external transcoding service after upload (real or mocked).
- Use a **Circuit Breaker** in the Node.js layer if that external service is down.

#### 3.3 Messaging (Kafka)

- Publishes `VIDEO_UPLOADED` when a new video is uploaded.
- (Optionally) listens for `VIDEO_PROCESSED` to set the video `status` to READY.

#### 3.4 Hexagonal Architecture

- **Domain**: Entities + Repositories (Mongo).
- **Controllers (Express)** + Application Services.
- **Tracing** with Jaeger or Zipkin client in Node.
- **Monitoring**: possible `express-prometheus-middleware` or similar.

### 4. Playlist Service (Java + Spring Boot + PostgreSQL)

This **additional microservice** showcases **more advanced data modeling** with **JPA** (relationships: OneToOne, OneToMany, ManyToMany), without adding too many endpoints.

#### 4.1 Purpose

- Manage **playlists** referencing the `User` by `ownerId` (which comes from the **User Service**).
- Exposes minimal operations that demonstrate complex relationships in a small domain.

#### 4.2 Entities (Example)

1. **Playlist**  
   - `id` (PK)  
   - `name` (String)  
   - `ownerId` (Long or UUID, referencing User Service)  
   - **Relationships**:
     - **OneToOne** → `PlaylistMetadata`
     - **OneToMany** → `List<PlaylistItem>`
     - **ManyToMany** → `Set<Tag>`

2. **PlaylistMetadata**  
   - `id` (PK)  
   - `description` (String)  
   - `createdAt` (LocalDateTime)  
   - Possibly other fields as needed  
   - OneToOne link back to `Playlist`.

3. **PlaylistItem**  
   - `id` (PK)  
   - `videoId` (String or UUID referencing the Video in the **Video Service**)  
   - `position` (int)  
   - ManyToOne link to `Playlist`.

4. **Tag**  
   - `id` (PK)  
   - `name` (String)  
   - ManyToMany link with `Playlist` (table `playlist_tags` in the DB).

#### 4.3 JPA Mappings

- **Playlist** ↔ **PlaylistMetadata**:  
  - `@OneToOne(cascade = CascadeType.ALL, mappedBy = "playlist")` or vice versa.
- **Playlist** ↔ **PlaylistItem**:  
  - `@OneToMany(mappedBy = "playlist", fetch = LAZY)` in `Playlist`.  
  - `@ManyToOne(fetch = LAZY)` in `PlaylistItem`.
- **Playlist** ↔ **Tag**:  
  - `@ManyToMany(fetch = LAZY)`, with a join table `playlist_tags`.

#### 4.4 Minimal Endpoints

Focus on just 2 or 3 endpoints to keep it small but show relational complexity:

1. **`POST /playlists`**  
   - **Request Body**: includes `ownerId`, `name`, optional tags, metadata, and items to create in one go.  
   - **Action**: Creates a `Playlist`, `PlaylistMetadata`, related `Tag`s (if they do not exist), and `PlaylistItem`s.  
   - **Response**: Returns the `id` of the new playlist.

2. **`GET /playlists/{id}`**  
   - **Action**: Fetches a playlist by `id` and returns:
     - `name`, `ownerId`
     - `metadata` (OneToOne)
     - `items` (OneToMany, possibly paginated via `?page=...&size=...`)
     - `tags` (ManyToMany)
   - Demonstrates **lazy loading** or custom queries with pagination.

3. (Optional) **`POST /playlists/{id}/items`**  
   - **Action**: Add a new `PlaylistItem` referencing a `videoId` from the **Video Service**.

With just these 2–3 endpoints, you can demonstrate:

- **OneToOne**  
- **OneToMany** (+ pagination)  
- **ManyToMany**  
- **Lazy Loading**  
- Linking external IDs (users, videos) from other services

#### 4.5 DDD & Hexagonal

- **Domain Layer**: entities (`Playlist`, `PlaylistMetadata`, `PlaylistItem`, `Tag`), repository interfaces, domain services (e.g. “create playlist”).  
- **Adapters**:
  - **Controller** for REST.  
  - **Database** with JPA/Hibernate.  
  - (Optional) produce/consume Kafka events if desired.
- **Integration with User Service**:
  - Store `ownerId` in the `Playlist`.  
  - Optionally, call the User Service (with a circuit breaker) to verify the user exists or fetch user details.

---

## 5. Front-End (React/Next.js)

### 5.1 Minimal Pages / Routing

1. **`/login`**  
   - Implements the OAuth2 flow. After login, you get a token (JWT) stored in local storage or cookie.

2. **`/meus-videos`** (My Videos)  
   - Calls `GET /api/videos?ownerId=<userId>` to list the user’s videos.  
   - Links to watch each video.

3. **`/upload`**  
   - A form for uploading a video (calls `POST /api/videos`).

4. **`/playlists`** (optional)  
   - Minimal UI to create or view playlists (calls `POST /api/playlists` or `GET /api/playlists`).

5. **`/playlists/:id`** (optional)  
   - Displays a single playlist (calls `GET /api/playlists/:id`) with associated items.

Use **Next.js** routing and handle private routes by checking the token. Keep it **simple** for demonstration purposes.

---

## 6. Overall Flow Summary

1. **User logs in** → front-end stores the OAuth2 token.
2. Front-end calls the **Gateway** with `Authorization: Bearer <token>`.
3. Gateway routes to the correct microservice:
   - **Circuit Breaker** is active.
   - **Tracing** with Sleuth/Zipkin or Jaeger is propagated.
4. **User Service**:
   - Returns basic user data (`GET /me`).
5. **Video Service**:
   - **Upload** → saves metadata, publishes `VIDEO_UPLOADED` event to Kafka.
   - **List** videos by `ownerId`.
   - **Stream** videos with a `GET /videos/:id/stream`.
6. **Playlist Service**:
   - **Create** a playlist with relationships (OneToOne, ManyToMany, etc.).
   - **Fetch** a playlist (with pagination for items).
   - Optionally, calls **User Service** to validate the `ownerId`.
7. **Monitoring & Logs**:
   - **Spring Boot Actuator** in Java services.
   - Potential metrics library in Node.js.
   - **Zipkin/Jaeger** for distributed tracing.

---

## 7. Folder Structure Example

```plaintext
my-video-platform/
├─ gateway/
│   ├─ src/
│   └─ pom.xml (Spring Cloud Gateway)
├─ user-service/
│   ├─ src/
│   └─ pom.xml (Spring Boot + PostgreSQL)
├─ video-service/
│   ├─ src/
│   ├─ package.json
│   └─ ...
├─ playlist-service/
│   ├─ src/
│   └─ pom.xml (Spring Boot + PostgreSQL, JPA)
├─ front-end/
│   ├─ pages/
│   └─ package.json (React/Next.js)
├─ docker-compose.yml (Kafka, PostgreSQL, MongoDB, etc.)
└─ README.md
```

## 8. Technologies and Concepts Checklist

- **Multiple Microservices**  
  - `User Service` (Java + Spring Boot + PostgreSQL)  
  - `Video Service` (Node.js + Express + MongoDB)  
  - `Playlist Service` (Java + Spring Boot + PostgreSQL, with complex JPA relationships)  
  - `Gateway` for routing

- **DDD + Hexagonal Architecture** in each service

- **Messaging (Kafka)** for asynchronous communication/events

- **NoSQL** (MongoDB) in Video Service, **SQL** (PostgreSQL) in User/Playlist Services

- **External API Integration** (e.g., transcoding or thumbnail service)

- **Circuit Breaker** and **Tracing** (Resilience4j, Sleuth/Zipkin, or Jaeger)

- **React/Next.js Front-End** with minimal routes

- **OAuth2** for authentication (Keycloak or Spring Security OAuth2)

---

## 9. Complex Relationships in Playlist Service

- **Entities**:
  - `Playlist`  
    - OneToOne → `PlaylistMetadata`  
    - OneToMany → `PlaylistItem`  
    - ManyToMany → `Tag`
  - `PlaylistMetadata`
  - `PlaylistItem`
  - `Tag`

**Example**:

```jsonc
// POST /playlists
{
  "ownerId": 123,
  "name": "My Favorite Playlist",
  "metadata": {
    "description": "Various awesome videos"
  },
  "tags": ["Rock", "Pop"],
  "items": [
    { "videoId": "abc-xyz", "position": 1 },
    { "videoId": "def-ghi", "position": 2 }
  ]
}
```

- Creates the main `Playlist` plus any related entities.  
- **GET /playlists/{id}** can return the playlist with metadata, tags, and paginated items, demonstrating **lazy loading** or customized queries.

---

## Conclusion

By keeping each microservice **small** but **distinct** in purpose:

- We demonstrate **DDD**, **hexagonal architecture**, and **microservices** best practices.
- We use **SQL** and **NoSQL**.
- We handle **video uploads/streaming** with a Node.js service.
- We show **complex JPA relationships** (OneToOne, OneToMany, ManyToMany) in the `Playlist Service`.
- We integrate with **Kafka** for asynchronous events.
- We implement **OAuth2** authentication, **circuit breaker**, **distributed tracing**, and **monitoring**.
