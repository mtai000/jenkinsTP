def runWorkflow()
{
    node(ip)
    {
        withEnv(['PATH+EXTRA=/usr/sbin:/usr/bin:/sbin:/bin'])
        {
            sh   '''#!/bin/bash\n
                    if [ ! -d /home/jenkins/novel ]; then\n
                    mkdir -p /home/jenkins/novel/\n
                    chmod 777 /home/jenkins/novel/
                    fi\n
                    
                '''

            def shStr = '''#!/bin/bash\npython /home/jenkins/novel.py \"''' + href.replace("\"","\\\"") + '\" \"' + savein + '\"'
            echo shStr
            sh shStr
        }
    }
    replay = load "/home/jenkins/jenkinsTP/novel/replay.groovy"
    def copy =replay.buildJob('copy',[string(name:'pathFrom',value:savein),
                            string(name:'nodeFrom',value:ip),
                            string(name:'pathTo',value:'/home/jenkins/novel/'+savein.substring(savein.lastIndexOf('/')+1)),
                            string(name:'nodeTo',value:'master')
                            ])
    copy.run()
    sleep(1)
}
return this
