package org.yuriak.crawler;


import java.net.URLEncoder;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.yuriak.beans.*;
import org.yuriak.util.MyLog;
public class TencentNewsCrawler extends DeepCrawler {
	private ArrayList<TencentNewsBean> news;
	private int newsNumber=0;
	private int status=0;
	public TencentNewsCrawler(String crawlPath) {
		super(crawlPath);
	}
	@Override
	public Links visitAndGetNextLinks(Page page) {
		TencentNewsBean tnb=new TencentNewsBean();
		Links newsLinks=new Links();
		Document document=page.getDoc();
		if (page.getUrl().split("/")[2].equals("news.sogou.com")) {
			Elements block=document.select(".rb");
			String tmpUrl="";
			for (Element element : block) {
				String url=element.select(".pp").attr("href").split("[?]")[0];
				if (!tmpUrl.equals(url)) {
					tmpUrl=url;
					newsLinks.add(url);
				}
			}
			return newsLinks;
		}else if (page.getUrl().split("/")[2].equals("news.qq.com")&&page.getUrl().split("/")[3].equals("a")) {
			MyLog.INFO("crawling "+page.getUrl());
			String content=page.getHtml();
			int cmt_id_index=content.indexOf("cmt_id");
			if (cmt_id_index!=-1) {
				String cmtid=content.substring(cmt_id_index,content.indexOf(";", cmt_id_index));
				cmtid=cmtid.split(" ")[2];
				tnb.setArticalId(cmtid);
				tnb.setTitle(page.getDoc().getElementsByTag("h1").text());
				tnb.setEmotion(0);
				Elements body=document.getElementsByAttributeValue("style", "TEXT-INDENT: 2em");
				if (body.size()>0) {
					tnb.setIntro(body.get(0).text());
				}else {
					tnb.setIntro("");
				}
				tnb.setTime(document.select("span[class$=time]").text());
				news.add(tnb);
				newsNumber=news.size();
			}
			return null;
		} else {
			return null;
		}
	}
	
	public ArrayList<TencentNewsBean> getNews(String keyWord,int newsNumber,int threadNumber) throws Exception{
		if (keyWord.equals("")) {
			throw new Exception("KeyWords is null");
		}
		if (newsNumber<=0) {
			throw new Exception("Page<=0");
		}
		if (threadNumber<=0) {
			throw new Exception("threadNumber<=0");
		}
		this.news=new ArrayList<>();
		this.status=1;
		this.newsNumber=0;
		this.addSeed("");
		this.setThreads(threadNumber);
		String encodedKeyword=URLEncoder.encode(keyWord, "UTF-8");
		for (int i = 1; i <= newsNumber/10; i++) {
			this.seeds.set(0,("http://news.sogou.com/news?time=0&query=site:news.qq.com "+encodedKeyword+"&mode=2&sort=1&page="+i));
			this.start(2);
		}
		this.status=0;
		return this.news;
	}
	
//	public static void main(String[] args) {
//		try {
//			TencentNewsCrawler.getNews("手机", 10, "result/", 20);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
