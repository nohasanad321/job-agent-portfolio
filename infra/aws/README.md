# AWS ECS Fargate deployment design — NOT deployed

**Status:** infrastructure *design* with automated static validation. There is no AWS deployment, live URL, AWS runtime test, CloudWatch evidence, or spending claim.

## Proposed path

```text
Source → GitHub Actions (Maven tests / Docker build)
       → Amazon ECR (versioned application image)
       → ECS Fargate service (Java 21 application on port 8083)
       ← Internet-facing ALB (HTTP 80 for DEMO ONLY)
       → CloudWatch Logs (7-day retention)
       ← Optional Redis (not provisioned; built-in fallback remains available)
```

The code models an ALB, ECS cluster/service/task definition, ECR repository, CloudWatch log group, IAM execution role and security groups. It **reuses an existing VPC and at least two public subnets**. Public IP task networking avoids a NAT gateway in this demonstration architecture. Task inbound traffic is restricted to the load balancer security group.

The task needs outbound internet access to pull images / reach public endpoints. Existing subnets therefore need the correct route and security settings. Default sample sizes are **0.25 vCPU / 512 MiB** for a single task.

## Free checks, no account or credentials

Run from `infra/aws`:

```sh
terraform fmt -check
terraform init -backend=false
terraform validate
```

These commands require local Terraform and provider installation, **but do not create AWS resources**. They do not prove IAM permissions, account quotas, runtime connectivity, a valid image, or launch success. GitHub CI runs this validation.

No `terraform apply`, ECR push, or live deployment is automated.

## Hypothetical deployment (not executed)

1. Choose an AWS account/region, existing VPC and public subnets in two different AZs; review all cost-producing resources.
2. Run `terraform init` / `terraform validate`; create ECR ahead of the service or provision it separately (ECR repository is also modeled here).
3. Build a version-tagged application image with Docker, authenticate to ECR and push the image. Supply that image URI as `image_uri`.
4. Provide `vpc_id`, `public_subnet_ids`, `image_uri`, `aws_region` and run an approved `terraform plan`.
5. Only after cost/security review, apply in an AWS account. Verify ECS task starts, ALB target health, `/actuator/health`, `/api/demo/jobs`, and CloudWatch logs.
6. Roll back by referencing the previous image/task revision; delete resources through a reviewed `terraform destroy` when finished.

The sample ALB listener is HTTP for explanatory clarity only. **Production requires HTTPS/ACM, access restrictions, WAF/rate limiting as applicable, private subnets with properly budgeted networking or VPC endpoints, least-privilege IAM, security scanning, and monitoring/alerting.**

## Cost and safety

Static Terraform checks and source-only GitHub CI **do not provision resources**. Fargate tasks, public IPv4 addresses, the application load balancer, ECR storage, CloudWatch ingestion/storage, and network traffic may all incur cost after an actual deployment. A NAT gateway is deliberately **not** modeled; none of this implies a deployed setup would be free.

Use the [AWS Pricing Calculator](https://calculator.aws/) and verify regional pricing first. Keep `desired_count=0` only for deliberately paused services; an ALB can still cost money. No credentials, real account identifiers, terraform state or secrets should ever be committed.

## Evidence table

| Claim | Evidence | Status |
|---|---|---|
| Java application compiles and tests pass | GitHub Actions Maven workflow | Verified |
| Docker image can run with Redis | Dockerfile/Compose exist, runtime verification pending | Not verified |
| ECS task configuration described | Terraform code and validation workflow | Static validation only |
| Service can be deployed to AWS | Requires account-specific plan, permissions and real task | Not verified |
| CloudWatch receives logs | Requires running task | Not verified |

This is a transparent **AWS deployment readiness design**, not a deployed application.
