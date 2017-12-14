pipeline {
    agent any
	tools {
        gradle "gradle4.3"
    }
  	
    stages {
        stage ("Preparation (Checking out)") {
            agent any
            steps {
                git url: 'https://github.com/bubalush/mntlab-pipeline.git'
                }
        }
        
        stage ("Building code") {
		agent any
		steps {
			sh 'gradle clean compileJava'
        	}
	}
        
        stage ("Testing code") {
		//agent none
		parallel{
                    stage("Cucumber Tests") {
                        agent { label "master"}
                        steps {
				sh 'gradle cucumber'}
					}
                     stage("JUnit Tests") {
                        agent {label "FIRST"}
                        steps {
				git url: 'https://github.com/bubalush/mntlab-pipeline.git'
				sh 'gradle test'}
                     }
                         stage("Jacoco Tests") {
                        agent {label "SECOND"}
                        steps {
				git url: 'https://github.com/bubalush/mntlab-pipeline.git'
				sh 'gradle test jacocoTestReport'}
                     }
				}
	       }
	    
                         
        stage ("Triggering job and fetching artefact after finishing") {
		agent any
		steps {
       		 build job: 'MNTLAB-lchernysheva-child1-build-job', parameters: [string(name: 'BRANCH_NAME', value: 'lchernysheva')], quietPeriod: 1 
        	}
	}
                         
        stage ("Packaging and Publishing results") {
	    agent any
            steps {
            	sh 'tar  -cvvzf pipeline-lchernysheva-$BUILD_NUMBER.tar.gz /var/lib/jenkins/workspace/MNTLAB-lchernysheva-child1-build-job/jobs.groovy /var/lib/jenkins/workspace/seed_for_Task10/Jenkinsfile'
                archiveArtifacts '**.tar.gz'}
        }
                         
        stage ("Asking for manual approval") {
	    agent any
            steps {
                input 'Are you want to deploy artifacts?'}
        }
                         
        stage ("Deployment") {
		agent any
		tools {
			jdk 'JDK 8'
		}
		steps {
			sh 'java -jar gradle-simple.jar'
			}
        }
                         
        stage ("Sending status") {
	    agent any
            steps {
                echo 'Pipeline was completed with \'SUCCESS\''
            }
        }
    }
}
