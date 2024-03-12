pipeline {
    agent any
    stages {
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