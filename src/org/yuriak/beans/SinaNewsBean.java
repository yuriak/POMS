package org.yuriak.beans;

import java.util.ArrayList;

public class SinaNewsBean extends CommonNewsBean {
	public ArrayList<SinaCommentBean> getComments() {
		return comments;
	}
	public void setComments(ArrayList<SinaCommentBean> comments) {
		this.comments = comments;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	private String keyWords;
	private String commentId;
	private String channel;
	private ArrayList<SinaCommentBean> comments;
}
