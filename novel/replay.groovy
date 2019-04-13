def buildJob(pipe,para)
{
    def work
    def jobPath
    if(pipe == 'distribute')
	{
        jobPath = '2.0 - Distribute'
        work = {
            build job: jobPath,wait:false
            return 'SUCCESS'
        }
    }
    if(pipe == 'run')
    {
        jobPath = '4.0 - Run'
        work = {
            build job : jobPath , parameters : para
            return 'SUCCESS'
        }
    }
    if(pipe == 'copy')
    {
        jobPath = '3.0 - ScpFile'
        work = {
            build job: jobPath, parameters : para
            return 'SUCCESS'
        }
    }
    //if(pipe == 'installPlugin')
    //{
    //    jobPath = '3.0 - InstallPlugin'
    //    work = {
    //        build job : jobPath , parameters : para , wait: false
    //        return 'SUCCCESS'
    //    }
    //}
    return work
}
return this

