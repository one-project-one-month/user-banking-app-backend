variable "project_name" {
  description = "test"
  type        = string
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "github_org" {
  description = "GitHub organization or username"
  type        = string
}

variable "github_repo" {
  description = "GitHub repository name"
  type        = string
}

variable "eks_cluster_name" {
  description = "EKS cluster name"
  type        = string
}

variable "eks_cluster_arn" {
  description = "EKS cluster ARN"
  type        = string
}

variable "ecr_repository_arns" {
  description = "List of ECR repository ARNs"
  type        = list(string)
}
