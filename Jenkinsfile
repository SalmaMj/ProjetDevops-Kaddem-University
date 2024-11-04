pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    environment {
        DOCKER_IMAGE = 'salma508/kaddem-app:latest'
        DOCKER_IMAGE1 = 'salma508/kaddem-frontend:latest'
        DOCKER_USER = 'salma508'
        SONAR_PROJECT_KEY = 'kaddem-SalmaMEJRI'
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar_token')
        NEXUS_CREDENTIALS_ID = 'nexus_credentials'
        GROUP_ID = 'tn.esprit.spring'
        VERSION = '0.0.1-SNAPSHOT'
        NEXUS_URL = '192.168.33.10:8081'
        NEXUS_REPOSITORY = 'KaddemUniversite_SalmaMEJRI_5Arctic4'
    }

    stages {
        stage('Checkout Git repository') {
            steps {
                git branch: 'main', url: 'https://github.com/SalmaMj/Devops-automation.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test with JaCoCo') {
            steps {
                sh 'mvn test'
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

        stage('Deploying to Nexus') {
            steps {
                script {
                    echo "Deploying to Nexus..."
                    echo "Using Nexus URL: ${NEXUS_URL}"

                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: "${NEXUS_URL}",
                        groupId: "${GROUP_ID}",
                        version: "${VERSION}",
                        repository: "${NEXUS_REPOSITORY}",
                        credentialsId: "${NEXUS_CREDENTIALS_ID}",
                        artifacts: [
                            [
                                artifactId: 'kaddem',
                                classifier: '',
                                file: "target/kaddem-${env.VERSION}.jar",
                                type: 'jar'
                            ]
                        ]
                    )

                    echo "Deployment to Nexus completed!"
                }
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
                    docker.build("${env.DOCKER_IMAGE1}", 'front') 
                }
            }
        }

        stage('Trivy Scan Frontend Image') {
            steps {
                script {
                    echo 'Running Trivy scan for frontend image...'
                    sh "trivy image --severity HIGH,CRITICAL ${DOCKER_IMAGE1}"
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

        stage('Trivy Scan Backend Image') {
            steps {
                script {
                    echo 'Running Trivy scan for backend image...'
                    sh "trivy image --severity HIGH,CRITICAL ${DOCKER_IMAGE}"
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

        stage('Email Notification') {
            steps {
                mail bcc: '', 
                     body: '''\
                        Bonjour,
                        
                        Voici le rapport de l'état de la pipeline :
                        
                        - **Git Pull**: Réussi
                        - **Maven Build**: Réussi
                        - **Tests**: Réussi
                        - **SonarQube Analysis**: Réussi
                        - **Déploiement Nexus**: Réussi
                        - **Construction Frontend**: Réussi
                        - **Scan Trivy Frontend**: Réussi
                        - **Construction Backend**: Réussi
                        - **Scan Trivy Backend**: Réussi
                        - **Déploiement Docker**: Réussi
                        
                        **Résumé**: La pipeline a été complétée avec succès.
                        
                        Cordialement,
                        L'équipe DevOps-SalmaMEJRI
                        ''',
                     cc: '', 
                     from: '', 
                     replyTo: '', 
                     subject: 'Rapport de Pipeline - Kaddem Université', 
                     to: 'mejrisalma01@gmail.com'
            }
        }
    }

    post {
        always {
            script {
                echo 'Cleaning up...'
                sh 'docker system prune -f'

                if (currentBuild.result == 'SUCCESS') {
                    echo 'Pipeline succeeded!'
                } else {
                    echo 'Pipeline failed.'
                }
            }
        }
    }
}
