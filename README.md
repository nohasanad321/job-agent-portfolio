# Job Agent Portfolio

Independent Spring Boot application skeleton for eventually demonstrating user-configurable job searches, Spring Boot architecture, Redis caching, and AWS ECS Fargate deployment. It uses `job-agent-core:0.1.0-SNAPSHOT`, starts without Redis or AWS credentials, and does not fetch live job feeds during startup. No personal preferences, personal configuration, or placeholder search endpoint are included.

## Future search preferences

Configurable searches should eventually accept user-supplied roles, skills, seniority, work arrangement, location eligibility, work-from-anywhere requirements, employment type, and salary amount, currency, and period. This repository does not yet implement configurable searches, authentication, saved accounts, or a frontend.

## Local build order

Run these Windows PowerShell commands in order. Install core locally before building either application:

```powershell
Set-Location 'D:\OnlineWork\job-agent-core'
.\mvnw.cmd clean install

Set-Location 'D:\OnlineWork\remote-job-agent'
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run

Set-Location 'D:\OnlineWork\job-agent-portfolio'
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

Build both applications first, then run each `spring-boot:run` command in a separate PowerShell window; the personal app uses port `8082`, while this app uses port `8083`. Its health endpoint is `http://localhost:8083/actuator/health`. Each repository contains its own Maven Wrapper.

Separate repositories consume a versioned core artifact. Local Maven installation is the initial development workflow; remote CI and AWS builds will later need access to a published core artifact or an explicit core checkout/build step.

## Future work

- Diagnose the personal application's known Arbeitnow `502 Bad Gateway` using the real server exception.
- Implement configurable portfolio searches against user-supplied preferences.
- Add Redis locally through Docker Compose.
- Preserve last successful provider data when refresh fails, with explicit freshness and stale-retention rules.
- Containerize and deploy the portfolio application to AWS ECS Fargate.
- Add AWS-hosted Redis later.
