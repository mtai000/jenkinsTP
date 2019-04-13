def runWorkflow()
{
    node(machineIP)
    {
        def shStr = '''scp {host1} {host2}'''.replace('{host1}',host1).replace('{host2}',host2)
        sh '''#!/bin/bash\n''' + shStr
    }
}
return this
