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

        // Scan Backend image
        stage('Trivy - Scan Backend') {
            steps {

                dir('backend') {

                    // Installation de Trivy.exe (si absent)
                    bat """
                        if not exist ..\\bin mkdir ..\\bin

                        if not exist ..\\bin\\trivy.exe (
                            powershell -Command "Invoke-WebRequest -Uri https://github.com/aquasecurity/trivy/releases/download/v0.67.0/trivy_0.67.0_Windows-64bit.zip -OutFile trivy.zip"
                            powershell -Command "Expand-Archive -Force trivy.zip ..\\bin"
                            del trivy.zip
                        )
                    """

                    /// Création du dossier des rapports ==="
                    bat """
                        if not exist trivy-reports mkdir trivy-reports
                    """

                    // Scan de l'image Docker Backend
                    bat """
                        ..\\bin\\trivy.exe image ^
                            --timeout 10m ^
                            --format json ^
                            -o trivy-reports\\trivy-backend.json ^
                            %BACKEND_IMAGE%:%BACKEND_TAG%
                    """

                    //  Conversion Rapport JSON → HTML
                    bat """
                        ..\\bin\\trivy.exe convert ^
                            trivy-reports\\trivy-backend.json ^
                            --output trivy-reports\\trivy-backend.html
                    """
                }
            }

            post {
                always {
                    //  Archivage des rapports Trivy

                    archiveArtifacts artifacts: 'backend/trivy-reports/trivy-backend.json', fingerprint: true
                    archiveArtifacts artifacts: 'backend/trivy-reports/trivy-backend.html', fingerprint: true

                    //  Publication rapport HTML dans Jenkins
                    publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'backend/trivy-reports',
                        reportFiles: 'trivy-backend.html',
                        reportName: 'Trivy Backend Scan'
                    ])
                }
            }
        }



        // Scan Frontend image
        stage('Trivy - Scan Frontend') {
            steps {
                dir('frontend') {

                    // Installation Trivy.exe si absent
                    bat """
                        if not exist ..\\bin mkdir ..\\bin

                        if not exist ..\\bin\\trivy.exe (
                            powershell -Command "Invoke-WebRequest -Uri https://github.com/aquasecurity/trivy/releases/download/v0.67.0/trivy_0.67.0_Windows-64bit.zip -OutFile trivy.zip"
                            powershell -Command "Expand-Archive -Force trivy.zip ..\\bin"
                            del trivy.zip
                        )
                    """



                    // Création dossier rapports
                    bat """
                        if not exist trivy-reports mkdir trivy-reports
                    """

                    // Scan Docker image Frontend
                    bat """
                        ..\\bin\\trivy.exe image ^
                            --timeout 10m ^
                            --format json ^
                            -o trivy-reports\\trivy-frontend.json ^
                            %FRONTEND_IMAGE%:%FRONTEND_TAG%
                    """

                    // Conversion  Rapport JSON → HTML (frontend)
                    bat """
                        ..\\bin\\trivy.exe convert ^
                            trivy-reports\\trivy-frontend.json ^
                            --output trivy-reports\\trivy-frontend.html
                    """
                }
            }

            post {
                always {

                    // Archivage des rapports Frontend

                    archiveArtifacts artifacts: 'frontend/trivy-reports/trivy-frontend.json', fingerprint: true
                    archiveArtifacts artifacts: 'frontend/trivy-reports/trivy-frontend.html', fingerprint: true

                    //  Publication rapport HTML Frontend dans Jenkins
                    publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'frontend/trivy-reports',
                        reportFiles: 'trivy-frontend.html',
                        reportName: 'Trivy Frontend Scan'
                    ])
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