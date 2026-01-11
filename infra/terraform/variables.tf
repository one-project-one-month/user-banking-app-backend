variable "aws_region" {
  description = "AWS region for all resources"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Environment name (e.g., production, staging)"
  type        = string
  default     = "development"
}

variable "project_name" {
  description = "Project name used for resource naming"
  type        = string
  default     = "banking-app"
}

variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
  default     = "banking-app-cluster"
}

variable "cluster_version" {
  description = "Kubernetes version for EKS cluster"
  type        = string
  default     = "1.28"
}

variable "node_instance_types" {
  description = "EC2 instance types for EKS nodes"
  type        = list(string)
  default     = ["t3.medium"]
}

variable "node_desired_size" {
  description = "Desired number of worker nodes"
  type        = number
  default     = 3
}

variable "node_min_size" {
  description = "Minimum number of worker nodes"
  type        = number
  default     = 1
}

variable "node_max_size" {
  description = "Maximum number of worker nodes"
  type        = number
  default     = 5
}

variable "github_org" {
  description = "GitHub organization or username"
  default     = "one-project-one-month"
  type        = string
}

variable "github_repo" {
  description = "GitHub repository name"
  type        = string
  default     = "user-banking-app-backend"
}

variable "ecr_repositories" {
  description = "List of ECR repository names"
  type        = list(string)
  default = [
    "gateway-service",
    "nickname-service",
    "personal-transaction-service",
    "personal-user-service",
    "qrtopay"
  ]
}

variable "vpc_cidr" {
  description = "CIDR block for VPC"
  type        = string
  default     = "10.0.0.0/16"
}
