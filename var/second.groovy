def call(String :repoUrl) {
  pipeline {
       agent any
       tools {
           maven 'M2_HOME'
          
       }
       stages {
         stage("Checkout Code") {
               steps {
                   git branch: 'main',
                       url: "${repoUrl}" ,
                     credentialsId: 'github'
               }
           }
      stages {
        stage ("MVN Build") {
            steps {
                sh 'mvn clean package'
              
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
}
