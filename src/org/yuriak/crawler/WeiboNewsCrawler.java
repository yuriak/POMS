package org.yuriak.crawler;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.yuriak.beans.WeiboNewsBean;
import org.yuriak.util.MyLog;
import org.yuriak.util.XmlUtil;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.weiboapi.WeiboCN;

public class WeiboNewsCrawler extends DeepCrawler {

	private  WeiboNewsCrawler crawler;
	public  ArrayList<WeiboNewsBean> newsList;
	private String username;
	private String password;
	public int NewsNumber;
	public int status;
	public WeiboNewsCrawler(String crawlPath) throws Exception {
		super(crawlPath);
		this.newsList=new ArrayList<>();
		status=0;
		NewsNumber=0;
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
//		System.out.println(page.getUrl());
		MyLog.INFO("crawling: "+page.getUrl());
		this.status=1;
		Elements content=page.getDoc().select(".c");
        for(Element weibo:content){
        	WeiboNewsBean wbNewsBean=new WeiboNewsBean();
        	if (!weibo.select(".nk").html().equals("")) {
        		if (weibo.select(".ctt").text().substring(0,1).equals(":")) {
        			wbNewsBean.setContent(weibo.select(".ctt").text().substring(1,weibo.select(".ctt").text().length()));
				}else {
					wbNewsBean.setContent(weibo.select(".ctt").text());
				}
        		wbNewsBean.setSource(weibo.select(".nk").text());
        		wbNewsBean.setCommentUrl(weibo.select(".cc").attr("href"));
        		wbNewsBean.setEmotion(0);
        		newsList.add(wbNewsBean);
        		this.NewsNumber=newsList.size();
			}
        }
		return null;
	}
	
//	public static WeiboNewsCrawler getCrawler() throws Exception{
//		if (crawler==null) {
//			crawler=new WeiboNewsCrawler("result/");
//		}
//		return crawler;
//	}
	
	public ArrayList<WeiboNewsBean> getNews(String username,String password,int newsNumber,int threadNumber) throws Exception{
		if (newsNumber<10) {
			throw new Exception("Page<=0");
		}
		if (threadNumber<=0) {
			throw new Exception("threadNumber<=0");
		}
		MyLog.INFO("crawler start.");
		String cookie = WeiboCN.getSinaCookie(username, password);
		HttpRequesterImpl myRequester=(HttpRequesterImpl) 
		this.getHttpRequester();  
        myRequester.setCookie(cookie);
        this.username=username;
        this.password=password;
        this.status=0;
        this.NewsNumber=0;
        this.setThreads(threadNumber);
        this.addSeed("");
		for (int i = 1; i <= newsNumber/10; i++) {
			this.seeds.set(0, "http://weibo.cn/?page=" + i);
			this.start(1);
		}
		this.status=0;
		MyLog.INFO("crawler stop.");
		return this.newsList;
	}
	
	public static void main(String[] args) throws Exception {
		final WeiboNewsCrawler newsCrawler=new WeiboNewsCrawler("result/");
		System.out.println(newsCrawler.getNews("yurix@sohu.com", "yurix301604", 100, 10).size());
		
//		final Thread thread=new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		Thread tthThread=new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while (thread.isAlive()) {
//					System.out.println(newsCrawler.NewsNumber);
//				}
//			}
//		});
//		
//		thread.start();
//		tthThread.start();
//		
	}

}
