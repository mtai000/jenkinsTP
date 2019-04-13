def runWorkflow()
{
    def hrefs
    replay = load '/home/jenkins/jenkinsTP/novel/replay.groovy'
    node('master')
    {
        hrefs = sh(returnStdout:true,script: '''#!/bin/bash\n
                                                hrefs=$(sudo cat /home/jenkins/hrefs.txt)\n
                                                echo \"${hrefs}\"''')
        sleep(3)

        def shStr=   '''#!/bin/bash\n
                        str=$(cat /home/jenkins/jenkinsTP/novel/novel.py)\n
                        echo \"${str}\"'''
        pyScr = sh (returnStdout: true , script: shStr)
        sleep(3)
    }

    node('EcsNode')
    {
        sh   '''#/bin/bash\n
                novelDir="/home/jenkins/novel"
                if [ -d $novelDir ];then\n
                rm -rf $novelDir\n
                fi\n
                mkdir -p $novelDir\n
                echo \"''' + pyScr + '''\" > /home/jenkins/novel.py                
                '''
        sleep(3)
    }
    
    node('master')
    {    
        def jobs = [:]
        for( int i = 0; i<hrefs.size();i++)
        {
            def job= replay.buildJob('run',/*parameters:*/[string(name:'href',value:hrefs[i])])
            jobs[i.toString()] = job
            
        }
        parallel jobs
    }



}
return this
