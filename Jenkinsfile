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
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE'){
                 sh 'mvn test'
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
