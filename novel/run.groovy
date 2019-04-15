def runWorkflow()
{
    node(ip)
    {
        withEnv(['PATH+EXTRA=/usr/sbin:/usr/bin:/sbin:/bin'])
        {
            def mkdirSH ='''#!/bin/bash\n
                            if [ ! -d /home/jenkins/novel ]; then\n
                            mkdir -p /home/jenkins/novel/\n
                            chmod 777 /home/jenkins/novel/
                            fi\n
                            if [ ! -d /mnt/windows/novel  ]; then\n
                            /root/mountWindows.sh
                            fi\n
                            '''
            echo mkdirSH
            sh mkdirSH
                        
            def shStr =  '''#!/bin/bash\n
                            cp /mnt/windows/novel.py /root/novel.py\n
                            python2 /root/novel.py \"''' + href.replace("\"","\\\"") + '\" \"' + '/home/jenkins/novel/' +  fileName + '\"'
            echo shStr
            sh shStr
            sh '''scp ''' + '/home/jenkins/novel/' + fileName + ''' /mnt/windows/novel/''' + fileName
        }

    }
    sleep(10)
}
return this
