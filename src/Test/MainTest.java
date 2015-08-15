package Test;

import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.common.config.CommonValues;
import org.haikism.LIBSVM.AnalysisBaseOnLIBSVM;
import org.haikism.config.FileOperation;
import org.jfree.data.xy.DefaultXYDataset;
import org.springframework.beans.factory.support.ManagedArray;
import org.yuriak.beans.CommonCommentBean;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;
import org.yuriak.beans.ReportBean;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.TimeUtil;


public class MainTest {
	public static ArrayList<ReportBean> reportList;
	
	public static void main(String[] args) throws Exception {
		MainTest mainTest=new MainTest();
		mainTest.initReportList();
		
		
		
//		ArrayList<String> xAixStr=new ArrayList<>();
//		ArrayList<Timestamp> timeList=new ArrayList<>();
//		ArrayList<Double> xValue=new ArrayList<>();
//		ArrayList<Double> gyValue=new ArrayList<>();
//		ArrayList<Double> byValue=new ArrayList<>();
//		Calendar calendar=Calendar.getInstance();
		Timestamp firstDay=reportList.get(0).getTime();
		Timestamp lastDay=reportList.get(reportList.size()-1).getTime();
		Calendar fCalendar=Calendar.getInstance();
		Calendar lCalendar=Calendar.getInstance();
		fCalendar.setTime(firstDay);
		lCalendar.setTime(lastDay);
//		System.out.println(TimeUtil.getDaysBetween(lCalendar,fCalendar));
//		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//		String tmpDay=df.format(firstDay);
//		Timestamp tDay=firstDay;
//		double x=0;
//		double gy=0;
//		double by=0;
//		for (ReportBean bean : reportList) {
//			if (df.format(bean.getTime()).equals(tmpDay)) {
//				if (bean.getEmotion()>0) {
//					gy++;
//				}else {
//					by++;
//				}
//			}else {
//				xAixStr.add(tmpDay);
//				xValue.add(x);
//				gyValue.add(gy);
//				byValue.add(by);
//				tmpDay=df.format(bean.getTime());
//				tDay=bean.getTime();
//				gy=0;
//				by=0;
//				x++;
//				if (bean.getEmotion()>0) {
//					gy++;
//				}else {
//					by++;
//				}
//			}
//		}
//		xAixStr.add(tmpDay);
//		timeList.add(tDay);
//		xValue.add(x);
//		gyValue.add(gy);
//		byValue.add(by);
//		double[] xv=new double[xValue.size()];
//		double[] gyV=new double[gyValue.size()];
//		double[] byV=new double[byValue.size()];
//		for (int i=0;i<xValue.size();i++) {
//			xv[i]=xValue.get(i);
//		}
//		for (int i=0;i<byValue.size();i++) {
//			byV[i]=byValue.get(i);
//		}
//		for (int i=0;i<gyValue.size();i++) {
//			gyV[i]=gyValue.get(i);
//		}
		
	}
	
	private void initReportList() throws Exception{
		CommonValues.AW_keywordList=MyFileUtil.readAnalyzeFromFile(new File("D:\\java\\J2EE\\POMS\\data\\analyze.txt"));
		reportList=new ArrayList<>();
		for (KeyWordsBean bean : CommonValues.AW_keywordList) {
			for (CommonNewsBean newsBean : bean.getNews()) {
				if (newsBean.getCommonList().size()==0) {
					continue;
				}else {
					for (CommonCommentBean commentBean : newsBean.getCommonList()) {
						ReportBean reportBean=new ReportBean();
						reportBean.setEmotion(commentBean.getEmotion());
						reportBean.setTime(TimeUtil.convertStringToTimeStamp(commentBean.getTime()));
						reportList.add(reportBean);
					}
				}
			}
		}
		Collections.sort(reportList);
	}
}
