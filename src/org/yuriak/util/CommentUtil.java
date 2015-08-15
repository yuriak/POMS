package org.yuriak.util;

import java.io.File;
import java.util.ArrayList;

import org.yuriak.beans.CommonCommentBean;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;


import cn.edu.hfut.dmic.webcollector.util.FileUtils;

public class CommentUtil {
	public static void main(String[] args) throws Exception {
		ArrayList<KeyWordsBean> wordsBeans=MyFileUtil.readCommentsFromFile(new File("data/comment.txt"));
		StringBuilder comments=new StringBuilder();
		for (KeyWordsBean keyWordsBean : wordsBeans) {
			for (CommonNewsBean newsBean : keyWordsBean.getNews()) {
				for (CommonCommentBean commentBean : newsBean.getCommonList()) {
					comments.append(commentBean.getContent()+"\r\n");
				}
			}
		}
		FileUtils.writeFile(new File("data/ProcessComments"), comments.toString(), "utf-8");
	}
}
