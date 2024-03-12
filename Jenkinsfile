pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                git 'https://github.com/Virppsa/testavimas.git'
                sh './mvnw clean compile'
            }
        }
        stage('Test') {
            steps {
                script {
                    sh 'chmod +x mvnw'
                    sh './mvnw test'
                }
            }

            post {
                always {
                    junit '**/target/sunfire-reports/TEST-*.xml'
                }
            }
        }
    }
}