def runWorkflow()
{
    def hrefs
    def replay = load '/home/jenkins/jenkinsTP/novel/replay.groovy'
    node('master')
    {
        hrefs = sh(returnStdout:true,script: '''#!/bin/bash\n
                                                hrefs=$(sudo cat /home/jenkins/hrefs.txt)\n
                                                echo \"${hrefs}\"''')
        sleep(3)
        hrefs=hrefs.split('\r\n')
        println hrefs
        def nodeList = Jenkins.instance.nodes
        def label = 'EcsNode'
        for(cmp in nodeList)
        {
            if(cmp.labelString.contains(label))
            {
                def machineIP = cmp.labelString.split(' ')[1]
                def copy = replay.buildJob('copy',[string(name:'nodeFrom',value:'master'),
                                                   string(name:'nodeTo',value:machineIP),
                                                   string(name:'pathFrom',value:'/home/jenkins/jenkinsTP/nove/novel.py'),
                                                   string(name:'pathTo',value:'/home/jenkins/novel.py')])
                copy.run()
            }
        }
    }

    
    node('EcsNode')
    {
        sh  '''#!/bin/bash\n
                novelDir="/home/jenkins/novel"\n
                if [ -d $novelDir ];then\n
                rm -rf $novelDir\n
                fi\n
                mkdir -p $novelDir'''
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

def copyScp(nodeFrom,nodeTo,pathFrom,pathTo)
{
    def str
    node(nodeFrom)
    {
        str=sh(returnStdout:true,script: '''#!/bin/bash\n
                                            str=$(sudo cat ''' + pathFrom + ''')\n
                                            echo \"${str}\"''')
        sleep(1)
    }
    node(nodeTo)
    {
        sh   '''#!/bin/bash\n
                echo \"''' + str + '''\" > ''' + pathTo
        sleep(1)
    }
}
