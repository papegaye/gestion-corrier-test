pipeline {

    agent {
        label 'agent-windows'
    }

   tools {
        maven 'Maven-3.9.6'
    }

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

        // SonarQube
        SONAR_HOST_URL = 'http://localhost:9000'
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
           3. SONARQUBE BACKEND
        ======================================================== */
        stage('Analyse SonarQube Backend') {
            steps {
                dir('backend') {
                    script {
                        withSonarQubeEnv('SonarQube') {
                            withCredentials([string(credentialsId: 'sonor-id-credential', variable: 'SONAR_AUTH_TOKEN')]) {

                                bat """
                                    mvn clean verify -DskipTests sonar:sonar ^
                                        -Dsonar.projectKey=analyse-code-backend ^
                                        -Dsonar.projectName="analyse-code-backend" ^
                                        -Dsonar.host.url=http://localhost:9000 ^
                                        -Dsonar.token=%SONAR_AUTH_TOKEN%
                                """
                            }
                        }
                    }
                }
            }
        }

       /*  stage('Quality Gate Backend') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        } */

        /* =======================================================
           4. SONARQUBE FRONTEND
        ======================================================== */
        stage('Analyse SonarQube Frontend') {
            steps {
                dir('frontend') {
                    script {

                        withSonarQubeEnv('SonarQube') {
                            withCredentials([string(credentialsId: 'sonor-id-credential', variable: 'SONAR_AUTH_TOKEN')]) {

                                def scannerHome = tool name: 'SonarQubeScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'

                                bat """
                                    "${scannerHome}\\bin\\sonar-scanner.bat" ^
                                        -Dsonar.projectKey=analyse-code-frontend ^
                                        -Dsonar.projectName="analyse-code-frontend" ^
                                        -Dsonar.sources=src ^
                                        -Dsonar.host.url=http://localhost:9000 ^
                                        -Dsonar.token=%SONAR_AUTH_TOKEN%
                                """
                            }
                        }

                    }
                }
            }
        }

        /* stage('Quality Gate Frontend') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        } */

        /* =======================================================
           5. OWASP DEPENDENCY-CHECK BACKEND b
        ======================================================== */
        stage('OWASP Dependency-Check Backend') {
            steps {
                dir('backend') {
                    script {
                        def depCheckHome = tool(
                            name: 'Owasp-Dependency-Check',
                            type: 'org.jenkinsci.plugins.DependencyCheck.tools.DependencyCheckInstallation'
                        )

                        bat """
                            if not exist reports mkdir reports
                            "${depCheckHome}\\bin\\dependency-check.bat" ^
                                --scan . ^
                                --format HTML --format XML ^
                                --out reports
                        """
                    }
                }
            }
            post {
                always {
                    dependencyCheckPublisher pattern: 'backend/reports/dependency-check-report.xml'
                    archiveArtifacts artifacts: 'backend/reports/dependency-check-report.html', fingerprint: true
                }
            }
        }

        /* =======================================================
           6. OWASP DEPENDENCY-CHECK FRONTEND ddd
        ======================================================== */
        stage('OWASP Dependency-Check Frontend') {
            steps {
                dir('frontend') {
                    script {
                        def depCheckHome = tool(
                            name: 'Owasp-Dependency-Check',
                            type: 'org.jenkinsci.plugins.DependencyCheck.tools.DependencyCheckInstallation'
                        )

                        bat """
                            if not exist reports mkdir reports
                            "${depCheckHome}\\bin\\dependency-check.bat" ^
                                --scan . ^
                                --disableAssembly ^
                                --disableYarnAudit ^
                                --format HTML --format XML ^
                                --out reports
                        """
                    }
                }
            }
            post {
                always {
                    dependencyCheckPublisher pattern: 'frontend/reports/dependency-check-report.xml'
                    archiveArtifacts artifacts: 'frontend/reports/dependency-check-report.html', fingerprint: true
                }
            }
        }

        /* =======================================================
           7. DOCKER BUILDS
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