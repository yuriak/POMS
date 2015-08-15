package org.yuriak.beans;

import java.util.ArrayList;

public class WeiboNewsBean extends CommonNewsBean {

	public ArrayList<WeiboCommentBean> getCommentList() {
		return commentList;
	}
	public void setCommentList(ArrayList<WeiboCommentBean> commentList) {
		this.commentList = commentList;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCommentUrl() {
		return commentUrl;
	}
	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}
	
	private String source;
	private String commentUrl;
	private String content;
	private ArrayList<WeiboCommentBean> commentList;
}
