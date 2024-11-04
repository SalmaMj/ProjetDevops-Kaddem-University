# ESPRIT - DevOps Project - Kaddem-Université (SalmaMejri-Groupe 3) 🏫

Welcome to the *Kaddem* DevOps project repository! This project is part of the final DevOps course at ESPRIT University. Here, we document the work, setup, and automation processes developed for our course requirements.  

## 📑 Table of Contents

- [📘 About the Project](#-about-the-project)
- [🛠 Technologies Used](#-technologies-used)
- [📂 Project Structure](#-project-structure)
- [⚙️ Setup Instructions](#️-setup-instructions)
- [🚀 Running the Project](#-running-the-project)
- [🔄 Continuous Integration and Deployment (CI/CD)](#-continuous-integration-and-deployment-cicd)
- [📊 Monitoring & Observability](#-monitoring--observability)
- [🛠 Troubleshooting](#-troubleshooting)
- [👥 Contributors](#-contributors)

---

## 📘 About the Project

The *Kaddem* project implements a DevOps pipeline for a Spring Boot application, covering areas such as automated testing, containerization, and deployment. This project represents our approach to building robust CI/CD pipelines with monitoring and management solutions.

## 🛠 Technologies Used

- **Programming Languages**: Java (Spring Boot), HTML/CSS (Frontend)
- **Containers**: Docker 🐳
- **Pipeline Orchestration**: Jenkins 🧩
- **Package Management**: Nexus 📦
- **Monitoring**: Prometheus & Grafana 📈
- **Testing**: JUnit, Mockito, JaCoCo 🔍
- **Security Scanning**: Trivy 🛡️
- **Source Control**: GitHub 🐙

## 📂 Project Structure

```plaintext
├── src/                         # Source code for the Spring Boot application
├── docker-compose.yml           # Docker Compose setup for local deployment
├── Jenkinsfile                  # Jenkins CI/CD pipeline configuration
├── README.md                    # Project documentation
└── .github/                     # GitHub-specific files (e.g., workflows, issues)
