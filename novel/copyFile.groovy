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
        sh   '''#!/bin/bash\n
                if [ -d ''' + pathFolder +''' ]; then\n
                rm -rf ''' + pathFolder + '''\n
                fi
                mkdir -p ''' + pathFolder + '''\n
                echo \"''' + str + '''\" > ''' + pathTo
        sleep(1)
    }
}
return this
