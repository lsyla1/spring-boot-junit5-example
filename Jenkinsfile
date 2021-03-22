pipeline {
    agent any
    environment{
    PATH = "/usr/local/Cellar/maven/3.6.3_1/libexec:$PATH"
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