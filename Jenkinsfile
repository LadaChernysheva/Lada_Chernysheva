pipeline {  
    agent none
    options{
        timestamp()
        ansiColor('xterm')
    }
    enviroment {
	def rtGradle = Artifactory.newGradleBuild()
        def buildInfo = Artifactory.newBuildInfo()
	}
    stages {
        stage ("Preparation (Checking out)"){
            agent any
            steps{
                git url: 'https://github.com/bubalush/mntlab-pipeline.git'
                rtGradle.tool = 'grandle4.3'
            }
        }
        
        stage ("Building code") {
           rtGradle.run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks:'clean compile'
        }
        
        stage ("Testing code") {
            steps {
                parallel{
                    stage("Cucumber Tests") {
                        agent { 
                            label "master"
                             }
                        steps {
            				rtGradle.run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks: 'cucumber'
                        }
					}
                     stage("JUnit Tests") {
                        agent { 
                            label "FIRST"
                             }
                        steps {
            				rtGradle.run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks: 'clean test'
                        }
                     }
                         stage("Jacoco Tests") {
                        agent { 
                            label "SECOND"
                             }
                        steps {
            				rtGradle.run rootDir: '/var/lib/jenkins/workspace/Task_10/', buildFile: 'build.gradle', tasks: 'jacoco'
                        }
                     }
				}
			}
		}
                         
        stage ("Triggering job and fetching artefact after finishing") {
       		 build job: 'Project MNTLAB-lchernysheva-child1-build-job', parameters: [string(name: 'BRANCH_NAME', value: lchernysheva)], quietPeriod: 2 
        }
                         
        stage ("Packaging and Publishing results") {
            steps {
            	sh '''#!/bin/bash
tar  -cvvzf pipeline-lchernysheva-$BUILD_NUMBER.tar.gz /var/lib/jenkins/workspace/MNTLAB-lchernysheva-child1-build-job/jobs.groovy /var/lib/jenkins/workspace/seed_for_Task10/Jenkinsfile'''
                archiveArtifacts '**.tar.gz'}
        }
                         
        stage ("Asking for manual approval") {
            steps {
                input 'Are you want to deploy artifacts?'}
        }
                         
        stage ("Deployment'{
		steps {
			sh '''#!/bin/bash
java -jar gradle-simple.jar'''
			}
        }
                         
        stage ("Sending status") {
            steps {
                echo 'Pipeline was completed with \'SUCCESS\''
            }
        }
    }
}
