pipeline {
    agent any
    tools {
        maven "Maven"
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Deploy') {
            steps {
                sh 'mvn clean deploy'
            }
        }
    }
}
