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

    distribute = load "/home/jenkins/jenkinsTP/novel/distribute.groovy"
    distribute.runWorkflow()
}
return this
