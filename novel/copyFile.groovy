def runWorkflow()
{
    /*node(machineIP)
    {
        def shStr = '''scp {host1} {host2}'''.replace('{host1}',host1).replace('{host2}',host2)
        sh '''#!/bin/bash\n''' + shStr
    }*/
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
        def pathFolder = pathTo[0..pathTo.lastIndexOf('/')]
        echo pathTo
        def shStr = '''#!/bin/bash\n
                        if [ ! -d ''' + pathFolder + ''' ]; then\n
                        sudo mkdir -p ''' + pathFolder +'''\n
                        fi\n
                        sudo chmod 777 ''' + pathFolder + '''\n
                        if [ -x ''' + pathTo + ''' ]; then\n
                        sudo rm ''' + pathTo + '''\n
                        fi\n
                        sudo touch ''' + pathTo + '''\n
                        sudo chmod 777 ''' + pathTo
                        
        sh shStr
        echo shStr
        for(s in str.split('\n'))
        {
            sh "echo \"" + s.replace("\"","\\\"").replace("\\\\\"","\\\"") + "\" >> " + pathTo
        }
        sleep(1)
    }
}
return this
