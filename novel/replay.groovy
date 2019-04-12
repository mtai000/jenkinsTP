def buildJob(pipe,para)
{
    def jobPath
	def work
    if(pipe == 'distribute')
	{
        jobPath = '2.0 - Distribute'
        work.run = {
            build job: jobPath
        }
    }
    if(pipe == 'run')
    {
        jobPath = '4.0 - Run'
        work.run = {
            build job : jobPath , parameters : para
        }
    }
    if(pipe == 'insPlugin')
    {
        jobPath = '3.0 - InstallPlugin'
        work.run = {
            build job : jobPath , parameters : para
        }
    }
    return work
}

