def runWorkflow()
{
    node('amd2600x')
    {
        def launchBat = "d:\\vmware\\jenkinsNodes\\launchVM.bat"
        bat launchBat
    }

    node('EcsNode')
    {
        sleep(1)
    }

    job = load "/home/jenkins/jenkinsTP/novel/buildJobe.groovy"
    distribute = job.buildJob('distribute',null)
    distribute.run()
}
return this
