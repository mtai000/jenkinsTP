#!/bin/bash
curDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#hrefs=$(cat ${curDir}/hrefs.txt)
#hrefArr=($(awk '{print $2}' hrefs.txt))
#echo ${hrefArr[0]}
res=$curDir/novel
if [ -x $res ]; then
    rm -r $res
fi
mkdir $res
i=0
exec < $curDir/hrefs.txt
while read line;do
    filename=$(echo $i|awk '{printf("%04d\n",$0)}')
    save=$res/$filename.txt
    python novel.py "${line}" "$save" >> py.log
    echo $save
    let i++
    sleep 10s
done
for file in $res/*.txt; do
    cat $file >> $curDir/output.txt
done
