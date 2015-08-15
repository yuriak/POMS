package org.yuriak.beans;

import java.util.ArrayList;

public class CommonNewsBean {
	public double getEmotion() {
		return emotion;
	}
	public void setEmotion(double emotion) {
		this.emotion = emotion;
	}
	public ArrayList<CommonCommentBean> getCommonList() {
		return commonList;
	}
	public void setCommonList(ArrayList<CommonCommentBean> commonList) {
		this.commonList = commonList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	protected String title;
	protected String url;
	protected String time;
	protected String intro;
	protected ArrayList<CommonCommentBean> commonList;
	protected double emotion;
}
