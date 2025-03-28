# Video Platform - Microservices Example

This project is a study-oriented sample designed to demonstrate modern technologies and architectural concepts by building a video-sharing platform. It leverages microservices, DDD, hexagonal architecture, messaging with Kafka, external API integration, OAuth2 authentication, circuit breaker, tracing, monitoring, and a combination of SQL (PostgreSQL) and NoSQL (MongoDB) databases. In addition, the project includes a sophisticated video publication scheduling module that generates available time slots based on configurable time intervals and gaps.

* * *

## 1\. Project Overview

### Domain

A video-sharing platform with two main bounded contexts:

-   **User Management**: Handles user registration, authentication, and profile data.
-   **Video Catalog and Scheduling**: Manages video uploads, listing, streaming, and publication scheduling.

### Key Features

-   **Microservices**:
    -   **User Service (Java + Spring Boot + PostgreSQL)**
    -   **Video Service (Node.js + Express + MongoDB)**
    -   **Playlist & Scheduling Service (Java + Spring Boot + PostgreSQL)** – manages playlists with complex JPA relationships and handles the scheduling logic.
    -   **Gateway**: Routes incoming requests to the appropriate service while managing OAuth2, circuit breaker, and tracing.
-   **Front-End (React/Next.js)**: A minimalist interface with screens for login, video upload, video listing, scheduling, and playlist management.
-   **Video Publication Scheduling**:
    -   Users can configure time intervals (e.g., 08:00 to 12:00 and 14:00 to 17:00) and a gap duration (e.g., 10 minutes).
    -   The system automatically generates a list of available slots (e.g., 08:00, 08:10, …, 11:50, then 14:00, 14:10, …, 16:50).
    -   Users can select a slot to schedule when a video should be published.
* * *

## 2\. Architecture & Microservices

### 2.1 Gateway

-   **Technology**: Spring Cloud Gateway (Java) or a similar reverse proxy (e.g., Kong, NGINX).
-   **Responsibilities**:
    -   Route requests:
        -   `/api/users/**` → User Service
        -   `/api/videos/**` → Video Service
        -   `/api/playlists/**` → Playlist & Scheduling Service
    -   Validate OAuth2 tokens (using Spring Security or Keycloak).
    -   Implement circuit breaker functionality (Resilience4j or Spring Cloud Circuit Breaker).
    -   Propagate distributed tracing headers (using Spring Cloud Sleuth/Zipkin).
* * *

### 2.2 User Service (Java + Spring Boot + PostgreSQL)

### Domain & Scope

-   **Primary Entity**: `User`
    -   Attributes: `id`, `email`, `name`, `passwordHash` (or integrate with Keycloak for OAuth2).
-   **Endpoints**:
    -   `POST /users` – Registration (optional if an external IdP is used).
    -   `GET /me` – Returns the authenticated user’s data.
-   **Architecture**:
    -   Built using DDD and hexagonal architecture.
    -   Adapters include REST controllers and JPA repositories (using Spring Data).
* * *

### 2.3 Video Service (Node.js + Express + MongoDB)

### Domain & Scope

-   **Primary Entity**: `Video`
    -   Attributes: `videoId`, `ownerId` (user reference), `title`, `description`, `status` (PROCESSING, READY), `createdAt`, `scheduledTime`, etc.
-   **Functionalities**:
    -   **Video Upload**: `POST /videos`
        -   Saves metadata in MongoDB.
        -   Stores the file locally or in an external storage service.
        -   Publishes a `VIDEO_UPLOADED` event via Kafka.
    -   **List Videos**: `GET /videos?ownerId=...`
    -   **Video Streaming**: `GET /videos/:id/stream`
    -   **Video Scheduling**: `POST /videos/:id/schedule`
        -   Allows scheduling the publication of a video by selecting one of the available time slots.
-   **Kafka Integration**:
    -   Publishes and consumes events for video processing.
* * *

### 2.4 Playlist & Scheduling Service (Java + Spring Boot + PostgreSQL)

### Domain & Scope

This service demonstrates advanced data modeling with JPA and incorporates the video scheduling functionality.

-   **Complex Relationships**:
    -   **Playlist** entity:
        -   **OneToOne**: with `PlaylistMetadata`
        -   **OneToMany**: with `PlaylistItem`
        -   **ManyToMany**: with `Tag`
-   **Scheduling of Publication**:
    -   **User Scheduling Configuration**:
        -   Endpoint to configure available time intervals and gap duration.
    -   **Time Slot Generation**:
        -   Generates available publication slots based on the user’s configuration.
    -   **Scheduling a Video**:
        -   Validates and assigns a publication time to a video.

### Detailed Endpoints

1.  **Schedule Configuration**
    -   `POST /schedule/config`
    -   **Request Body**:

        ```json
        {
          "ownerId": "user123",
          "intervals": [
            { "start": "08:00", "end": "12:00" },
            { "start": "14:00", "end": "17:00" }
          ],
          "gapMinutes": 10
        }
        ```

