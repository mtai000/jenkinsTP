import requests,threading
import time
import os
from lxml import etree
import sys


def get_html(href)
    headers = {'User-Agent' : 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36'}
    reqGet = requests.get(url,headers)
    response = reqGet.content
    return (response)

def get_capture(html):
    selector = etree.HTML(html.replace('<br/>','\r\n'))
    links = selector.xpath('//*[@id="content"]')
    return links[0].text

def SaveInTxt(str,path):
    file_object = open('path','w')
    file_object.write(str)
    file_object.close()

if __name__ == '__main__':
    reload(sys)
    sys.setdefaultencoding('utf-8')
    lineText = sys.argv[0]
    url = r"https://www.biquke.com/bq/61/61596/"
    href = url + lineText.split('"')[1][0:]
    title = lineText.split('</a')[0].split('\">')[1]
    html = get_html(href)
    captureBody = get_capture(html)
    saveInTxt(title + '\r\n' + captureBody + '\r\n\r\n', argv[1])
    
    #main()
