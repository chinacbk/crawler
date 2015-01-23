package com.cbk.crawler;

import java.util.Set;
public class Crawler { 
	
	//使用种子 url 初始化 URL 队列
	private void initCrawlerWithSeeds(String[] seeds)
	{
		system.out.println("test");
		for(int i=0;i<seeds.length;i++)
			LinkDb.addUnvisitedUrl(seeds[i]);
	}
	
	/* 爬取方法*/
	public void crawling(String[] seeds)
	{
		LinkFilter filter = new LinkFilter(){
			//提取以 http://www.twt.edu.cn 开头的链接
			public boolean accept(String url) {
				if(url.startsWith("https://www.qian360.com"))
					return true;
				else
					return false;
			}
		};
		//初始化 URL 队列
		initCrawlerWithSeeds(seeds);
		//循环条件：待抓取的链接不空且抓取的网页不多于 1000
		while(!LinkDb.unVisitedUrlsEmpty()&&LinkDb.getVisitedUrlNum()<=1000)
		{
			//队头 URL 出对
			String visitUrl=LinkDb.unVisitedUrlDeQueue();
			if(visitUrl==null)
				continue;
			FileDownLoader downLoader=new FileDownLoader();
			//下载网页
			downLoader.downloadFile(visitUrl);
			//该 url 放入到已访问的 URL 中
			LinkDb.addVisitedUrl(visitUrl);
			//提取出下载网页中的 URL
			
			Set<String> links=HtmlParserTool.extracLinks(visitUrl,filter);
			//新的未访问的 URL 入队
			for(String link:links)
			{
					LinkDb.addUnvisitedUrl(link);
			}
		}
	}
	//main 方法入口
	public static void main(String[]args)
	{
		Crawler crawler = new Crawler();
		crawler.crawling(new String[]{"http://www.erongdu.com/index.html"});
	}
}
