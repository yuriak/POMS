package org.yuriak.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.CellFeatures;
import jxl.CellType;
import jxl.DateCell;
import jxl.format.CellFormat;
import jxl.write.DateTime;

import org.apache.bcel.generic.NEW;
import org.common.config.CommonValues;
import org.haikism.config.FileOperation;
import org.jfree.io.FileUtilities;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.universalchardet.prober.SBCSGroupProber;
import org.yuriak.beans.CommonCommentBean;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;
import org.yuriak.beans.WeiboNewsBean;

import com.sleepycat.je.cleaner.FileSelector;
import com.sleepycat.je.utilint.Timestamp;
import com.sun.jna.platform.win32.WinDef.LONG;

import cn.edu.hfut.dmic.webcollector.util.FileUtils;

public class MyFileUtil {

	public static void AppendFile(String fileName,String content){
		OutputStreamWriter writer=null;
        try { 
        	writer = new OutputStreamWriter(new FileOutputStream(fileName, true),"GBK");
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(fileName, true);     
            writer.write(content);       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
	}
	
	public static String[] getKeyWordsFromFile() throws IOException{
		byte[] by=FileUtils.readFile(new File("resources/key.txt"));
		String content=new String(by,"UTF-8");
		String[] row=content.split("\n");
		for (int i = 0; i < row.length; i++) {
			row[i]=row[i].substring(row[i].indexOf("：")+1,row[i].length());
		}
		return row;
	}
	
//	public static ArrayList<WeiboNewsBean> readMaterialFromFile(File file) throws Exception{
//		ArrayList<WeiboNewsBean> weiboNewsBeans=new ArrayList<>();
//		byte[] by=FileUtils.readFile(file);
//		String content=new String(by,"UTF-8");
//		String[] row=content.split("\r\n");
//		if (!row[0].equals("POMS_EVENT")) {
//			throw new Exception("unsupport file");
//		}
//		for (int i = 1; i < row.length; i++) {
//			WeiboNewsBean bean=new WeiboNewsBean();
//			String[] element=row[i].split("\\|");
//			bean.setSource(element[0]);
//			bean.setContent(element[1]);
//			bean.setCommentUrl(element[2]);
//			weiboNewsBeans.add(bean);
//		}
//		return weiboNewsBeans;
//	}
	
	public static ArrayList<WeiboNewsBean> readMaterialFromFile(File file) throws Exception{
		ArrayList<WeiboNewsBean> weiboNewsBeans=new ArrayList<WeiboNewsBean>();
		String fileContent=new String(FileUtils.readFile(file),"UTF-8");
		JSONObject mainObject=new JSONObject(fileContent);
		JSONArray mainArray=mainObject.getJSONArray("POMS_EVENT");
		if (mainArray!=null) {
			for (int i = 0; i < mainArray.length(); i++) {
				JSONObject weiboObject=mainArray.getJSONObject(i);
				WeiboNewsBean bean=new WeiboNewsBean();
				bean.setSource(weiboObject.getString("source"));
				bean.setContent(weiboObject.getString("content"));
				bean.setCommentUrl(weiboObject.getString("commentUrl"));
				weiboNewsBeans.add(bean);
			}
		}
		return weiboNewsBeans;
	}
	
	
	
	
//	public static void writeMaterialToFile(ArrayList<WeiboNewsBean> weiboNewsBeans,File destFilePath) throws FileNotFoundException, IOException{
//		StringBuffer output=new StringBuffer();
//		output.append("POMS_EVENT\r\n");
//		for (WeiboNewsBean weiboNewsBean : weiboNewsBeans) {
//			output.append(weiboNewsBean.getSource());
//			output.append("|");
//			output.append(weiboNewsBean.getContent());
//			output.append("|");
//			output.append(weiboNewsBean.getCommentUrl());
//			output.append("\r\n");
//		}
//		FileUtils.writeFile(destFilePath, output.toString(), "UTF-8");
//	}
	
	public static void writeMaterialToFile(ArrayList<WeiboNewsBean> weiboNewsBeans,File destFilePath) throws Exception{
		JSONObject mainObject=new JSONObject();
		JSONArray mainArray=new JSONArray();
		
		if (weiboNewsBeans!=null) {
			for (WeiboNewsBean weiboNewsBean : weiboNewsBeans) {
				JSONObject newsObj=new JSONObject();
				newsObj.put("source", weiboNewsBean.getSource());
				newsObj.put("content", weiboNewsBean.getContent());
				newsObj.put("commentUrl", weiboNewsBean.getCommentUrl());
				mainArray.put(newsObj);
			}
		}
		mainObject.put("POMS_EVENT", mainArray);
		FileUtils.writeFile(destFilePath, mainObject.toString(), "UTF-8");
	}
	
	public static ArrayList<KeyWordsBean> readCommentsFromFile(File file) throws Exception{
		ArrayList<KeyWordsBean> keyWordsBeans=new ArrayList<>();
		String fileContent=new String(FileUtils.readFile(file),"UTF-8");
		JSONObject mainObj=new JSONObject(fileContent);
		JSONArray array=mainObj.getJSONArray("POMS_COMMENT");
		if (array!=null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject keywordObj=array.getJSONObject(i);
				KeyWordsBean keyWordsBean=new KeyWordsBean();
				keyWordsBean.setKeywords(keywordObj.get("keyword").toString());
				keyWordsBean.setEmotion(keywordObj.getDouble("emotion"));
				JSONArray newsArray=keywordObj.getJSONArray("news");
				ArrayList<CommonNewsBean> newsList=new ArrayList<>();
				if (newsArray!=null) {
					for (int j = 0; j < newsArray.length(); j++) {
						JSONObject newsObj=newsArray.getJSONObject(j);
						CommonNewsBean newsBean=new CommonNewsBean();
						newsBean.setTitle(newsObj.getString("title"));
						if (newsObj.get("intro")!=null) {
							newsBean.setIntro(newsObj.getString("intro"));
						}
						newsBean.setTime(newsObj.getString("time"));
						newsBean.setEmotion(newsObj.getDouble("emotion"));
						ArrayList<CommonCommentBean> commentList=new ArrayList<>();
						JSONArray commentsArray=newsObj.getJSONArray("comments");
						if (commentsArray!=null) {
							for (int k = 0; k < commentsArray.length(); k++) {
								JSONObject commentObj=commentsArray.getJSONObject(k);
								CommonCommentBean commentBean=new CommonCommentBean();
								commentBean.setContent(commentObj.getString("content"));
								commentBean.setTime(commentObj.getString("time"));
								commentBean.setEmotion(commentObj.getDouble("emotion"));
								commentList.add(commentBean);
							}
							newsBean.setCommonList(commentList);
						}
						newsList.add(newsBean);
					}
					keyWordsBean.setNews(newsList);
				}
				keyWordsBeans.add(keyWordsBean);
			}
		}
		return keyWordsBeans;
	}
	
