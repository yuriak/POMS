package org.common.view;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.common.config.CommonValues;
import org.yuriak.beans.CommonCommentBean;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;
import org.yuriak.beans.SinaCommentBean;
import org.yuriak.beans.SinaNewsBean;
import org.yuriak.beans.TencentCommentBean;
import org.yuriak.beans.TencentNewsBean;
import org.yuriak.crawler.SinaCommentCrawler;
import org.yuriak.crawler.SinaNewsCrawler;
import org.yuriak.crawler.TencentCommentCrawler;
import org.yuriak.crawler.TencentNewsCrawler;
import org.yuriak.util.GUIPrintStream;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.MyLog;
import org.yuriak.util.TextAreaLogAppender;

import cn.edu.hfut.dmic.webcollector.util.FileUtils;

import javax.swing.JButton;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ListSelectionModel;

public class CommentCrawlerWindow {

	private JFrame CommentCrawlerFrame;
	private static CommentCrawlerWindow window;
	private JLayeredPane layeredPane = new JLayeredPane();
	private JTextArea logTextArea = new JTextArea();
	private JScrollPane logScrollPane = new JScrollPane();
	private JProgressBar progressBar = new JProgressBar();
	private JLabel ProgressLabel = new JLabel("");
	private JList commentJlist = new JList();
	private DefaultListModel<String> commentModel;
	private JList keywordsJlist = new JList();
	private DefaultListModel<String> keywordsModel;
	private JList newsJlist = new JList();
	private DefaultListModel<String> newsModel;
	private JButton SettingButton = new JButton("Setting");
	private JButton CrawlButton = new JButton("Crawl");
	private JButton OpenAnalyzeWindowButton = new JButton("OpenAnalyzeWindow");
	private JButton ImportButton = new JButton("Import");
	private int windowStatus;
	private ArrayList<KeyWordsBean> keywordsList=new ArrayList<>();
	private String[] kws={};
	private Thread CrawlerThread;
	private Thread CrawlerUiThread;
	private File importedFile;
	private int commentNumber;
	private final JButton BackButton = new JButton("Exit");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new CommentCrawlerWindow();
					window.CommentCrawlerFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public static void showView(){
		if (window==null) {
			main(null);
		}else {
			window.CommentCrawlerFrame.setVisible(true);
			try {
				System.setOut(new GUIPrintStream(System.out, window.logTextArea));
				if (CommonValues.CW_keywordsList!=null&&window.keywordsList!=null) {
					if (CommonValues.CW_keywordsList!=window.keywordsList) {
						window.OpenAnalyzeWindowButton.setEnabled(false);
						window.initKeywordList();
					}
				}
				window.welcome();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public static void hideView(){
		if (window!=null) {
			window.keywordsList=CommonValues.CW_keywordsList;
			window.CommentCrawlerFrame.setVisible(false);
		}
	}
	
	public void closeWindow(){
		if (window!=null) {
			window=null;
		}
	}
	
	private void initLog() {  
        try {  
            Thread t;  
            t = new TextAreaLogAppender(logTextArea, logScrollPane);
            t.start();  
        } catch (Exception e) {  
        }  
    }
	
	private void welcome(){
		MyLog.INFO("Welcome to use CommentCrawler,Start Crawler or Set Parameters Please");
		MyLog.INFO("comment material will save at "+CommonValues.commentFilePath);
		MyLog.INFO("you have "+CommonValues.CW_keywordsList.size()+" keywords to crawl now");
	}
	private void initKeywordList(){
		keywordsModel=new DefaultListModel<>();
		newsModel=new DefaultListModel<>();
		commentModel=new DefaultListModel<>();
		newsJlist.setModel(newsModel);
		commentJlist.setModel(commentModel);
		keywordsJlist.setModel(keywordsModel);
		for (int i = 0; i < CommonValues.CW_keywordsList.size(); i++) {
			keywordsModel.addElement(CommonValues.CW_keywordsList.get(i).getKeywords());
		}
	}
	private void initThread(){
		
	}
	/**
	 * Create the application.
	 */
	public CommentCrawlerWindow() {
		System.setOut(new GUIPrintStream(System.out, logTextArea));
		initView();
		initAction();
		initLog();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initView() {
		CommentCrawlerFrame = new JFrame();
		
		CommentCrawlerFrame.setResizable(false);
		CommentCrawlerFrame.setTitle("CommentCrawler");
		CommentCrawlerFrame.setMinimumSize(new Dimension(1000, 600));
		CommentCrawlerFrame.setMaximumSize(new Dimension(1000, 600));
		CommentCrawlerFrame.setBounds(100, 100, 450, 300);
		CommentCrawlerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CommentCrawlerFrame.getContentPane().setLayout(null);
		
		
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane.setBounds(10, 10, 974, 311);
		CommentCrawlerFrame.getContentPane().add(layeredPane);
		
		JLayeredPane layeredPane_3 = new JLayeredPane();
		layeredPane_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_3.setBounds(10, 10, 146, 250);
		layeredPane.add(layeredPane_3);
		
		JLabel KeywordsLabel = new JLabel("Keywords:");
		KeywordsLabel.setBounds(10, 10, 54, 15);
		layeredPane_3.add(KeywordsLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 33, 126, 207);
		layeredPane_3.add(scrollPane);
		keywordsJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		scrollPane.setViewportView(keywordsJlist);
		
		JLayeredPane layeredPane_4 = new JLayeredPane();
		layeredPane_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_4.setBounds(166, 10, 339, 250);
		layeredPane.add(layeredPane_4);
		
		JLabel NewsLabel = new JLabel("News:");
		NewsLabel.setBounds(10, 10, 54, 15);
		layeredPane_4.add(NewsLabel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 35, 319, 205);
		layeredPane_4.add(scrollPane_1);
		newsJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		
		scrollPane_1.setViewportView(newsJlist);
		
		JLayeredPane layeredPane_5 = new JLayeredPane();
		layeredPane_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_5.setBounds(515, 10, 449, 250);
		layeredPane.add(layeredPane_5);
		
		JLabel CommentsLabel = new JLabel("Comments:");
		CommentsLabel.setBounds(10, 10, 76, 15);
		layeredPane_5.add(CommentsLabel);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 34, 429, 206);
		layeredPane_5.add(scrollPane_2);
		commentJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(commentJlist);
		
		
		
		ImportButton.setBounds(126, 266, 106, 35);
		layeredPane.add(ImportButton);
		
		
		
		SettingButton.setBounds(242, 266, 106, 35);
		layeredPane.add(SettingButton);
		
		
		
		CrawlButton.setBounds(358, 266, 106, 35);
		layeredPane.add(CrawlButton);
		
		OpenAnalyzeWindowButton.setEnabled(false);
		
		
		OpenAnalyzeWindowButton.setBounds(776, 266, 188, 35);
		layeredPane.add(OpenAnalyzeWindowButton);
		
		BackButton.setBounds(10, 266, 106, 35);
		
		layeredPane.add(BackButton);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_1.setBounds(10, 333, 974, 219);
		CommentCrawlerFrame.getContentPane().add(layeredPane_1);
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_2.setBounds(10, 160, 954, 49);
		layeredPane_1.add(layeredPane_2);
		
		
		progressBar.setBounds(10, 25, 934, 14);
		layeredPane_2.add(progressBar);
		
		
		ProgressLabel.setBounds(10, 10, 166, 15);
		layeredPane_2.add(ProgressLabel);
		
		JLabel ConsoleLabel = new JLabel("Console:");
		ConsoleLabel.setBounds(10, 10, 77, 22);
		layeredPane_1.add(ConsoleLabel);
		
		
		logScrollPane.setBounds(10, 42, 954, 111);
		layeredPane_1.add(logScrollPane);
		logTextArea.setBackground(Color.BLACK);
		logTextArea.setForeground(Color.GREEN);
		logScrollPane.setViewportView(logTextArea);
		
		
		logTextArea.setEditable(false);
	}
	
	private void initAction(){
		windowStatus=-1;
		initLog();
		
		keywordsList=new ArrayList<>();
		if (CommonValues.CW_keywordsList!=null) {
			if (CommonValues.CW_keywordsList!=keywordsList) {
				OpenAnalyzeWindowButton.setEnabled(false);
				initKeywordList();
				welcome();
			}
		}
		SettingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideView();
				CommentCrawlerSettingWindow.showView();
			}
		});
		
		CrawlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						CrawlerThread=new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									if (CommonValues.CW_keywordsList.size()==0) {
										MyLog.INFO("set keywords first");
										return;
									}
									ImportButton.setEnabled(false);
									SettingButton.setEnabled(false);
									CrawlButton.setEnabled(false);
									BackButton.setEnabled(false);
									keywordsJlist.setModel(new DefaultListModel<>());
									newsJlist.setModel(new DefaultListModel<>());
									commentJlist.setModel(new DefaultListModel<>());
									OpenAnalyzeWindowButton.setEnabled(false);
									windowStatus=0;
									commentNumber=0;
									for (KeyWordsBean keyWord : CommonValues.CW_keywordsList) {
										keyWord.setEmotion(0);
										ProgressLabel.setText("crawling "+keyWord.getKeywords()+"...");
										progressBar.setValue(0);
										ArrayList<SinaNewsBean> sinaNewsList=new ArrayList<>();
										ArrayList<TencentNewsBean> tencentNewsList=new ArrayList<>();
										ArrayList<CommonNewsBean> commonNewsBeans=new ArrayList<>();
										SinaNewsCrawler sinaNewsCrawler=new SinaNewsCrawler(CommonValues.DATA_DIR);
										TencentNewsCrawler tencentNewsCrawler=new TencentNewsCrawler(CommonValues.DATA_DIR);
										sinaNewsList=sinaNewsCrawler.getNews(keyWord.getKeywords(), CommonValues.newsNumberPerKeyword, CommonValues.crawlerThreadNumber);
										progressBar.setValue(50);
										tencentNewsList=tencentNewsCrawler.getNews(keyWord.getKeywords(), CommonValues.newsNumberPerKeyword, CommonValues.crawlerThreadNumber);
										progressBar.setValue(100);
										int maxNumber=sinaNewsList.size()+tencentNewsList.size();
										if (sinaNewsList!=null&&sinaNewsList.size()>0) {
											for (int i=0;i<sinaNewsList.size();i++) {
												ArrayList<SinaCommentBean> sinaCommentList=new ArrayList<>();
												ArrayList<CommonCommentBean> commonCommentList=new ArrayList<>();
												SinaCommentCrawler sinaCommentCrawler=new SinaCommentCrawler(CommonValues.DATA_DIR);
												sinaCommentList=sinaCommentCrawler.getComment(sinaNewsList.get(i).getChannel(), sinaNewsList.get(i).getCommentId(), CommonValues.commentNumberPerNews, CommonValues.crawlerThreadNumber);
												commentNumber+=sinaCommentList.size();
												commonCommentList.addAll(sinaCommentList);
												sinaNewsList.get(i).setCommonList(commonCommentList);
												progressBar.setValue((int)(((double)i/(double)maxNumber)*100));
											}
										}else {
											sinaNewsList=new ArrayList<>();
										}
										commonNewsBeans.addAll(sinaNewsList);
										if (tencentNewsList!=null&&tencentNewsList.size()>0) {
											for (int i=0;i<tencentNewsList.size();i++) {
												ArrayList<TencentCommentBean> tencentCommentList=new ArrayList<>();
												ArrayList<CommonCommentBean> commonCommentList=new ArrayList<>();
												TencentCommentCrawler tencentCommentCrawler=new TencentCommentCrawler(CommonValues.DATA_DIR);
												tencentCommentList=tencentCommentCrawler.getComment(tencentNewsList.get(i).getArticalId(), CommonValues.commentNumberPerNews, CommonValues.crawlerThreadNumber);
												commentNumber+=tencentCommentList.size();
												commonCommentList.addAll(tencentCommentList);
												tencentNewsList.get(i).setCommonList(commonCommentList);
												progressBar.setValue((int)(((double)(i+sinaNewsList.size())/(double)maxNumber)*100));
											}
										}else {
											tencentNewsList=new ArrayList<>();
										}
										progressBar.setValue(100);
										commonNewsBeans.addAll(tencentNewsList);
										keyWord.setNews(commonNewsBeans);
									}
									MyLog.INFO("crawling done");
									MyLog.INFO("crawled "+commentNumber+" comments");
									MyLog.INFO("writing comments to "+CommonValues.commentFilePath);
									MyFileUtil.writeCommentsToFile(CommonValues.CW_keywordsList, new File(CommonValues.commentFilePath));
									MyLog.INFO("writing done");
									ImportButton.setEnabled(true);
									SettingButton.setEnabled(true);
									CrawlButton.setEnabled(true);
									BackButton.setEnabled(true);
									OpenAnalyzeWindowButton.setEnabled(true);
									windowStatus=1;
									keywordsList=CommonValues.CW_keywordsList;
									initKeywordList();
								} catch (Exception e2) {
//									MyLog.INFO(e2.getMessage());
									e2.printStackTrace();
								}
							}
						});
						CrawlerUiThread=new Thread(new Runnable() {
							@Override
							public void run() {
								while (CrawlerThread.isAlive()) {
									
								}
							}
						});
						CrawlerThread.start();
						CrawlerUiThread.start();
						return null;
					}
				}.execute();
			}
		});
		
		ImportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileSystemView fsv = FileSystemView.getFileSystemView();
					JFileChooser fileChooser=new JFileChooser();
					fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int i=fileChooser.showDialog(new JLabel(), "Choose comment file"); 
					if (i==fileChooser.APPROVE_OPTION) {
						importedFile=fileChooser.getSelectedFile();
					}else {
						return;
					}
					MyLog.INFO("importing file "+importedFile.getAbsolutePath());
					CommonValues.CW_keywordsList=MyFileUtil.readCommentsFromFile(importedFile);
					String[] kes=new String[CommonValues.CW_keywordsList.size()];
					int tmpCommentsNumber=0;
					for (int j = 0; j < CommonValues.CW_keywordsList.size(); j++) {
						kes[j]=CommonValues.CW_keywordsList.get(j).getKeywords();
						for (CommonNewsBean bean : CommonValues.CW_keywordsList.get(j).getNews()) {
							tmpCommentsNumber+=bean.getCommonList().size();
						}
					}
					commentNumber=tmpCommentsNumber;
					CommonValues.keywords=kes;
					MyLog.INFO("importing done");
					MyLog.INFO("imported file has "+kes.length+" keywords and "+commentNumber+" comment");
					windowStatus=1;
					initKeywordList();
					OpenAnalyzeWindowButton.setEnabled(true);
					keywordsList=CommonValues.CW_keywordsList;
				} catch (Exception e2) {
					MyLog.INFO(e2.getMessage());
					e2.printStackTrace();
				}
			}
		});
		
		keywordsJlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (keywordsJlist.getSelectedIndex()!=-1) {
					newsModel=new DefaultListModel<>();
					if (CommonValues.CW_keywordsList.get(keywordsJlist.getSelectedIndex()).getNews()!=null) {
						for (CommonNewsBean bean : CommonValues.CW_keywordsList.get(keywordsJlist.getSelectedIndex()).getNews()) {
							if (bean!=null&&bean.getTitle()!=null&&bean.getCommonList()!=null) {
								newsModel.addElement(bean.getTitle()+" commentNumber: "+bean.getCommonList().size());
							}else {
								newsModel.addElement("");
							}
						}
					}
					newsJlist.setModel(newsModel);
				}
			}
		});
		
		newsJlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (keywordsJlist.getSelectedIndex()!=-1) {
					if (newsJlist.getSelectedIndex()!=-1) {
						commentModel=new DefaultListModel<>();
						if (CommonValues.CW_keywordsList.get(keywordsJlist.getSelectedIndex())!=null) {
							if (CommonValues.CW_keywordsList.get(keywordsJlist.getSelectedIndex()).getNews().get(newsJlist.getSelectedIndex())!=null) {
								for (CommonCommentBean bean : CommonValues.CW_keywordsList.get(keywordsJlist.getSelectedIndex()).getNews().get(newsJlist.getSelectedIndex()).getCommonList()) {
									commentModel.addElement(bean.getContent());
								}
							}
						}
						commentJlist.setModel(commentModel);
					}
				}
			}
		});
		
		OpenAnalyzeWindowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonValues.AW_keywordList=(ArrayList<KeyWordsBean>) CommonValues.CW_keywordsList.clone();
				AnalyzerWindow.showView();
				hideView();
			}
		});
		
		CommentCrawlerFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				MainWindow.showView();
			}
		});
		
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideView();
				MainWindow.showView();
			}
		});
	}
	
}
