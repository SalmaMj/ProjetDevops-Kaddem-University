pipeline {
    agent any

    tools {
        maven 'M2_HOME' // Ensure 'M2_HOME' is configured in Jenkins global tools
    }

    environment {
        DOCKER_IMAGE = 'salma508/kaddem-app:latest'
        DOCKER_IMAGE1 = 'salma508/kaddem-frontend:latest'
        DOCKER_USER = 'salma508'
        SONAR_PROJECT_KEY = 'kaddem' 
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar_token') 
        NEXUS_URL = 'http://192.168.33.10:8081/repository/maven-releases/'
        NEXUS_CREDENTIALS_ID = 'nexus_credentials'
    }

    stages {
        stage('Checkout Git repository') {
            steps {
                git branch: 'main', credentialsId: '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', url: 'https://github.com/SalmaMj/Devops-automation'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test with JaCoCo') {
            steps {
                sh 'mvn clean jacoco:prepare-agent test'
            }
        }

        stage('Generate JaCoCo Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        stage('Publish JaCoCo Report') {
            steps {
                step([$class: 'JacocoPublisher',
                      execPattern: '**/target/jacoco.exec',
                      classPattern: '**/target/classes',
                      sourcePattern: '**/src/main/java',
                      exclusionPattern: '**/target/**,**/*Test*,**/*_javassist/**'
                ])
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('Sonar') { 
                    sh 'mvn sonar:sonar ' +
                       "-Dsonar.projectKey=${env.SONAR_PROJECT_KEY} " +
                       "-Dsonar.host.url=${env.SONAR_HOST_URL} " +
                       "-Dsonar.login=${env.SONAR_TOKEN}"
                }
            }
        }

        stage('Nexus Deployment') {
            steps {
                sh 'mvn deploy'
            }
        }

      stage('Build Frontend') {
            steps {
                echo 'Building the Angular frontend using Docker...'
                script {
                    dir('front') {
                        sh 'sudo npm install'
                        sh 'sudo ng build --configuration production'
                    }
                }
            }
        }


        stage('Build Frontend Docker Image') {
            steps {
                script {
                    echo 'Building the Docker image for the frontend...'
                    docker.build("${env.DOCKER_IMAGE1}", 'front') // Specify context for Docker build
                }
            }
        }
        
        stage('Push Frontend Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker_credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                        echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USER}" --password-stdin
                        docker push "${DOCKER_IMAGE1}"
                        """
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building the Docker image for the backend...'
                    docker.build("${env.DOCKER_IMAGE}")
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker_credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                        echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USER}" --password-stdin
                        docker push "${DOCKER_IMAGE}"
                        """
                    }
                }
            }
        }

        stage('Docker Compose') {
            steps {
                echo 'Starting up the Docker containers...'
                sh 'docker compose up -d'
            }
        }
    }

    post {
        always {
            script {
                // Cleanup if needed
                echo 'Cleaning up...'
                sh 'docker system prune -f' // Adjust this command based on your cleanup needs
                
                if (currentBuild.result == 'SUCCESS') {
                    echo 'Pipeline succeeded!'
                } else {
                    echo 'Pipeline failed.'
                }
            }
        }
    }
}