-   **Function**: Saves the user's scheduling settings, validating the intervals and gap.
1.  **Generate Available Time Slots**
-   `GET /schedule/options?ownerId=user123&date=2025-04-01`
-   **Response Example**:

    ```json
    {
      "date": "2025-04-01",
      "availableSlots": [
        "2025-04-01T08:00:00",
        "2025-04-01T08:10:00",
        "...",
        "2025-04-01T11:50:00",
        "2025-04-01T14:00:00",
        "2025-04-01T14:10:00",
        "...",
        "2025-04-01T16:50:00"
      ]
    }
    ```

-   **Logic**: Iterates over each interval using the provided gap to generate a complete list of valid time slots.
1.  **Schedule a Video**
-   `POST /videos/:id/schedule`
-   **Request Body**:

```json
{
  "scheduledTime": "2025-04-01T14:10:00"
}
```

-   **Function**: Verifies the selected slot against available options and updates the video’s metadata with the scheduled publication time.

### Integration with Playlists

-   The service aggregates scheduled videos into playlists.
-   Playlists display videos along with their scheduled publication times.
-   Demonstrates advanced JPA relationships with lazy loading and pagination.
* * *

## 3\. Front-End (React/Next.js)

### Pages & Features

-   **Login (/login)**:
    -   Implements the OAuth2 flow.
    -   Stores the token (using localStorage or cookies).
-   **My Videos (/meus-videos)**:
    -   Lists the user’s videos (fetched via the Gateway from the Video Service).
    -   Displays the status (processing, scheduled, published).
-   **Video Upload (/upload)**:
    -   A form for uploading videos (title, description, file selection).
    -   After upload, allows the user to configure scheduling options.
-   **Video Scheduling (/videos/:id/schedule)**:
    -   Provides an interface where the user can:
        -   Configure available time intervals (if not already set).
        -   View available time slots (fetched via `GET /schedule/options`).
        -   Select a slot to schedule the video.
-   **Playlist Management (/playlists and /playlists/\[id\])**:
    -   Create and view playlists.
    -   Each playlist displays videos with their scheduled publication times.
    -   Implements pagination for playlist items.
-   **Routing & Auth Guards**:
    -   Use Next.js routing with `getServerSideProps` or client-side checks to ensure only authenticated users access protected pages.
* * *

## 4\. Overall System Flow

1.  **User Authentication**:
    -   The user logs in via the front-end, obtains an OAuth2 token, which is sent in all API calls through the Gateway.
2.  **User Service**:
    -   Manages user registration and authentication, providing secure access to the system.
3.  **Video Service**:
    -   Handles video uploads and streaming.
    -   Publishes `VIDEO_UPLOADED` events and integrates video scheduling.
4.  **Playlist & Scheduling Service**:
    -   Manages playlists and complex data relationships.
    -   Handles configuration of scheduling intervals and gap.
    -   Generates available publication slots and assigns scheduled times to videos.
5.  **Gateway**:
    -   Routes requests, validates tokens, applies circuit breakers, and propagates tracing headers.
6.  **Front-End (Next.js)**:
    -   Provides interfaces for all user interactions (login, video upload, scheduling, and playlist management).

## 5\. Example Folder Structure

```perl
my-video-platform/
├─ gateway/                 # Spring Cloud Gateway
│   ├─ src/
│   └─ pom.xml
├─ user-service/            # Java, Spring Boot, PostgreSQL
│   ├─ src/
│   └─ pom.xml
├─ video-service/           # Node.js, Express, MongoDB
│   ├─ src/
│   ├─ package.json
│   └─ ...
├─ playlist-scheduling/     # Java, Spring Boot, PostgreSQL, JPA (Playlists & Scheduling)
│   ├─ src/
│   └─ pom.xml
├─ front-end/               # React/Next.js
│   ├─ pages/
│   └─ package.json
├─ docker-compose.yml       # Kafka, PostgreSQL, MongoDB, etc.
└─ README.md

```

## 6\. Technologies and Concepts Checklist

-   **Microservices**:
    -   User Service (Java + Spring Boot + PostgreSQL)
    -   Video Service (Node.js + Express + MongoDB)
    -   Playlist & Scheduling Service (Java + Spring Boot + PostgreSQL, JPA)
    -   Gateway for routing
-   **DDD + Hexagonal Architecture** in every service
-   **Kafka Messaging** for asynchronous communication
-   **NoSQL** (MongoDB) for video metadata, **SQL** (PostgreSQL) for users and playlists
-   **External API Integration** (e.g., transcoding or thumbnail generation)
-   **Circuit Breaker** and **Tracing** (using Resilience4j, Sleuth/Zipkin, or Jaeger)
-   **OAuth2 Authentication** (using Keycloak or Spring Security OAuth2)
-   **React/Next.js Front-End**
-   **Complex JPA Relationships** (OneToOne, OneToMany, ManyToMany) with lazy loading and pagination
-   **Video Upload & Streaming**
-   **Configurable Publication Scheduling**:
    -   Scheduling configuration (intervals and gap)
    -   Automatic time slot generation
    -   Video scheduling endpoint
