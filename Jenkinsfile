pipeline {
    agent any
    environment {
        //Server Info
        ip = "184.72.119.103"
        username = "ubuntu"

        //Spring Info
        springname = "dj-first-toy"
        port = "8088"

        //Docker Info
        imagename = "dj-first-toy-img"
        dockerCredential = "docker-access-key"
        dockerImage = ''
        tagname = "dev"

        //GitHub Info
        giturl = 'https://github.com/allrightDJ0108/DJ-First-ToyProject.git'
        gitCredential = "github-access-token"
        branchname = "main"
    }
    stages {
        stage('Git Repository Clone') {
            steps {
                echo 'Clone the Repository...'
                    git url: giturl,
                    branch: branchname,
                    credentialsId: gitCredential
            }
            post {
                success {
                    echo 'Successfully Cloned Repository'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
        stage('Build Gradle') {
            steps {
                echo 'Permission to gradlew...'
                script {
                    sh 'chmod +x ./gradlew'
                }
                echo 'building the application...'
                dir ('.'){
                    sh """
                    ./gradlew clean build --exclude-task test
                    """
                }
            }
            post {
                success {
                    echo 'Successfully Build Gradle'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
        stage('Build Docker Image'){
            steps {
                echo 'building docker image...'
                script {
                    imagename = "allrightdj/${imagename}"
                    dockerImage = docker.build imagename
                }
            }
            post {
                success {
                    echo 'Successfully Build Image'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
        stage('Push Image To Docker Hub'){
            steps {
                echo 'pushing docker image...'
                script {
                    docker.withRegistry( '', dockerCredential) {
                        dockerImage.push("dev")
                    }
                }
            }
            post {
                success {
                    echo 'Successfully Push Image'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
        stage('Run Docker Container on Dev Server'){
            steps {
                echo 'Run Container on Dev Server...'
                sshagent(['ec2-ssh']) {
                    sh 'ssh -o StrictHostKeyChecking=no ${username}@${ip} "whoami"'

                    sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker ps -f name=${springname} -q | xargs --no-run-if-empty docker container stop'"
                    sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker container ls -a -fname=${springname} -q | xargs --no-run-if-empty docker container rm'"
                    sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker images -f reference=${imagename}:${tagname} -q | xargs --no-run-if-empty docker image rmi'"


                    sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker pull ${imagename}:${tagname}'"
                    sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker run -d -p 80:${port} -p ${port}:${port} --name ${springname} ${imagename}:${tagname}'"
                }
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

