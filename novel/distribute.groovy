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
        hrefs=hrefs.split('\r\n')
        println hrefs
    }

    def nodeList = jenkins.instance.nodes
    def label = 'EcsNode'
    for(cmp in nodes)
    {
        if(cmp.labelString.contains(label))
        {
            def machineIP = cmp.labelString.split(' ')[1]
            node(machineIP)
            {
                sh  '''#!/bin/bash\n
                        novelDir="/home/jenkins/novel"\n
                        if [ -d $novelDir ];then\n
                        rm -rf $novelDir\n
                        fi\n
                        mkdir -p $novelDir'''
            }
            copyScp('master',cmp.labelString.split(' ')[1],'/home/jenkins/jenkinsTP/novel/novel,py','/home/jenkins/novel.py')
        }
    } 
    
    node('master')
    {    
        def jobs = [:]
        for( int i = 0; i<hrefs.size();i++)
        {          
            def job= replay.buildJob('run',/*parameters:*/[string(name:'href',value:hrefs[i]),
                                                           string(name:'path',value:'/home/jenkins/novel/' + String.format("%09d",i))])
            jobs[i.toString()] = job   
        }
        parallel jobs
    }



}
return this

def copyScp(nodeFrom,nodeTo,fromPath,toPath)
{
    def str
    node(nodeFrom)
    {
        str=sh(returnStdout:true,script  '''#!/bin/bash\n
                                            str=$(sudo cat ''' + fromePath + ''')\n
                                            echo \"${str}\"''')
        sleep(1)
    }
    node(nodeTp)
    {
        sh   '''#!/bin/bash\n
                echo \"''' + str + '''\" > '''toPath
        sleep(1)
    }
}
