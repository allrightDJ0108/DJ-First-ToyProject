pipeline {
    agent any



    environment {


        //서버 정보
        ip = "172.16.213.55"
        username = "root"

        //스프링 서버 정보
        springname = "dj-first-toy"
        port = "8080"

        //도커 정보
        imagename = "dj-first-toy-img"
        dockerCredential = 'docker-access-token'
        dockerImage = ''
        tagname = "dev"

        //깃 정보
        giturl = 'https://github.com/allrightDJ0108/DJ-First-ToyProject.git/'
        gitCredential = "github-access-token"
        branchname = "main"
    }

    stages {
        // git에서 repository clone
        stage('Prepare') {
          steps {
            echo 'Clonning Repository'
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

        // gradle build
        stage('Bulid Gradle') {
          steps {
            echo 'Bulid Gradle'
            dir ('.'){
                sh """
                ./gradlew clean build --exclude-task test
                """
            }
          }
          post {
            failure {
              error 'This pipeline stops here...'
            }
          }
        }

        // docker build
        stage('Bulid Docker') {
          steps {
            echo 'Bulid Docker'
            script {
                imagename = "allrightDJ0108/${imagename}"
                dockerImage = docker.build imagename
            }
          }
          post {
            failure {
              error 'This pipeline stops here...'
            }
          }
        }

        // docker push
        stage('Push Docker') {
          steps {
            echo 'Push Docker'
            script {
                docker.withRegistry( '', dockerCredential) {
                    dockerImage.push("dev")
                }
            }
          }
          post {
            failure {
              error 'This pipeline stops here...'
            }
          }
        }

        stage('Run Container on Dev Server') {
          steps {
            echo 'Run Container on Dev Server'

            sshagent(['ec2-ssh']) {

                sh 'ssh -o StrictHostKeyChecking=no ${username}@${ip} "whoami"'

				sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker stop ${springname}'"
                sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker rm ${springname}'"
                sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker rmi ${imagename}:${tagname}'"


                sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker pull ${imagename}:${tagname}'"
                sh "ssh -o StrictHostKeyChecking=no ${username}@${ip} 'docker run -d -p 81:${port} -p ${port}:${port} --name ${springname} ${imagename}:${tagname}'"
            }
          }



        }
    }
}