def runWorkflow()
{
    def hrefs
    def pyScr
    replay = load '/home/jenkins/jenkinsTP/novel/replay.groovy'
    node('master')
    {
        hrefs = sh(returnStdout:true,script: '''#!/bin/bash\n
                                                hrefs=$(cat /home/jenkins/hrefs.txt)\n
                                                echo \"$(hrefs)\"''')
        sleep(3)

        def shStr=   '''#!/bin/bash\n
                        str=$(cat /home/jenkins/jenkinsTP/novel/novel.py)\n
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
        def nodeList = jenkins.instance.nodes
        def label = 'EcsNode'
        for(cmp in nodes)
        {
            if(cmp.labelString.contains(label))
            {
                def job = replay.buildJob('installPlugin',['MACHINEIP':cmp.labelString.split(' ')[0],'PluginList':'requests,lxml'])
                job.run()
            }
        }
        
        def jobs
        for(href in hrefs)
        {
            def job= replay.buildJob('run',['href':href])
            jobs += job.run()
            
        }
        parallel jobs
    }



}
return this
