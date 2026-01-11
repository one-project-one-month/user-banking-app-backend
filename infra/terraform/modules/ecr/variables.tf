variable "project_name" {
  description = "test"
  type        = string
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "repositories" {
  description = "List of ECR repository names"
  type        = list(string)
}
