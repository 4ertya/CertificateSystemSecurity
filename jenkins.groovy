#!groovy
properties([disableConcurrentBuilds()])

pipeline {
    agent any

    stages {

        stage("Build & Tests") {
            steps {
                script {
                        './gradlew clean build codeCoverageReport'
                }
            }
        }
//        stage("SonarQube analysis") {
//            environment {
//                scannerHome = tool 'InstalledSonar'
//            }
//            steps {
//                withSonarQubeEnv('LocalSonar') {
//                    bat "\"${scannerHome}\\bin\\sonar-scanner.bat\""
//                    //-Dsonar.buildbreaker.skip=true"
//                }
//            }
//        }
        // stage("Quality Gate") {
        //     steps {
        //         timeout(time: 20, unit: 'SECONDS') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }
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