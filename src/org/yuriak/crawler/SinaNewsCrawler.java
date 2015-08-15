package org.yuriak.crawler;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.MyLog;
import org.yuriak.beans.*;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

public class SinaNewsCrawler extends DeepCrawler {
	
	private ArrayList<SinaNewsBean> news;
	private int newsNumber=0;
	private int status=0;
	public SinaNewsCrawler(String crawlPath) {
		super(crawlPath);
		
	}
	
	@Override
	public Links visitAndGetNextLinks(Page page) {
		MyLog.INFO("crawling "+page.getUrl());
		JSONObject jsonObject=new JSONObject(page.getHtml());
		int newsNumber=jsonObject.getJSONObject("result").getJSONArray("list").length();
		JSONArray newsArray=jsonObject.getJSONObject("result").getJSONArray("list");
		for (int i = 0; i < newsNumber; i++) {
			String commentId=newsArray.getJSONObject(i).get("cid").toString();
			if (!commentId.equals("")) {
				SinaNewsBean snb=new SinaNewsBean();
				snb.setTitle(newsArray.getJSONObject(i).get("title").toString());
				snb.setIntro(newsArray.getJSONObject(i).get("intro").toString());
				snb.setUrl(newsArray.getJSONObject(i).get("url").toString());
				snb.setKeyWords(newsArray.getJSONObject(i).get("kl").toString());
				snb.setTime(newsArray.getJSONObject(i).get("datetime").toString());
				snb.setCommentId(commentId.split(":")[1]);
				snb.setChannel(commentId.split(":")[0]);
				snb.setEmotion(0);
				news.add(snb);
				this.newsNumber=news.size();
			}
		}
		return null;
	}
	
	
	public ArrayList<SinaNewsBean> getNews(String keyWord,int newsNumber,int threadNumber) throws Exception{
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
		this.setThreads(20);
		this.newsNumber=0;
		this.status=1;
		String encodedKeyWord=URLEncoder.encode(keyWord, "UTF-8");
		this.addSeed("");
		for (int i = 1; i <= newsNumber/20; i++) {
			MyLog.INFO("crawling \""+keyWord+"\"...");
			this.seeds.set(0,"http://api.search.sina.com.cn/?q="+encodedKeyWord+"&range=title&c=news&sort=time&page="+i);
			this.start(1);
		}
		this.status=0;
		return this.news;
	}
	
	public static void main(String[] args) throws Exception {
		SinaNewsCrawler crawler=new SinaNewsCrawler("result/");
//		crawler.getNews("习近平", 10, "result/", 20,10);
	}
}
