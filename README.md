

# Sample Project

## MindMap
https://sharing.clickup.com/9013913601/l/h/5-90135396944-1/65f5a890d2d3e36

## Overview
Portfolio‑grade **microservices video platform** built with Java (Spring Boot), Node.js, and a React / Next.js front‑end.  
Demonstrates clean architecture (DDD + Hexagonal), asynchronous processing with Kafka, and modern observability patterns.

---

## Service Topology

| Service | Tech | Purpose |
|---------|------|---------|
| **gateway** | Spring Cloud Gateway | Single ingress – routing, OAuth2 token relay |
| **user‑service** | Spring Boot + Postgres | Stores Keycloak user ID and profile data |
| **video‑service** | Spring Boot + Mongo | Uploads, metadata, feed, streaming, **playlists (delivery 2)** |
| **processing‑video‑service** | Node.js + Express + Kafka | Consumes `VIDEO_UPLOADED`, generates thumbnails, marks video processed |
| **scheduling‑service** | Spring Boot + Postgres | Generates publication time‑slots & validates schedules |
| **front‑end** | Next.js (React 18 + TypeScript) | SPA with protected routes & video player |

---

## High‑Level Flow – Upload → Process → Publish

1. **Front‑end** hits `POST /videos` on **video‑service** via the **gateway**.  
2. **video‑service** stores the temporary file, writes metadata to Mongo (`status=UPLOADING`), and emits a `VIDEO_UPLOADED` Kafka event.  
3. **processing‑video‑service** consumes the event, performs lightweight processing (e.g., thumbnail service), then `PATCH /videos/{id}/status` → `PROCESSED`.  
4. The public feed (`GET /videos/feed`) returns only `PUBLISHED` items. **My Uploads** shows every status.

---

## Technology & Concepts Used

* **Spring Boot 3 / Java 21** – REST APIs, JPA, observability.
* **Node.js 18 + Express** – event consumer & processing.
* **Next.js 14 + React 18 + TypeScript** – front‑end SPA, server components, React Query.
* **Kafka** – decoupled event pipeline.
* **MongoDB & PostgreSQL** – polyglot persistence (document vs. relational).
* **Spring Cloud Gateway** – token relay & routing.
* **Resilience4j** – circuit breakers (Delivery 3).
* **Spring Cloud Sleuth & Zipkin** – distributed tracing (Delivery 3).
* **Docker Compose** – local‑dev orchestrator.
* **DDD + Hexagonal** – ports/adapters isolation, clear domain boundaries.

---

## Roadmap & Deliveries

| Delivery | Scope |
|----------|-------|
| **1. Core UX & Upload Pipeline** | Login / logout, Home feed, upload modal, My Uploads, streaming page, upload + processing pipeline |
| **2. Scheduling & Playlists** | Admin page for time slots, scheduling endpoints, **playlist CRUD in video‑service** & UI |
| **3. Observability & Resilience** | Circuit breaker, distributed tracing, metrics dashboards, UI polish |
