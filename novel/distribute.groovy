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
        hrefs=hrefs.split('\n')
        println hrefs
        def nodeList= getMachines()
        def masterNode 
        //for(cmp in Jenkins.instance.computers)
        //{
        //    if(cmp.getAssignedLabels() == 'master')
        //        masterNode = cmp
        //}
        //def jobs = [:]
        for( int i = 0; i<hrefs.size();i++)
        {          
            def machineIP = nodeList[i%16]
            echo hrefs[i]
            def job= replay.buildJob('run',/*parameters:*/[string(name:'href',value:hrefs[i]),
                                                           string(name:'ip',value:machineIP),
                                                           string(name:'fileName',value:String.format("%04d",i) + '.txt')])
            //jobs[i.toString()] = job   
            while(Jenkins.getInstance().getQueue().countBuildableItems() > 100) 
            {
                sleep(10)
            }
            job.run()
        }
    }
    
    node('amd2600x')
    {
        bat '''for %%a in (e:\\Share\\novel\\*.txt) do(\n
            echo %%a\n
            type %%a >> e:\\Share\\output.txt)'''
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

