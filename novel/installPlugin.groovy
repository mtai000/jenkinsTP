def runWorkflow()
{
    node('MACHINEIP')
    {
        def plugs = PluginList.tokenize(',')
        def cmdStr = ""
        for(plugin in plugs)
            cmdStr = cmdStr + "pip install " + plugin + "\n"

        sh   '''#!/bin/bash\n''' + cmdStr 
    }
}
return this
