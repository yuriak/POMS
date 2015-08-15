package org.yuriak.crawler;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

import org.yuriak.beans.*;
import org.yuriak.util.MyLog;

import com.mysql.jdbc.TimeUtil;

public class TencentCommentCrawler extends DeepCrawler {

	private int status;
	private int commentNumber;
	private String lastID;
	private String firstID;
	private boolean hasnext=true;
	private int totalNumber=0;
	private int retNumber=0;
	public ArrayList<TencentCommentBean> commentList;
	public TencentCommentCrawler(String crawlPath) {
		super(crawlPath);
		commentList=new ArrayList<>();
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		MyLog.INFO("crawling "+page.getUrl());
		JSONObject content=new JSONObject(page.getHtml());
		firstID=content.getJSONObject("data").get("first").toString();
		lastID=content.getJSONObject("data").get("last").toString();
		hasnext=Boolean.valueOf(content.getJSONObject("data").get("hasnext").toString());
		totalNumber=Integer.valueOf(content.getJSONObject("data").get("total").toString());
		retNumber=Integer.valueOf(content.getJSONObject("data").get("retnum").toString());
		JSONArray commentArray=content.getJSONObject("data").getJSONArray("commentid");
		for (int i = 0; i < commentArray.length(); i++) {
			TencentCommentBean commentBean=new TencentCommentBean();
			commentBean.setId(commentArray.getJSONObject(i).get("id").toString());
			commentBean.setContent(commentArray.getJSONObject(i).get("content").toString());
			commentBean.setTime(org.yuriak.util.TimeUtil.convertLongToDateString(commentArray.getJSONObject(i).get("time").toString()));
			commentBean.setEmotion(0);
			commentList.add(commentBean);
			commentNumber=commentList.size();
		}
		return null;
	}
	
	public ArrayList<TencentCommentBean> getComment(String articalId,int commentNumber,int threadNumber) throws Exception{
		if (articalId.equals("")) {
			return this.commentList;
		}
		if (commentNumber<50) {
			throw new Exception("Page<=0");
		}
		if (threadNumber<=0) {
			throw new Exception("threadNumber<=0");
		}
		this.commentList=new ArrayList<>();
		this.firstID="0";
		this.lastID="0";
		String url="http://coral.qq.com/article/"+articalId+"/comment?commentid=0&reqnum=50";
		this.addSeed(url);
		this.setThreads(10);
		for (int i = 1; i <= commentNumber/50; i++) {
			if (this.lastID.equals("false")) {
				break;
			}
			this.seeds.set(0, "http://coral.qq.com/article/"+articalId+"/comment?commentid="+this.lastID+"&reqnum=50");
			this.start(1);
		}
		return this.commentList;
	}
}
