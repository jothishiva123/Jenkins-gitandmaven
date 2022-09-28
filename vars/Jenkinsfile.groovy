@Library('shared-library@main') _
pipeline {
agent any 
     tools {
         maven 'maven3'
         }
      stages {
        stage ('Build maven') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'jothishiva123', url: 'https://github.com/jothishiva123/mvn.git']]])
                sh 'mvn clean package'
                echo 'Build Completed'
                }
        }
        
        stage ('Build Docker image') {
            steps {
                script {
                    sh 'docker build -t jothimanikandanraja/mynewapp .'
                }
            }
        }
         stage('Push Docker Image') {
            steps {
                script {
                 withCredentials([string(credentialsId: 'dockerhubpwd', variable: 'dockerhubpwd')]) {
                     sh 'docker login -u jothimanikandanraja -p ${dockerhubpwd}'
  

                    
                 }  
                 sh 'docker push jothimanikandanraja/mynewapp'
                }
            }
        }
    }
}  
