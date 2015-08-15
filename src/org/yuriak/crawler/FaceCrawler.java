package org.yuriak.crawler;


import java.io.File;

import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;
import cn.edu.hfut.dmic.webcollector.weiboapi.WeiboCN;

public class FaceCrawler extends DeepCrawler {

	public FaceCrawler(String crawlPath) throws Exception {
		super(crawlPath);
//		String cookie = WeiboCN.getSinaCookie("yurix@sohu.com", "yurix301604");
		String cookie="_T_WM=4f932647095bd9356b842eff27319eba; SUB=_2A254UxQLDeTxGeNI61YY8ynLyzqIHXVbv7xDrDV6PUJbrdANLVPukW0Wxl4uF-j9LwUpYNXIdSHy2kGp8A..; SUHB=0D-Vnq3wWNoQsx; SSOLoginState=1431790683; M_WEIBOCN_PARAMS=featurecode%3D20000180%26oid%3D3843269253159563%26luicode%3D20000061%26lfid%3D3843269253159563";
		HttpRequesterImpl myRequester=(HttpRequesterImpl) 
		this.getHttpRequester();
        myRequester.setCookie(cookie);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		// TODO Auto-generated method stub
		Document document=page.getDoc();
		Elements ele=document.select("#face-list-items");
		System.out.println(ele.size());
		for (Element element : ele) {
			System.out.println(element.getElementsByTag("i").get(0).attr("data-face").toString());
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
//		FaceCrawler crawler=new FaceCrawler("result/");
//		crawler.addSeed("http://m.weibo.cn/comment?id=3843269253159563");
//		crawler.start(1);
		String string=new String(FileUtils.readFile(new File("resources/Face.txt")),"UTF-8");
		JSONArray jsonArray=new JSONArray(string);
		for (int i = 0; i < jsonArray.length(); i++) {
			System.out.println(jsonArray.getJSONObject(i).get("value"));
			
		}
	}

}
