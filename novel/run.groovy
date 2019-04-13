def runWorkflow()
{
    node('EcsNode')
    {
        sh '''/root/jenkins/novel.py ''' + href
    }
}
return this
