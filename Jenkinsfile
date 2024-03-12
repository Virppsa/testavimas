pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                git branch: 'main', url: 'https://github.com/Virppsa/testavimas.git'
                sh 'chmod +x mvnw'
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
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
    }
}