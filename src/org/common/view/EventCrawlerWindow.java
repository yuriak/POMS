package org.common.view;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.common.bean.ClusterBean;
import org.common.config.CommonValues;
import org.haikism.textcluster.ToCluster;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;
import org.yuriak.beans.WeiboNewsBean;
import org.yuriak.crawler.WeiboNewsCrawler;
import org.yuriak.util.GUIPrintStream;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.MyLog;
import org.yuriak.util.TextAreaLogAppender;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileSystemView;

import java.awt.Font;

import javax.swing.UIManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EventCrawlerWindow {

	private int WindowStatus=-1;
	private static EventCrawlerWindow window;
	private JFrame KeywordCrawlerFrame;
	private JLayeredPane layeredPane = new JLayeredPane();
	private JLayeredPane layeredPane_2 = new JLayeredPane();
	private JLabel EventLabel = new JLabel("Material/Event:");
	private JList EventJlist = new JList();
	private JLabel DetailLabel = new JLabel("Detail:");
	private JLayeredPane layeredPane_1 = new JLayeredPane();
	private JLayeredPane layeredPane_4 = new JLayeredPane();
	private JButton CrawlerButton = new JButton("Crawl");
	private JButton ResetButton = new JButton("Setting");
	private JLabel ConsoleLabel = new JLabel("Console:");
	private JProgressBar StatusProgressBar = new JProgressBar();
	private JLabel StatusLabel = new JLabel("");
	private ArrayList<String> keywordList;
	private ArrayList<WeiboNewsBean> ori_newsList;
	private ArrayList<ClusterBean> clusterList;
	private Thread crawlerThread;
	private Thread crawlerUIThread;
	private Thread clusterThread;
	private Thread clusterUIThread;
	private WeiboNewsCrawler crawler;
	private ToCluster cluster;
	private JLabel logLabel;  
	private JScrollPane logScrollPane;  
	private JTextArea logTextArea = new JTextArea();
	private JButton ClusterButton = new JButton("Cluster");
    private  static Log log = LogFactory.getLog(EventCrawlerWindow.class);  
    private DefaultListModel<String> eventModel;
    private  JScrollPane scrollPane = new JScrollPane();
    private  JButton ImportButton = new JButton("Import");
    private File importedFile=new File(CommonValues.materialFilePath);
    private  JLabel keyWordsNumberLabel = new JLabel("KeywordsNumber");
    private  JLabel RelativeMaterialsNumberLabel = new JLabel("RelativeMaterials");
    private  JLabel KeywordsNumberFigure = new JLabel("");
    private  JLabel RelativeMaterialsNumberFigure = new JLabel("");
    private  JLabel SourceNumberFigure = new JLabel("");
    private  JButton CommentCrawlerButton = new JButton("CrawlComment");
    
    private JButton BackButton = new JButton("Exit");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window= new EventCrawlerWindow();
					window.KeywordCrawlerFrame.setVisible(true);
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
			System.setOut(new GUIPrintStream(System.out, window.logTextArea));
			window.KeywordCrawlerFrame.setVisible(true);
		}
	}
	
	public static void hideView(){
		if (window!=null) {
			window.KeywordCrawlerFrame.setVisible(false);
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
	
	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public EventCrawlerWindow() throws Exception {
		System.setOut(new GUIPrintStream(System.out, logTextArea));
		initView();
		initialize();
		MyLog.INFO("Welcome to use EventCrawler,Start Crawler or Set Parameters Please");
		MyLog.INFO("Event material will save at "+CommonValues.materialFilePath);
	}
	
	public void initView(){
		KeywordCrawlerFrame = new JFrame();
		KeywordCrawlerFrame.setTitle("EventCrawler");
		
		KeywordCrawlerFrame.setMinimumSize(new Dimension(1000, 600));
		KeywordCrawlerFrame.setMaximumSize(new Dimension(1000, 600));
		KeywordCrawlerFrame.setResizable(false);
		KeywordCrawlerFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		KeywordCrawlerFrame.setBounds(100, 100, 450, 300);
		KeywordCrawlerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		KeywordCrawlerFrame.getContentPane().setLayout(null);
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		layeredPane.setBounds(10, 10, 974, 311);
		KeywordCrawlerFrame.getContentPane().add(layeredPane);
		layeredPane.setLayout(null);
		layeredPane_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		layeredPane_2.setBounds(10, 10, 692, 294);
		layeredPane.add(layeredPane_2);
		layeredPane_2.setLayout(null);
		
		
		EventLabel.setBounds(10, 10, 106, 22);
		layeredPane_2.add(EventLabel);
		
		JLayeredPane layeredPane_3 = new JLayeredPane();
		layeredPane_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_3.setBounds(712, 10, 252, 294);
		layeredPane.add(layeredPane_3);
		layeredPane_3.setLayout(null);
		DetailLabel.setFont(UIManager.getFont("Button.font"));
		
		
		DetailLabel.setBounds(10, 10, 123, 35);
		layeredPane_3.add(DetailLabel);
		keyWordsNumberLabel.setBounds(20, 47, 123, 35);
		
		layeredPane_3.add(keyWordsNumberLabel);
		RelativeMaterialsNumberLabel.setBounds(20, 92, 123, 35);
		
		layeredPane_3.add(RelativeMaterialsNumberLabel);
		KeywordsNumberFigure.setBounds(153, 57, 54, 15);
		
		layeredPane_3.add(KeywordsNumberFigure);
		RelativeMaterialsNumberFigure.setBounds(153, 102, 54, 15);
		
		layeredPane_3.add(RelativeMaterialsNumberFigure);
		SourceNumberFigure.setBounds(153, 147, 54, 15);
		
		layeredPane_3.add(SourceNumberFigure);
		
		CommentCrawlerButton.setEnabled(false);
		CommentCrawlerButton.setBounds(10, 249, 197, 35);
		
		layeredPane_3.add(CommentCrawlerButton);
		layeredPane_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		layeredPane_1.setBounds(10, 331, 974, 224);
		KeywordCrawlerFrame.getContentPane().add(layeredPane_1);
		layeredPane_1.setLayout(null);
		ConsoleLabel.setBounds(20, 10, 77, 22);
		
		layeredPane_1.add(ConsoleLabel);
		
		logLabel = new JLabel();
		logLabel.setText(" ");  
		
		logScrollPane = new JScrollPane();
		logScrollPane.setBounds(20, 42, 944, 113);
		layeredPane_1.add(logScrollPane);
		logTextArea.setForeground(Color.GREEN);
		logTextArea.setBackground(Color.BLACK);
		logScrollPane.setViewportView(logTextArea);
		logTextArea.setEditable(false);
		
		
		StatusProgressBar.setBounds(10, 25, 924, 14);
		layeredPane_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_4.setBounds(20, 165, 944, 49);
		layeredPane_1.add(layeredPane_4);
		layeredPane_4.add(StatusProgressBar);
		
		
		StatusLabel.setBounds(10, 10, 166, 15);
		layeredPane_4.add(StatusLabel);
		scrollPane.setViewportView(EventJlist);
		EventJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		
		this.initLog();
		scrollPane.setBounds(10, 30, 672, 213);
		
		layeredPane_2.add(scrollPane);
		
		EventJlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				
				int index=EventJlist.getSelectedIndex();
				if (index!=-1) {
					if (clusterList!=null) {
						KeywordsNumberFigure.setText(clusterList.get(index).getKeywords().size()+"");
						RelativeMaterialsNumberFigure.setText(clusterList.get(index).getSentences().size()+"");
						CommentCrawlerButton.setEnabled(true);
					}
				}
			}
		});
		
		ResetButton.setBounds(124, 253, 106, 35);
		layeredPane_2.add(ResetButton);
		ImportButton.setBounds(240, 253, 106, 35);
		layeredPane_2.add(ImportButton);
		CrawlerButton.setBounds(460, 253, 106, 35);
		layeredPane_2.add(CrawlerButton);
		ClusterButton.setBounds(576, 253, 106, 35);
		layeredPane_2.add(ClusterButton);
		
		ClusterButton.setEnabled(false);
		
		
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideView();
				MainWindow.showView();
			}
		});
		BackButton.setBounds(10, 253, 106, 35);
		layeredPane_2.add(BackButton);
		ClusterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (WindowStatus==1) {
					new SwingWorker<Void, Void>(){
						@Override
						protected Void doInBackground() throws Exception {
							// TODO Auto-generated method stub
							clusterThread=new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									eventModel=new DefaultListModel<>();
									MyLog.INFO("Cluster start...");
									CrawlerButton.setEnabled(false);
									ResetButton.setEnabled(false);
									ClusterButton.setEnabled(false);
									ImportButton.setEnabled(false);
									CommentCrawlerButton.setEnabled(false);
									BackButton.setEnabled(false);
									String[] sentences=new String[ori_newsList.size()];
									for (int i = 0; i < ori_newsList.size(); i++) {
										sentences[i]=ori_newsList.get(i).getContent();
									}
									try {
										cluster=new ToCluster(sentences, CommonValues.clusterNumber, CommonValues.keywordNumberPerCluster, CommonValues.sentencePerCluster);
										clusterList=cluster.startCluster();
										MyLog.INFO(clusterList.size()+" event get");
											for (ClusterBean bean : clusterList) {
												String tmpWord="";
												for (String string : bean.getKeywords()) {
													tmpWord+=string+" ";
												}
												eventModel.addElement(tmpWord);
											}
										EventJlist.setModel(eventModel);
									} catch (Exception e2) {
										MyLog.INFO("cluster faild try again");
										e2.printStackTrace();
									}finally{
										MyLog.INFO("Cluster stop...");
										BackButton.setEnabled(true);
										CrawlerButton.setEnabled(true);
										ResetButton.setEnabled(true);
										ClusterButton.setEnabled(true);
										ImportButton.setEnabled(true);
										logScrollPane.getVerticalScrollBar().setValue(logScrollPane.getVerticalScrollBar().getMaximum());
										MyLog.INFO("Select a event and start analyze or redo previous action...");
									}
								}
							});
							clusterThread.start();
							return null;
						}
					}.execute();
				}
			}
		});
		
		CrawlerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (WindowStatus==-1||WindowStatus==1) {
					new SwingWorker<Void, Integer>(){
						@Override
						protected Void doInBackground() throws Exception {
							// TODO Auto-generated method stub
							crawler=new WeiboNewsCrawler(CommonValues.DATA_DIR);
							BackButton.setEnabled(false);
							CrawlerButton.setEnabled(false);
							ResetButton.setEnabled(false);
							ClusterButton.setEnabled(false);
							ImportButton.setEnabled(false);
							CommentCrawlerButton.setEnabled(false);
							
							eventModel=new DefaultListModel<String>();
							EventJlist.setModel(eventModel);
//							final boolean ccb=CommentCrawlerButton.isEnabled();
							CommentCrawlerButton.setEnabled(false);
							crawlerThread=new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										WindowStatus=0;
										ori_newsList=crawler.getNews(CommonValues.weiboUsername, CommonValues.weiboPassword, CommonValues.sentenceNumber, CommonValues.crawlerThreadNumber);
										eventModel=new DefaultListModel<>();
										for (WeiboNewsBean bean : ori_newsList) {
											eventModel.addElement(bean.getContent());
										}
										EventJlist.setModel(eventModel);
										WindowStatus=1;
										ClusterButton.setEnabled(true);
										MyLog.INFO("Crawled "+crawler.NewsNumber+" Materials,Start Cluseter Please");
										try {
											MyLog.INFO("saving event materials into "+CommonValues.materialFilePath);
											MyFileUtil.writeMaterialToFile(ori_newsList, new File(CommonValues.materialFilePath));
											MyLog.INFO("save successful");
											MyLog.INFO("Start clustering please");
										} catch (Exception e2) {
											// TODO: handle exception
											MyLog.INFO("saving materials faild");
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										WindowStatus=-1;
										MyLog.INFO(e.getMessage());
									}finally{
										BackButton.setEnabled(true);
										CrawlerButton.setEnabled(true);
										ResetButton.setEnabled(true);
										ImportButton.setEnabled(true);
										StatusProgressBar.setValue(100);
									}
								}
							});
							crawlerUIThread=new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										while (crawlerThread.isAlive()) {
											StatusLabel.setText("Crawling: "+(int)(((double)crawler.NewsNumber/(double)CommonValues.sentenceNumber)*100)+"%");
											StatusProgressBar.setValue((int)(((double)crawler.NewsNumber/(double)CommonValues.sentenceNumber)*100));
											logScrollPane.getVerticalScrollBar().setValue(logScrollPane.getVerticalScrollBar().getMaximum());
										}
										StatusProgressBar.setValue(100);
										StatusLabel.setText("Crawling: 100%");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
							crawlerThread.start();
							crawlerUIThread.start();
							return null;
						}
					}.execute();
				}
			}
		});
		
		ImportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSystemView fsv = FileSystemView.getFileSystemView();
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int i=fileChooser.showDialog(new JLabel(), "Choose material file"); 
				if (i==fileChooser.APPROVE_OPTION) {
					importedFile=fileChooser.getSelectedFile();
				}else {
					return;
				}
				MyLog.INFO("Importing material file: "+importedFile);
				new SwingWorker<Void, Void>(){
					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						Thread fileThread=new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									MyLog.INFO("reading material file...");
									ori_newsList=MyFileUtil.readMaterialFromFile(importedFile);
									eventModel=new DefaultListModel<>();
									for (WeiboNewsBean bean : ori_newsList) {
										eventModel.addElement(bean.getContent());
									}
									EventJlist.setModel(eventModel);
									CommonValues.materialFilePath=importedFile.getAbsolutePath();
									WindowStatus=1;
									ClusterButton.setEnabled(true);
									MyLog.INFO("reading successful");
									MyLog.INFO("read "+ori_newsList.size()+" materials");
									MyLog.INFO("Start clustering please");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									MyLog.INFO("file reading failed");
									e.printStackTrace();
								}
							}
						});
						fileThread.start();
						return null;
					}}.execute();
			}
		});
		ResetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (WindowStatus!=0) {
					EventCrawlerSettingWindow.showView();
					hideView();
				}
			}
		});
		
		CommentCrawlerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=EventJlist.getSelectedIndex();
				CommonValues.CW_keywordsList=new ArrayList<>();
				if (index!=-1) {
					for (int i = 0; i < clusterList.get(index).getKeywords().size(); i++) {
						KeyWordsBean bean=new KeyWordsBean();
						bean.setKeywords(clusterList.get(index).getKeywords().get(i));
						CommonValues.CW_keywordsList.add(bean);
					}
					CommentCrawlerWindow.showView();
					hideView();
				}
			}
		});
		
		KeywordCrawlerFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				MainWindow.showView();
			}
		});
	}
}
