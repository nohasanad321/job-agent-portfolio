terraform {
  required_version = ">= 1.7.0"
  required_providers {
    aws = { source = "hashicorp/aws", version = "~> 5.0" }
  }
}

provider "aws" {
  region = var.aws_region
}

variable "aws_region" {
  description = "AWS region used for all resources."
  type        = string
  default     = "eu-west-1"
}
variable "project_name" {
  type    = string
  default = "job-agent-portfolio"
}
variable "vpc_id" {
  description = "Existing VPC ID. No networking is provisioned by this example."
  type        = string
}
variable "public_subnet_ids" {
  description = "At least two existing public subnets in distinct availability zones, with an internet gateway route."
  type        = list(string)
  validation {
    condition     = length(var.public_subnet_ids) >= 2
    error_message = "Provide at least two public subnet IDs."
  }
}
variable "image_uri" {
  description = "Existing ECR container image URI with immutable digest or version tag, built and pushed separately."
  type        = string
}
variable "desired_count" {
  type    = number
  default = 1
  validation {
    condition     = var.desired_count >= 0 && var.desired_count <= 5
    error_message = "desired_count must be 0 through 5."
  }
}

resource "aws_ecr_repository" "api" {
  name                 = var.project_name
  image_tag_mutability = "IMMUTABLE"
  image_scanning_configuration { scan_on_push = true }
}

resource "aws_cloudwatch_log_group" "api" {
  name              = "/ecs/${var.project_name}"
  retention_in_days = 7
}

resource "aws_iam_role" "execution" {
  name = "${var.project_name}-execution"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "execution" {
  role       = aws_iam_role.execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_ecs_cluster" "api" {
  name = var.project_name
}

resource "aws_security_group" "alb" {
  name        = "${var.project_name}-alb"
  description = "Public HTTP demo access"
  vpc_id      = var.vpc_id
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "task" {
  name        = "${var.project_name}-task"
  description = "Only ALB may contact the application port"
  vpc_id      = var.vpc_id
  ingress {
    from_port       = 8083
    to_port         = 8083
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb" "api" {
  name               = var.project_name
  load_balancer_type = "application"
  internal           = false
  subnets            = var.public_subnet_ids
  security_groups    = [aws_security_group.alb.id]
}

resource "aws_lb_target_group" "api" {
  name        = var.project_name
  port        = 8083
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = var.vpc_id
  health_check {
    enabled             = true
    path                = "/actuator/health"
    matcher             = "200"
    healthy_threshold   = 2
    unhealthy_threshold = 3
    interval            = 30
  }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.api.arn
  port              = 80
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.api.arn
  }
}

resource "aws_ecs_task_definition" "api" {
  family                   = var.project_name
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.execution.arn
  container_definitions = jsonencode([{
    name      = "api"
    image     = var.image_uri
    essential = true
    portMappings = [{
      containerPort = 8083
      hostPort      = 8083
      protocol      = "tcp"
    }]
    environment = [{ name = "PORT", value = "8083" }]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.api.name
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "api"
      }
    }
  }])
}

resource "aws_ecs_service" "api" {
  name            = var.project_name
  cluster         = aws_ecs_cluster.api.id
  task_definition = aws_ecs_task_definition.api.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"
  network_configuration {
    subnets          = var.public_subnet_ids
    security_groups  = [aws_security_group.task.id]
    assign_public_ip = true
  }
  load_balancer {
    target_group_arn = aws_lb_target_group.api.arn
    container_name   = "api"
    container_port   = 8083
  }
  depends_on = [aws_lb_listener.http, aws_iam_role_policy_attachment.execution]
}

output "ecr_repository_url" { value = aws_ecr_repository.api.repository_url }
output "load_balancer_dns_name" { value = aws_lb.api.dns_name }
output "ecs_cluster_name" { value = aws_ecs_cluster.api.name }
