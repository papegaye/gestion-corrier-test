pipeline {

    agent {
        label 'agent-windows'
    }

   /*  tools {
        maven 'Maven-3.9.6'
    } */

    environment {
        // DockerHub
        DOCKERHUB_USER = "test-ci-cd-microservices"
        BACKEND_IMAGE  = "${DOCKERHUB_USER}/backend-courrier"
        FRONTEND_IMAGE = "${DOCKERHUB_USER}/frontend-courrier"
        BACKEND_TAG    = "1.${BUILD_NUMBER}"
        FRONTEND_TAG   = "1.${BUILD_NUMBER}"

        // Frontend GitHub
        FRONTEND_REPO_URL     = 'https://github.com/papegaye/frontend-courrier-test.git'
        FRONTEND_REPO_BRANCH  = 'master'
        FRONTEND_CREDENTIALS  = 'credential-id-github'
    }

    stages {

        /* =======================================================
           1. CHECKOUT BACKEND
        ======================================================== */
        stage('Checkout Backend') {
            steps {
                dir('backend') {
                    checkout scm
                }
            }
        }

        /* =======================================================
           2. CHECKOUT FRONTEND
        ======================================================== */
        stage('Checkout Frontend') {
            steps {
                dir('frontend') {
                    git branch: env.FRONTEND_REPO_BRANCH,
                        url: env.FRONTEND_REPO_URL,
                        credentialsId: env.FRONTEND_CREDENTIALS
                }
            }
        }



        /* =======================================================
           5. DOCKER BUILDS
        ======================================================== */
        stage('Build Backend Docker Image') {
            steps {
                dir('backend') {
                    bat "docker build -t %BACKEND_IMAGE%:%BACKEND_TAG% ."
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                dir('frontend') {
                    bat "docker build -t %FRONTEND_IMAGE%:%FRONTEND_TAG% ."
                }
            }
        }

        /* =======================================================
           6. DEPLOY DOCKER COMPOSE
        ======================================================== */
        stage('Deploy with Docker Compose') {
            steps {
                bat "docker-compose down || true"
                bat "docker-compose up -d --build"
            }
        }
    }

    post {
        success {
            echo "🚀 Déploiement réussi !"
        }
        failure {
            echo "❌ Le pipeline a échoué, vérifie les logs Jenkins."
        }
    }
}