	public static void writeCommentsToFile(ArrayList<KeyWordsBean> keyWordsBeans,File destFilePath) throws Exception{
		StringBuffer output=new StringBuffer();
		JSONObject mainObj=new JSONObject();
		JSONArray array=new JSONArray();
		if (keyWordsBeans!=null) {
			for (KeyWordsBean keyWordsBean : keyWordsBeans) {
				JSONObject keyword=new JSONObject();
				JSONArray newsArray=new JSONArray();
				if (keyWordsBean.getNews()!=null) {
					for (CommonNewsBean commonNewsBean:keyWordsBean.getNews()) {
						JSONObject news=new JSONObject();
						news.put("title", commonNewsBean.getTitle());
						news.put("intro", commonNewsBean.getIntro()==null?"":commonNewsBean.getIntro());
						news.put("time", commonNewsBean.getTime());
						news.put("emotion", commonNewsBean.getEmotion());
						JSONArray comments=new JSONArray();
						if (commonNewsBean.getCommonList()!=null) {
							for (CommonCommentBean comment : commonNewsBean.getCommonList()) {
								JSONObject jsonObject=new JSONObject();
								jsonObject.put("content", comment.getContent());
								jsonObject.put("time", comment.getTime());
								jsonObject.put("emotion", comment.getEmotion());
								comments.put(jsonObject);
							}
						}
						news.put("comments", comments);
						newsArray.put(news);
					}
				}
				keyword.put("news", newsArray);
				keyword.put("keyword", keyWordsBean.getKeywords());
				keyword.put("emotion", keyWordsBean.getEmotion());
				array.put(keyword);
			}
			mainObj.put("POMS_COMMENT", array);
		}
		output.append(mainObj.toString());
		FileUtils.writeFile(destFilePath, output.toString(), "UTF-8");
	}
	
	public static void writeAnalyzeToFile(ArrayList<KeyWordsBean> keyWordsBeans,File destFilePath) throws Exception{
		StringBuffer output=new StringBuffer();
		JSONObject mainObj=new JSONObject();
		JSONArray array=new JSONArray();
		if (keyWordsBeans!=null) {
			for (KeyWordsBean keyWordsBean : keyWordsBeans) {
				JSONObject keyword=new JSONObject();
				JSONArray newsArray=new JSONArray();
				if (keyWordsBean.getNews()!=null) {
					for (CommonNewsBean commonNewsBean:keyWordsBean.getNews()) {
						JSONObject news=new JSONObject();
						news.put("title", commonNewsBean.getTitle());
						news.put("intro", commonNewsBean.getIntro()==null?"":commonNewsBean.getIntro());
						news.put("time", commonNewsBean.getTime());
						news.put("emotion", commonNewsBean.getEmotion());
						JSONArray comments=new JSONArray();
						if (commonNewsBean.getCommonList()!=null) {
							for (CommonCommentBean comment : commonNewsBean.getCommonList()) {
								JSONObject jsonObject=new JSONObject();
								jsonObject.put("content", comment.getContent());
								jsonObject.put("time", comment.getTime());
								jsonObject.put("emotion", comment.getEmotion());
								comments.put(jsonObject);
							}
						}
						news.put("comments", comments);
						newsArray.put(news);
					}
				}
				keyword.put("news", newsArray);
				keyword.put("keyword", keyWordsBean.getKeywords());
				keyword.put("emotion", keyWordsBean.getEmotion());
				array.put(keyword);
			}
			mainObj.put("POMS_ANALYZE", array);
		}
		output.append(mainObj.toString());
		FileUtils.writeFile(destFilePath, output.toString(), "UTF-8");
	}
	
