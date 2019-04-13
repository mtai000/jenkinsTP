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
                                            str=$(sudo cat ''' + fromPath + ''')\n
                                            echo \"${str}\"''')
        sleep(1)
    }
    node(nodeTp)
    {
        sh   '''#!/bin/bash\n
                echo \"''' + str + '''\" > '''toPath
        sleep(1)
    }
}
return this
