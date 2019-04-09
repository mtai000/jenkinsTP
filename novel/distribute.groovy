def runWorkflow()
{
    def hrefs
    def pyScr

    node('master')
    {
        hrefs = sh(returnStdout:true,script: '''#!/bin/bash\n
                                                hrefs=$(cat /home/jenkins/hrefs.txt)\n
                                                echo \"$(hrefs)\"''')
        sleep(3)

        def shStr=   '''#!/bin/bash\n
                        str=$(cat /home/jenkins/novel.py)\n
                        echo \"$(str)\"'''
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
                echo \"''' + pyScr + '''\">$novelDir\n'''
        sleep(3)
    }
    
    node('master')
    {
        for(href in hrefs)
        {
            run = load "/home/jenkins/jenkinsTP/novel/run.groovy"
            run.runWorkflow(href)
            
        }
        
    }



}
return this
