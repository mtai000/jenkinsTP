def runWorkflow()
{
    def hrefs
    def replay = load '/home/jenkins/jenkinsTP/novel/replay.groovy'
    def paraCopy=[:]
    node('master')
    {
        hrefs = sh(returnStdout:true,script: '''#!/bin/bash\n
                                                hrefs=$(sudo cat /home/jenkins/hrefs.txt)\n
                                                echo \"${hrefs}\"''')
        sleep(3)
        hrefs=hrefs.split('\r\n')
        println hrefs
    
        def nodeList = getMachines()
        for(int i = 0; i < nodeList.size(); i++)
        { 
            def machineIP = nodeList[i]
            def copy = replay.buildJob('copy',[string(name:'nodeFrom',value:'master'),
                                               string(name:'nodeTo',value:machineIP),
                                               string(name:'pathFrom',value:'/home/jenkins/jenkinsTP/novel/novel.py'),
                                               string(name:'pathTo',value:'/home/jenkins/novel.py')])
            paraCopy[i.toString()] = copy
    
        }
        parallel paraCopy
    
/*    node('EcsNode')
    {
        sh  '''#!/bin/bash\n
                novelDir="/home/jenkins/novel"\n
                if [ -d $novelDir ];then\n
                rm -rf $novelDir\n
                fi\n
                mkdir -p $novelDir'''
    }
  */  
    
    
    //node('master')
    //{    
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
@NonCPS
def getMachines()
{
    def nodeList = Jenkins.instance.nodes
    def label='EcsNode'
    def ips = []
    for(node in nodeList)
    {
        if(node.labelString.contains(label))
            ips += node.labelString.split(' ')[1]
    }
    return ips
}

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
