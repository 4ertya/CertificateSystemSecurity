#!groovy
properties([disableConcurrentBuilds()])

pipeline {
    agent any

    stages {

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
    post {
        always {
            echo 'Build is completed'
        }
        success {
            echo 'Build is successful'
        }
        failure {
            echo 'Build is failed'
        }
        changed {
            echo 'The state of the Pipeline has changed'
        }
    }
}