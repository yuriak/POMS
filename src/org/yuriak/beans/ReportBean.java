package org.yuriak.beans;

import java.sql.*;

public class ReportBean implements Comparable<ReportBean> {
	
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public double getEmotion() {
		return emotion;
	}
	public void setEmotion(double emotion) {
		this.emotion = emotion;
	}
	private Timestamp time;
	private double emotion;
	@Override
	
	public int compareTo(ReportBean o) {
		// TODO Auto-generated method stub
		if (o.getTime().after(this.getTime())) {
			return -1;
		}else if (o.getTime().before(this.getTime())) {
			return 1;
		} else {
			return 0;
		}
	}
}
