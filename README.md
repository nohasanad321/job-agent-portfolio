
# Job Agent Portfolio

A standalone Java 21 / Spring Boot 3.5 REST API demonstrating layered backend architecture, searchable synthetic job data, pagination, automated testing, Docker containerization, and infrastructure-as-code for AWS.

The application is self-contained and does not depend on private repositories or paid cloud services.

## Features

- REST endpoints for a synthetic job catalog
- Text and location filtering
- Pagination with response metadata
- Layered controller, service, repository, and model structure
- Automated application, controller, and service tests
- Multi-stage Docker build
- Docker Compose environment with Redis
- AWS infrastructure design using Terraform
- GitHub Actions for Java verification and Terraform validation

All job listings are fictional. No live recruitment providers are connected.

## Technology

- Java 21
- Spring Boot 3.5.7
- Maven Wrapper
- Spring Web and Actuator
- JUnit and Spring Boot Test
- Docker and Docker Compose
- Redis 7 (container provisioned; caching not implemented)
- Terraform and AWS ECS/Fargate deployment design
- GitHub Actions

## API

### Search jobs

`GET /api/demo/jobs`

Supported query parameters:

| Parameter | Purpose |
| --- | --- |
| `q` | Text search |
| `location` | Location filter |
| `page` | Zero-based page index |
| `size` | Page size |

Example:

```bash
curl "http://localhost:8083/api/demo/jobs?page=0&size=2"
```

The response includes `jobs`, `page`, `size`, `totalElements`, and `totalPages`.

### Get a job

`GET /api/demo/jobs/{id}`

Example:

```bash
curl http://localhost:8083/api/demo/jobs/demo-001
```

### Health check

`GET /actuator/health`

## Run locally

Requirements: JDK 21.

On Windows PowerShell:

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run
```

The application runs on port `8083` by default.

## Run with Docker Compose

Requirements: Docker Desktop with Docker Compose.

```bash
docker compose up --build -d
```

The API is exposed on host port `8084`, mapped to container port `8083`.

```bash
curl http://localhost:8084/actuator/health
curl "http://localhost:8084/api/demo/jobs?page=0&size=2"
```

Stop the containers:

```bash
docker compose down
```

Docker Compose also starts Redis. Redis is available for future caching work but is not currently used by the job search implementation.

## Architecture

```text
HTTP Client
    |
    v
REST Controller
    |
    v
Job Service
    |
    v
Demo Repository
    |
    v
Synthetic Job Catalog
```

The application separates HTTP handling, business logic, data access, and response models.

## Testing and CI

Run all tests:

```bash
./mvnw clean verify
```

The `.github/workflows/ci.yml` workflow runs Maven verification on pushes and pull requests targeting `main`.

## AWS deployment design

The `infra/aws` directory contains Terraform infrastructure configuration intended to demonstrate AWS deployment planning.

The `.github/workflows/aws-readiness.yml` workflow initializes Terraform without a backend and validates configuration syntax and provider schemas.

**No AWS infrastructure has been provisioned as part of this project.** Terraform validation demonstrates configuration readiness, not a successful live deployment or runtime performance.

See [AWS infrastructure documentation](infra/aws/README.md) for details.

## Scope and limitations

This repository is a backend portfolio demonstration, not a production job-search platform.

It does not currently include:

- Live job-provider integrations
- User accounts or authentication
- Persisted search preferences
- Implemented Redis caching
- A frontend
- A live AWS deployment

These capabilities are deliberately outside the current demonstration scope.
