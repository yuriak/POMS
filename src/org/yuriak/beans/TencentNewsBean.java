package org.yuriak.beans;

import java.util.ArrayList;

public class TencentNewsBean extends CommonNewsBean {

	public ArrayList<TencentCommentBean> getComments() {
		return comments;
	}
	public void setComments(ArrayList<TencentCommentBean> comments) {
		this.comments = comments;
	}
	public String getSourse() {
		return sourse;
	}
	public void setSourse(String sourse) {
		this.sourse = sourse;
	}
	public String getArticalId() {
		return articalId;
	}
	public void setArticalId(String articalId) {
		this.articalId = articalId;
	}
	private String sourse;
	private String articalId;
	private	ArrayList<TencentCommentBean> comments;
}
