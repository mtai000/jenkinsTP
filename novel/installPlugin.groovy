def runWorkflow(MACHINEIP,plugins)
{
    
    node(MACHINEIP)
    {
        def plugList = plugins.tokenize(',')
        def cmdStr = ""
        for(plugin in plugList)
            cmdStr = cmdStr + "pip install " + plugin + "\n"

        sh   '''#!/bin/bash\n''' + cmdStr 
    }
}
return this
