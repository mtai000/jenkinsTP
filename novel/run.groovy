def runWorkflow()
{
    node('EcsNode')
    {
        sh '''python /root/jenkins/novel.py \"''' + href + '\" \"' + path + '\"'
    }
}
return this
