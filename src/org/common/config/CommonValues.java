package org.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.common.bean.ClusterBean;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;

public class CommonValues {
	
	public static final String BASE_DIR=System.getProperty("user.dir");
	public static final String DATA_DIR=BASE_DIR+File.separator+"data";
	public static final String RES_DIR=BASE_DIR+File.separator+"res";
	public static final String LIBSVM_MODEL_PATH=RES_DIR+File.separator+"LIBSVM.model";
	public static final String SUBOBJ_MODEL_PATH=RES_DIR+File.separator+"model_SubObj.txt";
	public static final String EMOVOC_XLS_PATH=RES_DIR+File.separator+"Emotion_Vocabulary.xls";
	public static final String LIBSVM_MODEL_MD5="2A1604F9D1507769E89A08C425859FD1";
	public static final String SUBOBJ_MODEL_MD5="9108604940CD12FC53179FB01F955AA7";
	public static final String EMOVOC_XLS_MD5="168E4A1E7B6270110CB38B5988AA2236";
	
	public static String weiboUsername="yurix@sohu.com";
	public static String weiboPassword="yurix301604";
	public static int crawlerThreadNumber=10;
	public static int clusterNumber=100;
	public static int keywordNumberPerCluster=10;
	public static int sentencePerCluster=3;
	public static int sentenceNumber=200;
	public static ArrayList<ClusterBean> clusterList=new ArrayList<>();
	public static ArrayList<CommonNewsBean> newsList=new ArrayList<>();
	public static int newsNumberPerKeyword=20;
	public static int commentNumberPerNews=100;
	public static String materialFilePath=DATA_DIR+File.separator+"event.txt";
	public static String commentFilePath=DATA_DIR+File.separator+"comment.txt";
	public static String[] keywords={};
	public static ArrayList<KeyWordsBean> CW_keywordsList=new ArrayList<>();
	public static ArrayList<KeyWordsBean> AW_keywordList=new ArrayList<>();
	public static ArrayList<KeyWordsBean> analyzedKeywordList=new ArrayList<>();
	public static String analyzeFilePath=DATA_DIR+File.separator+"analyze.txt";
	
}
