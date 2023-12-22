pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checkout the application...'
                git branch: 'main',
                    credentialsId: 'github_access_token',
                    url: 'https://github.com/allrightDJ0108/DJ-First-ToyProject.git'
            }
        }
        stage('build') {
            steps {
                echo 'building the application...'
            }
        }
        stage('test') {
            steps {
                echo 'testing the application...'
            }
        }
        stage('deploy') {
            steps {
                echo 'deploying the application...'
            }
            post {
                success {
                    slackSend (
                        channel: '#jenkins',
                        color: '#00FF00',
                        message: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
                    )
                }
                failure {
                    slackSend (
                        channel: '#jenkins',
                        color: '#FF0000',
                        message: "FAIL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
                    )
                }
            }
        }
    }
}

