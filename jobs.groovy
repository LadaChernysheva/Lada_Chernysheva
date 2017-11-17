def gitUrl='Lada_Chernysheva/mntlab-dsl'
def credits='jenkins_git.epam'

def list = ['${ENV,var="MNTLAB-lchernysheva-child1-build-job"}',
            '${ENV,var="MNTLAB-lchernysheva-child2-build-job"}',
            '${ENV,var="MNTLAB-lchernysheva-child3-build-job"}',
            '${ENV,var="MNTLAB-lchernysheva-child4-build-job"}']

def main = job('MNTLAB-lchernysheva-main-build-job')

for(int i=1;i<=4;i++)
{
    job("MNTLAB-lchernysheva-child${i}-build-job")
    {
    parameters {
        stringParam('BRANCH_NAME')
    }
    
    scm {
        git{
            remote{
                github(gitUrl, 'ssh', 'git.epam.com')
                credentials(credits)
                }
            branches('${BRANCH_NAME}', 'master')
        }       
    }
     steps {
          shell('cd $WORKSPACE; chmod +x script.sh && ./script.sh > output.txt')
         shell('cd $WORKSPACE; tar -czf ${BRANCH_NAME}_script.tar.gz *')
    }
    
    publishers { 
        publishBuild {
            discardOldBuilds(1, 1, 1, 1)
        }
        archiveArtifacts {
            pattern('*.txt')
            pattern('*.tar.gz')
            onlyIfSuccessful()
            }
        }
	}
}


main.with{
 parameters{
        activeChoiceParam('Selected_Branch') {
            description('allow select a branch name')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script("""return[
'aaksionkin',
'adoropei',
'akarzhou',
'akonchyts',
'amaslakou',
'asemirski',
'atsuranau',
'dsilnyagin',
'hpashuto',
'lchernysheva',
'mdemenkova',
'ndolya',
'pyurchuk',
'vtarasiuk',
'vulantsau',
'yshchanouski',
'zvirinsky'
]""")
                fallbackScript("return['fallback choice']")
            }
        }

    booleanParam('MNTLAB-lchernysheva-child1-build-job', false, '')
    booleanParam('MNTLAB-lchernysheva-child2-build-job', false, '')
    booleanParam('MNTLAB-lchernysheva-child3-build-job', false, '')
    booleanParam('MNTLAB-lchernysheva-child4-build-job', false, '')
    }
    
    
    steps {
        for(int i=1; i<=4; i++)
    { 
        def p = list[i-1]
        conditionalSteps {
            condition {
                booleanCondition("${p}")
            }
         		steps{
        		downstreamParameterized {
                    trigger("MNTLAB-lchernysheva-child${i}-build-job"){
                		block {
                    		buildStepFailure('FAILURE')
                    		failure('FAILURE')
                    		unstable('UNSTABLE')
                		}   
                        parameters {
                            predefinedProp('BRANCH_NAME', '${Selected_Branch}')
                        }
                    }
                }
            }
        }
    }
  }
        

 
}

