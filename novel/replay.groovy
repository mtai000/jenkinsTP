def work
def buildJob(pipe,para)
{
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
    if(pipe == 'insPlugin')
    {
        jobPath = '3.0 - InstallPlugin'
        work = {
            build job : jobPath , parameters : para
            return 'SUCCCESS'
        }
    }
    return work
}
return work
