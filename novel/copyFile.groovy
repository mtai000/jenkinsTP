def runWorkflow()
{
    node(MACHINEIP)
    {
        def shStr = '''scp {host1} {host2}'''
    }
}
return this