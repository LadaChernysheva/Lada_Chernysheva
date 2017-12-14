pipeline {
    agent { label "master"}
	tools {
        gradle "gradle4.3"
    }
  	
    stages {
        stage ("Preparation (Checking out)") {
            agent { label "master"}
            steps {
                git url: 'https://github.com/bubalush/mntlab-pipeline.git'
                }
        }
        
        stage ("Building code") {
		agent { label "master"}
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
		agent { label "FIRST"}
		steps {
       		 build job: 'MNTLAB-lchernysheva-child1-build-job', parameters: [string(name: 'BRANCH_NAME', value: 'lchernysheva')], quietPeriod: 1 
        	}
	}
                         
        stage ("Packaging and Publishing results") {
	    agent { label "master"}
            steps {
            	sh 'tar  -cvvzf pipeline-lchernysheva-$BUILD_NUMBER.tar.gz /var/lib/jenkins/workspace/MNTLAB-lchernysheva-child1-build-job/jobs.groovy /var/lib/jenkins/workspace/seed_for_Task10/Jenkinsfile'
                archiveArtifacts '**.tar.gz'}
        }
                         
        stage ("Asking for manual approval") {
	    agent { label "master"}
            steps {
                input 'Are you want to deploy artifacts?'}
        }
                         
        stage ("Deployment") {
		agent { label "master"}
		tools {
			jdk 'JDK 8'
		}
		steps {
			sh '''gradle jar
			cd $WORKSPACE/build/libs/
			chmod 777 gradle-simple.jar
			java -jar gradle-simple.jar'''
			}
        }
                         
        stage ("Sending status") {
	    agent { label "master"}
            steps {
                echo 'Pipeline was completed with \'SUCCESS\''
            }
        }
    }
}
