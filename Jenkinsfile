pipeline {
    agent {
        docker {
            image 'maven:3.9.7-eclipse-temurin-17'
            args  '-v $HOME/.m2:/root/.m2'
        }
    }

    tools {
        maven 'Maven_3.9'
        jdk   'JDK17'
    }

    stages {

        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build & test') {
            steps {
                sh 'mvn -B clean test'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/allure-results/**',
                                       allowEmptyArchive: true
                }
            }
        }
    }

    post {
        always {
            allure includeProperties: false,
                   results: [[path: 'target/allure-results']]
        }
    }
}
