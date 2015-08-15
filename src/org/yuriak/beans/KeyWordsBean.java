package org.yuriak.beans;

import java.util.ArrayList;

public class KeyWordsBean {
	public double getEmotion() {
		return emotion;
	}
	public void setEmotion(double emotion) {
		this.emotion = emotion;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public ArrayList<CommonNewsBean> getNews() {
		return news;
	}
	public void setNews(ArrayList<CommonNewsBean> news) {
		this.news = news;
	}
	private double emotion;
	private String keywords;
	private ArrayList<CommonNewsBean> news;
}
