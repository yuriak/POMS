package org.yuriak.crawler;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.yuriak.beans.*;
import org.yuriak.util.MyLog;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

public class SinaCommentCrawler extends DeepCrawler {
	private ArrayList<SinaCommentBean> comments=new ArrayList<>();
	private int commentNumber=0;
	private int status=1;
	public SinaCommentCrawler(String crawlPath) {
		super(crawlPath);
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		MyLog.INFO("crawling "+page.getUrl());
		String content=page.getHtml();
		content=content.substring(9,content.length());
		JSONObject jObject=new JSONObject(content);
		try {
			JSONArray cmntArray=jObject.getJSONObject("result").getJSONArray("cmntlist");
			if (cmntArray!=null) {
				for (int i = 0; i < cmntArray.length(); i++) {
					SinaCommentBean sCommentBean=new SinaCommentBean();
					sCommentBean.setContent(cmntArray.getJSONObject(i).get("content").toString());
					sCommentBean.setUserType(cmntArray.getJSONObject(i).get("usertype").toString());
					sCommentBean.setTime(cmntArray.getJSONObject(i).get("time").toString());
					sCommentBean.setId(cmntArray.getJSONObject(i).get("mid").toString());
					sCommentBean.setEmotion(0);
					comments.add(sCommentBean);
					commentNumber=comments.size();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ArrayList<SinaCommentBean> getComment(String channel,String commentId,int commentNumber,int threadNumber) throws Exception{
		
		
			if (channel.equals("")) {
				return this.comments;
			}
			if (commentId.equals("")) {
				return this.comments;
			}
			if (commentNumber<=0) {
				return this.comments;
			}
			if (threadNumber<=0) {
				return this.comments;
			}
			this.comments=new ArrayList<>();
			this.commentNumber=0;
			this.status=1;
			this.setThreads(threadNumber);
			this.addSeed("");
			for (int i = 1; i <= commentNumber/100; i++) {
				this.seeds.set(0,"http://comment5.news.sina.com.cn/page/info?format=js&channel="+channel+"&newsid="+commentId+"&compress=1&page="+i+"&page_size=100");
				this.start(1);
			}
			this.status=0;
			return this.comments;
	}
	
	public static void main(String[] args) throws Exception {
//		SinaCommentCrawler crawler=new SinaCommentCrawler("result");
//		ArrayList<SinaCommentBean> beans=crawler.getComment("yx", "115-15-415718", 100, 10);
//		System.out.println(beans.size());
	}
}
