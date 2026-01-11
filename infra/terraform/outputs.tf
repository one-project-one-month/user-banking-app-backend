output "ecr_registry_url" {
  description = "ECR registry URL for Docker images"
  value       = module.ecr.registry_url
}

output "ecr_repository_urls" {
  description = "URLs of all ECR repositories"
  value       = module.ecr.repository_urls
}

output "eks_cluster_name" {
  description = "EKS cluster name"
  value       = module.eks.cluster_name
}

output "eks_cluster_endpoint" {
  description = "EKS cluster endpoint"
  value       = module.eks.cluster_endpoint
}

output "eks_cluster_region" {
  description = "AWS region where EKS cluster is deployed"
  value       = var.aws_region
}

output "github_actions_role_arn" {
  description = "IAM role ARN for GitHub Actions"
  value       = module.github_iam.github_actions_role_arn
}

output "vpc_id" {
  description = "VPC ID"
  value       = module.vpc.vpc_id
}

output "kubeconfig_command" {
  description = "Command to update kubeconfig"
  value       = "aws eks update-kubeconfig --region ${var.aws_region} --name ${module.eks.cluster_name}"
}

output "github_secrets_summary" {
  description = "Summary of values to add to GitHub Secrets"
  value = {
    AWS_IAM_ROLE_TO_ASSUME = module.github_iam.github_actions_role_arn
    AWS_ECR_REGISTRY       = module.ecr.registry_url
    AWS_REGION             = var.aws_region
    EKS_CLUSTER_NAME       = module.eks.cluster_name
  }
}