	public static ArrayList<KeyWordsBean> readAnalyzeFromFile(File file) throws Exception{
		ArrayList<KeyWordsBean> keyWordsBeans=new ArrayList<>();
		String fileContent=new String(FileUtils.readFile(file),"UTF-8");
		JSONObject mainObj=new JSONObject(fileContent);
		JSONArray array=mainObj.getJSONArray("POMS_ANALYZE");
		if (array!=null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject keywordObj=array.getJSONObject(i);
				KeyWordsBean keyWordsBean=new KeyWordsBean();
				keyWordsBean.setKeywords(keywordObj.get("keyword").toString());
				keyWordsBean.setEmotion(keywordObj.getDouble("emotion"));
				JSONArray newsArray=keywordObj.getJSONArray("news");
				ArrayList<CommonNewsBean> newsList=new ArrayList<>();
				if (newsArray!=null) {
					for (int j = 0; j < newsArray.length(); j++) {
						JSONObject newsObj=newsArray.getJSONObject(j);
						CommonNewsBean newsBean=new CommonNewsBean();
						newsBean.setTitle(newsObj.getString("title"));
						if (newsObj.get("intro")!=null) {
							newsBean.setIntro(newsObj.getString("intro"));
						}
						newsBean.setTime(newsObj.getString("time"));
						newsBean.setEmotion(newsObj.getDouble("emotion"));
						ArrayList<CommonCommentBean> commentList=new ArrayList<>();
						JSONArray commentsArray=newsObj.getJSONArray("comments");
						if (commentsArray!=null) {
							for (int k = 0; k < commentsArray.length(); k++) {
								JSONObject commentObj=commentsArray.getJSONObject(k);
								CommonCommentBean commentBean=new CommonCommentBean();
								commentBean.setContent(commentObj.getString("content"));
								commentBean.setTime(commentObj.getString("time"));
								commentBean.setEmotion(commentObj.getDouble("emotion"));
								commentList.add(commentBean);
							}
							newsBean.setCommonList(commentList);
						}
						newsList.add(newsBean);
					}
					keyWordsBean.setNews(newsList);
				}
				keyWordsBeans.add(keyWordsBean);
			}
		}
		return keyWordsBeans;
	}
	
	public static double[] readTmpAnalyzeValue(File file) throws Exception{
		List<String> oriStr=org.apache.commons.io.FileUtils.readLines(file, "UTF-8");
		double[] values=new double[oriStr.size()];
		for (int i = 0; i < values.length; i++) {
			values[i]=Double.parseDouble(oriStr.get(i));
		}
		return values;
	}
	
	public static void main(String[] args) throws Exception {
//		System.out.println(getMd5ByFile(new File(CommonValues.LIBSVM_MODEL_PATH)));
	}
	
	public static boolean checkFile(){
		File dataFile=new File(CommonValues.DATA_DIR);
		File resFile=new File(CommonValues.RES_DIR);
		File svmModelFile=new File(CommonValues.LIBSVM_MODEL_PATH);
		File subModelFile=new File(CommonValues.SUBOBJ_MODEL_PATH);
		File emoVocFile=new File(CommonValues.EMOVOC_XLS_PATH);
		if (!dataFile.exists()) {
			dataFile.mkdir();
		}
		if (!resFile.exists()) {
			return false;
		}
		if (!svmModelFile.exists()) {
			return false;
		}
		if (!subModelFile.exists()) {
			return false;
		}
		if (!emoVocFile.exists()) {
			return false;
		}
		if (!CommonValues.LIBSVM_MODEL_MD5.equals(getMd5ByFile(svmModelFile))) {
			return false;
		}
		if (!CommonValues.SUBOBJ_MODEL_MD5.equals(getMd5ByFile(subModelFile))) {
			return false;
		}
		if (!CommonValues.EMOVOC_XLS_MD5.equals(getMd5ByFile(emoVocFile))) {
			return false;
		}
		return true;
	}
	
	public static String getMd5ByFile(File file) {
		String value = null;
		FileInputStream in=null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value.toUpperCase().trim();
	}
}
