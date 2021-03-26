pipeline {
    agent any
    environment {
        LOG_JUNIT_RESULTS = 'true'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
                junit 'target/surefire-reports/*.xml'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Publish test results') {
            steps{ 
            junit 'target/surefire-reports/*.xml'
            influxDbPublisher(selectedTarget: 'JUnit_Data')
            }
        } 
    }
}
