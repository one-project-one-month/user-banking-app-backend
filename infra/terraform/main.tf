# VPC Module
module "vpc" {
  source = "./modules/vpc"

  project_name = var.project_name
  environment  = var.environment
  vpc_cidr     = var.vpc_cidr
}

# ECR Module
module "ecr" {
  source = "./modules/ecr"

  project_name = var.project_name
  environment  = var.environment
  repositories = var.ecr_repositories
}

# EKS Module
module "eks" {
  source = "./modules/eks"

  project_name        = var.project_name
  environment         = var.environment
  cluster_name        = var.cluster_name
  cluster_version     = var.cluster_version
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  node_instance_types = var.node_instance_types
  node_desired_size   = var.node_desired_size
  node_min_size       = var.node_min_size
  node_max_size       = var.node_max_size
}

# IAM Module for GitHub Actions
module "github_iam" {
  source = "./modules/iam"

  project_name        = var.project_name
  environment         = var.environment
  github_org          = var.github_org
  github_repo         = var.github_repo
  eks_cluster_name    = module.eks.cluster_name
  eks_cluster_arn     = module.eks.cluster_arn
  ecr_repository_arns = module.ecr.repository_arns
}


# Kubernetes Namespace
resource "kubernetes_namespace" "production" {
  metadata {
    name = "production"
    labels = {
      environment = "production"
      managed-by  = "terraform"
    }
  }

  depends_on = [module.eks]
}
