# coding=utf-8
import scrapy
import socket
import urllib2

socket.setdefaulttimeout(2)


class ProxiesSpider(scrapy.Spider):
    name = 'proxy'
    allow_domains = ["http://www.xicidaili.com"]
    start_urls = ['http://www.xicidaili.com/nt/' + str(page) for page in range(1, 4)]
    custom_settings = {
        'DOWNLOAD_DELAY': 2,
        'SPIDER_MIDDLEWARES': {
            'crawler.middlewares.AgentMiddleware': 400,
            'crawler.middlewares.ProxyMiddleware': None,
            'crawler.middlewares.DBMiddleware': None,
        }
    }

    def __init__(self, proxy_path):
        self.file = open(proxy_path, "w")

    @classmethod
    def from_crawler(cls, crawler, **kwargs):
        proxy_path = crawler.settings.get('PROXY_FILE_NAME')
        return cls(proxy_path)

    def parse(self, response):
        ip_list = response.xpath('//table[@id="ip_list"]/tr')
        is_begin = True
        for ip in ip_list:
            if is_begin:
                is_begin = False
            else:
                td = ip.xpath('./td/text()').extract()
                proxy = td[0] + ':' + td[1]
                if not self.is_bad_proxy(proxy):
                    self.file.write(proxy + '\n')
                    print proxy + " is working"
                else:
                    print "Bad Proxy ", proxy
        yield None

    @staticmethod
    def is_bad_proxy(pip):
        try:
            proxy_handler = urllib2.ProxyHandler({'http': pip, 'https': pip})
            opener = urllib2.build_opener(proxy_handler)
            opener.addheaders = [('User-agent', 'Mozilla/5.0')]
            urllib2.install_opener(opener)
            req = urllib2.Request('https://www.baidu.com')
            sock = urllib2.urlopen(req)
        except urllib2.HTTPError, e:
            print 'Error code: ', e.code
            return e.code
        except Exception, detail:
            print "ERROR:", detail
            return 1
        return 0

    def closed(self, reason):
        self.file.close()
