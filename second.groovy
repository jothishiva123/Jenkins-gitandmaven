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
        stage('Code Analysis') {
          steps {
            withSonarQubeEnv('SonarQube') {
              sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
                                                                      }
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
