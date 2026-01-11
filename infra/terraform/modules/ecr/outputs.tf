output "registry_url" {
  description = "ECR registry URL"
  value       = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${data.aws_region.current.name}.amazonaws.com"
}

output "repository_urls" {
  description = "Map of repository names to URLs"
  value = {
    for repo in var.repositories :
    repo => aws_ecr_repository.repositories[repo].repository_url
  }
}

output "repository_arns" {
  description = "List of repository ARNs"
  value       = [for repo in aws_ecr_repository.repositories : repo.arn]
}
