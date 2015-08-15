package org.yuriak.crawler;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.yuriak.beans.WeiboCommentBean;
import org.yuriak.util.XmlUtil;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.weiboapi.WeiboCN;

public class WeiboCommentCrawler extends DeepCrawler {
	private ArrayList<WeiboCommentBean> commentList;
	private int status=0;
	private int commentNumber;
	public WeiboCommentCrawler(String crawlPath) throws Exception {
		super(crawlPath);
		XmlUtil xmlUtil=new XmlUtil("resources/WeiboInfo.xml");
		String username=xmlUtil.getConfigValue("Username");
		String password=xmlUtil.getConfigValue("Password");
		String cookie = WeiboCN.getSinaCookie(username, password);
		HttpRequesterImpl myRequester=(HttpRequesterImpl) this.getHttpRequester();  
        myRequester.setCookie(cookie);
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		Elements content=page.getDoc().select(".c");
		for(Element weibo:content){
			if (!weibo.select(".ctt").html().equals("")&&!weibo.attr("id").equals("M_")) {
				WeiboCommentBean commentBean=new WeiboCommentBean();
				commentBean.setId(weibo.attr("id"));
				commentBean.setContent(weibo.select(".ctt").text());
				commentBean.setEmotion(0);
			}
		}
		return null;
	}
	
	public static ArrayList<WeiboCommentBean> getComment(String commentUrl,int pageNumber,int threadNumber,String filePath) throws Exception{
		if (commentUrl.equals("")) {
			throw new Exception("commentUrl=null");
		}
		if (pageNumber<=0) {
			throw new Exception("Page<=0");
		}
		if (threadNumber<=0) {
			throw new Exception("threadNumber<=0");
		}
		commentUrl=commentUrl.split("#")[0];
		WeiboCommentCrawler crawler=new WeiboCommentCrawler(filePath);
		crawler.setThreads(threadNumber);
		crawler.addSeed(commentUrl);
		for (int i = 1; i <= pageNumber; i++) {
			crawler.seeds.set(0, commentUrl+"&page="+i);
			crawler.start(1);
		}
		return crawler.commentList;
	}
	
	public static void main(String[] args) throws Exception {
		getComment("http://weibo.cn/comment/ChVKf5m6H?uid=1589353923&rl=0&gid=10001#cmtfrm", 3, 10, "result/");
	}
	
}
