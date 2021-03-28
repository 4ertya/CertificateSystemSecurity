#!groovy
properties([disableConcurrentBuilds()])

pipeline {
    agent any

    stages {
        stage("Checkout") {
            steps {
                git branch: 'jenkins', changelog: true, poll: true, url: 'https://github.com/4ertya/CertificateSystemSecurity.git/'
            }
        }

        stage("Build & Tests") {
            steps {
                script {
                    bat './gradlew clean build codeCoverageReport'
                }
            }
        }
        stage("SonarQube") {
            environment {
                scannerHome = tool 'sonarqube'
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat "\"${scannerHome}\\bin\\sonar-scanner.bat\""
                }
            }
        }

        stage("Deploy") {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'tomcat_credentials',
                        path: '', url: 'http://localhost:8088/')],
                        contextPath: '', onFailure: false, war: '**/*.war'
            }
        }
    }
}