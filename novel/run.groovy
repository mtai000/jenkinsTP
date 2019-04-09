def runWorkflow()
{
    def pyScr


    node('master')
    {
        def shStr='''#!/bin/bash\n
                    str=$(cat /home/jenkins/novel.sh)\n
                    echo \"${str}\"'''
        pyScr = sh (returnStdout: true, script: shStr)
        sleep(3)
    }

    node('EcsNode')
    {
        def shStr='''#!/bin/bash\n
                    mkdir -p /home/jenkins/novel\n
                    echo \"''' + pyScr + '''\">/home/jenkins/novel/novel.py\n
                    '''
        sh shStr

    }
}
