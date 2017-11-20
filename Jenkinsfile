pipeline {
    def rtGradle = Artifactory.newGradleBuild()
	def buildInfo
     agent none
    options{
        timestamp()
        ansiColor('xterm')
    }
    stages {
        stage 'Preparation (Checking out)'{
            agent any
            steps{
                git url: 'https://github.com/bubalush/mntlab-pipeline.git'
            }
        }
        stage 'Building code'{
            buildInfo = rtGradle.run rootDir: "", buildFile: 'build.gradle', tasks:
        }
        stage 'Testing code'{
        }
        stage 'Triggering job and fetching artefact after finishing'{
        }
        stage 'Packaging and Publishing results'{
        }
        stage 'Asking for manual approval'{
        }
        stage 'Deployment'{
        }
        stage 'Sending status'{
        }
    }
}
