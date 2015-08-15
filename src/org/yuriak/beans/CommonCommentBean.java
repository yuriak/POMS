package org.yuriak.beans;

public class CommonCommentBean {
	public double getEmotion() {
		return emotion;
	}
	public void setEmotion(double emotion) {
		this.emotion = emotion;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	protected String content;
	protected String time;
	protected String id;
	protected double emotion;
}
