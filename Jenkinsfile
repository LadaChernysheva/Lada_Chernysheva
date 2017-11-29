pipeline {
    agent any
	tools {
        gradle "gradle4.3"
    }

    /**environment {
	env.rtGradle = Artifactory.newGradleBuild()
	env.buildInfo = Artifactory.newBuildInfo()
    }
    options{
        timestamp()
        ansiColor('xterm')
    }*/
	
    stages {
        stage ("Preparation (Checking out)") {
            agent any
            steps {
                git url: 'https://github.com/bubalush/mntlab-pipeline.git'
                //Artifactory.newGradleBuild().tool = 'grandle4.3'
            }
        }
        
        stage ("Building code") {
		agent any
		steps {
			sh 'gradle clean compile'
          // Artifactory.newGradleBuild().run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks:'clean compile'
        	}
	}
        
        stage ("Testing code") {
		agent none
		parallel{
                    stage("Cucumber Tests") {
                        agent { label "master"}
                        steps {
				sh 'gradle cucmber'
            			//Artifactory.newGradleBuild().run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks: 'cucumber'
                        }
					}
                     stage("JUnit Tests") {
                        agent {label "FIRST"}
                        steps {
				sh 'gradle clean test'
            			//Artifactory.newGradleBuild().run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks: 'clean test'
                        }
                     }
                         stage("Jacoco Tests") {
                        agent {label "SECOND"}
                        steps {sh 'gradle jacoco'
				//Artifactory.newGradleBuild().run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks: 'jacoco'
                        }
                     }
				}
	       }
	    
                         
        stage ("Triggering job and fetching artefact after finishing") {
		agent any
		steps {
       		 build job: 'Project MNTLAB-lchernysheva-child1-build-job', parameters: [string(name: 'BRANCH_NAME', value: lchernysheva)], quietPeriod: 2 
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
