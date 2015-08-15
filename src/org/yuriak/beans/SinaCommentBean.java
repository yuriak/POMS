package org.yuriak.beans;

public class SinaCommentBean extends CommonCommentBean {
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	private String userType;
}
