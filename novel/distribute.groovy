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
        def jobs = [:]
        for( int i = 0; i<hrefs.size();i++)
        {          
            def machineIP = nodeList[i%nodeList.size()]
            echo hrefs[i]
            def job= replay.buildJob('run',/*parameters:*/[string(name:'href',value:hrefs[i]),
                                                           string(name:'ip',value:machineIP),
                                                           string(name:'fileName',value:String.format("%04d",i) + '.txt')])
            jobs[i.toString()] = job   
            if( (i%16 == 15 || i == hrefs.size()-1) && i != 0)
            {
                parallel jobs
                job = [:]
            }
        }


    }
    
    node('amd2600x')
    {
        def shareFolder = 'e:\\share\\novel'
        def fp = new File('e:\\share\\out.txt').newPrintWriter()
        new File(shareFolder).listFiles().each
        {f ->
            fr = new File(f).read()
            fp.write(fr)
        }
        fp.flush()
        fp.close()
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

