def runWorkflow()
{
    node(ip)
    {
        withEnv(['PATH+EXTRA=/usr/sbin:/usr/bin:/sbin:/bin'])
        {
            def shStr = '''#!/bin/bash\npython /home/jenkins/novel.py \"''' + href.replace("\"","\\\"") + '\" \"' + savein + '\"'
            echo shStr
            sh shStr
        }
    }
    replay = load "/home/jenkins/jenkinsTP/novel/replay.groovy"
    def copy =replay.buildJob('copy',[string(name:'pathFrom',value:path),
                            string(name:'nodeFrom',value:ip),
                            string(name:'pathTo',value:'/home/jenkins/novel/'),
                            string(name:'nodeTo',value:'mater')
                            ])
    copy.run()
    sleep(1)
}
return this
