# Job Agent Portfolio — Standalone API Demo

A **public, self-contained Spring Boot 3.5 / Java 21** project demonstrating a synthetic job-discovery API, filtering, pagination, Redis cache-aside behavior, graceful operation when Redis is unavailable, containerization and automated endpoint tests.

**All job listings are fictional samples.** This repository does not retrieve real jobs, handle personal profiles, rank candidates, expose private matching rules, or depend on the private `job-agent-core` repository.

## Quick start

```bash
./mvnw clean test
./mvnw spring-boot:run
```
Windows: `mvnw.cmd clean test` and `mvnw.cmd spring-boot:run`. Requires Java 21.

```bash
curl 'http://localhost:8083/api/demo/jobs?region=Worldwide&skill=Java&page=0&size=5'
curl 'http://localhost:8083/api/demo/jobs/DEMO-001'
curl 'http://localhost:8083/actuator/health'
```

## Run with Redis

```bash
docker compose up --build
```

Redis is optional locally. The demo catalog is kept in memory, and cache failures fall back to it. The Redis health indicator is excluded so missing optional Redis does not mark the API unhealthy. Cache entries expire after 10 minutes.

## API

- `GET /api/demo/jobs`: optional query, region, type, seniority, skill, page (0-based), size (1–100)
- `GET /api/demo/jobs/{id}`: one fictional job, or HTTP 404
- `GET /actuator/health`: operational health

Responses include `synthetic: true` and `source: DEMO_DATA_ONLY`.

## Architecture

```text
HTTP client → REST Controller → Filter Service → Redis (optional cache)
                                     ↘ Synthetic in-memory catalog
```

Controllers validate paging. Services filter against a known catalog. Cache is best-effort and only caches the whole synthetic catalog, not user-supplied queries. There is no account data, authentication or external provider integration.

## Status and next milestones

- [x] Standalone public source — no private library dependency
- [x] Synthetic catalog and REST filtering/pagination
- [x] Optional Redis-backed cache-aside with local fallback
- [x] Automated API tests
- [x] Docker / Compose definition
- [ ] Verify build/tests in a Java 21 runtime and exercise Docker Compose (changes were committed without executing tests in this environment)
- [ ] GitHub Actions CI
- [ ] AWS ECR / ECS Fargate deployment and CloudWatch observability
- [ ] Architecture diagram, screenshots, deployment evidence, and portfolio write-up

The separate private personal job agent is intentionally out of scope.
