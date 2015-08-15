package org.common.view;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.common.config.CommonValues;
import org.haikism.LIBSVM.AnalysisBaseOnLIBSVM;
import org.yuriak.beans.CommonCommentBean;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;
import org.yuriak.util.GUIPrintStream;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.MyLog;
import org.yuriak.util.TextAreaLogAppender;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnalyzerWindow {

	private static AnalyzerWindow window;
	private JFrame AnalyzerFrame;
	private JScrollPane logScrollPane = new JScrollPane();
	private JLabel ProgressLabel = new JLabel("");
	private JProgressBar progressBar = new JProgressBar();
	private JTextArea logTextArea = new JTextArea();
	private  JLayeredPane layeredPane_2 = new JLayeredPane();
	private  JLayeredPane layeredPane_3 = new JLayeredPane();
	private  JLabel label = new JLabel("Keywords:");
	private  JScrollPane scrollPane = new JScrollPane();
	private  JLayeredPane layeredPane_4 = new JLayeredPane();
	private  JLabel label_2 = new JLabel("News:");
	private  JScrollPane scrollPane_1 = new JScrollPane();
	private  JLayeredPane layeredPane_5 = new JLayeredPane();
	private  JLabel label_3 = new JLabel("Comments:");
	private  JScrollPane scrollPane_2 = new JScrollPane();
	private  JButton StartAnalyzeButton = new JButton("StartAnalyze");
	private  JLabel CommentDataFilePathLabel = new JLabel("CommentDataFilePath");
	private  JTextField CommentFilePathTextField = new JTextField();
	private  JButton CommentFileChooseButton = new JButton("Import");
	private int windowStatus;
	private static ArrayList<KeyWordsBean> keywordsList=new ArrayList<>();
	private ArrayList<KeyWordsBean> analyzedKeywordList;
	private File importedFile;
	private File outputFile;
	private  JButton AnalyzeFilePathChooserButton = new JButton("Browse");
	private  JTextField AnalyzeFileSavePathTextfield = new JTextField();
	private  JLabel AnalyzeFileSavePathLabel = new JLabel("AnalyzeFileSavePath");
	private Thread analysisThread;
	private JCheckBox UseSubObjAnaChkBX = new JCheckBox("UseSubObjAnalysis");
	private JList KeywordsJList = new JList();
	private JList NewsJList = new JList();
	private JList CommentsJList = new JList();
	private DefaultListModel<String> keywordModel;
	private DefaultListModel<String> newsModel;
	private DefaultListModel<String> commentModel;
	private JButton ShowReportButton = new JButton("ShowReport");
	private final JButton BackButton = new JButton("Exit");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new AnalyzerWindow();
					window.AnalyzerFrame.setVisible(true);
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
			window.AnalyzerFrame.setVisible(true);
			window.initBean();
		}
	}
	
	public static void hideView(){
		if (window!=null) {
			window.AnalyzerFrame.setVisible(false);
		}
	}
	
	public void closeWindow(){
		if (window!=null) {
			window=null;
		}
	}
	
	/**
	 * Create the application.
	 */
	
	private void initLog() {  
		
        try {  
            Thread t;  
            t = new TextAreaLogAppender(logTextArea, logScrollPane);
            t.start();  
        } catch (Exception e) {  
        }  
    }
	
	private void initBean(){
		keywordsList=CommonValues.AW_keywordList;
		if (CommonValues.AW_keywordList.size()>0) {
			windowStatus=-1;
			StartAnalyzeButton.setEnabled(true);
			MyLog.INFO("you have "+CommonValues.AW_keywordList.size()+" words to analyze,start analyze now");
		}else {
			MyLog.INFO("you have "+0+" words to analyze,start analyze now");
		}
	}
	
	
	private void initFilePath(){
		AnalyzeFileSavePathTextfield.setText(CommonValues.analyzeFilePath);
	}
	
	
	private void welcome(){
		MyLog.INFO("Welcome to use Analyzer,Start Analyze or Import Comment File");
		MyLog.INFO("Analyze tmp file will save at "+CommonValues.analyzeFilePath);
	}
	
	
	public AnalyzerWindow() {
		System.setOut(new GUIPrintStream(System.out, logTextArea));
		initView();
		initLog();
		welcome();
		initFilePath();
		initBean();
		initAction();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initView() {
		AnalyzerFrame = new JFrame();
		AnalyzerFrame.setTitle("Analyzer");
		
		AnalyzerFrame.setMinimumSize(new Dimension(1000, 600));
		AnalyzerFrame.setMaximumSize(new Dimension(1000, 600));
		AnalyzerFrame.setResizable(false);
		AnalyzerFrame.setBounds(100, 100, 450, 300);
		AnalyzerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AnalyzerFrame.getContentPane().setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane.setBounds(10, 343, 974, 219);
		AnalyzerFrame.getContentPane().add(layeredPane);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_1.setBounds(10, 160, 954, 49);
		layeredPane.add(layeredPane_1);
		
		
		progressBar.setBounds(10, 25, 934, 14);
		layeredPane_1.add(progressBar);
		
		
		ProgressLabel.setBounds(10, 10, 166, 15);
		layeredPane_1.add(ProgressLabel);
		
		JLabel label_1 = new JLabel("Console:");
		label_1.setBounds(10, 10, 77, 22);
		layeredPane.add(label_1);
		
		
		logScrollPane.setBounds(10, 42, 954, 111);
		layeredPane.add(logScrollPane);
		logTextArea.setBackground(Color.BLACK);
		logTextArea.setForeground(Color.GREEN);
		logTextArea.setEditable(false);
		
		logScrollPane.setViewportView(logTextArea);
		layeredPane_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_2.setBounds(10, 10, 974, 323);
		
		AnalyzerFrame.getContentPane().add(layeredPane_2);
		layeredPane_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_3.setBounds(10, 10, 146, 250);
		
		layeredPane_2.add(layeredPane_3);
		label.setBounds(10, 10, 54, 15);
		
		layeredPane_3.add(label);
		scrollPane.setBounds(10, 33, 126, 207);
		
		layeredPane_3.add(scrollPane);
		
		
		
		KeywordsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(KeywordsJList);
		layeredPane_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_4.setBounds(166, 10, 339, 250);
		
		layeredPane_2.add(layeredPane_4);
		label_2.setBounds(10, 10, 54, 15);
		
		layeredPane_4.add(label_2);
		scrollPane_1.setBounds(10, 35, 319, 205);
		
		layeredPane_4.add(scrollPane_1);
		
		
		
		NewsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(NewsJList);
		layeredPane_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_5.setBounds(515, 10, 449, 250);
		
		layeredPane_2.add(layeredPane_5);
		label_3.setBounds(10, 10, 76, 15);
		
		layeredPane_5.add(label_3);
		scrollPane_2.setBounds(10, 34, 429, 206);
		
		layeredPane_5.add(scrollPane_2);
		
		
		
		CommentsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(CommentsJList);
		
		StartAnalyzeButton.setEnabled(false);
		StartAnalyzeButton.setBounds(684, 270, 133, 35);
		
		layeredPane_2.add(StartAnalyzeButton);
		CommentDataFilePathLabel.setBounds(84, 270, 140, 20);
		
		layeredPane_2.add(CommentDataFilePathLabel);
		CommentFilePathTextField.setEditable(false);
		CommentFilePathTextField.setColumns(10);
		CommentFilePathTextField.setBounds(234, 270, 189, 21);
		
		layeredPane_2.add(CommentFilePathTextField);
		
		CommentFileChooseButton.setBounds(433, 270, 93, 23);
		
		layeredPane_2.add(CommentFileChooseButton);
		
		AnalyzeFilePathChooserButton.setBounds(433, 299, 93, 23);
		
		layeredPane_2.add(AnalyzeFilePathChooserButton);
		AnalyzeFileSavePathTextfield.setEditable(false);
		AnalyzeFileSavePathTextfield.setColumns(10);
		AnalyzeFileSavePathTextfield.setBounds(234, 300, 189, 21);
		
		layeredPane_2.add(AnalyzeFileSavePathTextfield);
		AnalyzeFileSavePathLabel.setBounds(84, 300, 140, 20);
		
		layeredPane_2.add(AnalyzeFileSavePathLabel);
		
		
		UseSubObjAnaChkBX.setBounds(532, 276, 146, 23);
		layeredPane_2.add(UseSubObjAnaChkBX);
		
		
		
		ShowReportButton.setEnabled(false);
		ShowReportButton.setBounds(827, 270, 133, 35);
		layeredPane_2.add(ShowReportButton);
		
		BackButton.setBounds(10, 270, 64, 43);
		
		layeredPane_2.add(BackButton);
	}
	
	private void initAction(){
		windowStatus=-2;
		CommentFileChooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileSystemView fsv = FileSystemView.getFileSystemView();
					JFileChooser fileChooser=new JFileChooser();
					fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int i=fileChooser.showDialog(new JLabel(), "Open Comment File");
	                if(i==JFileChooser.APPROVE_OPTION)  //判断是否为打开的按钮
	                {
	                	
	                	File file=fileChooser.getSelectedFile();
	    				CommentFilePathTextField.setText(file.getAbsolutePath());  //取得选中的文件
	    				importedFile=file;
	    				CommonValues.AW_keywordList=MyFileUtil.readCommentsFromFile(file);
	    				keywordsList=CommonValues.AW_keywordList;
	    				MyLog.INFO("importing "+file.getAbsolutePath());
	    				MyLog.INFO(CommonValues.AW_keywordList.size()+" keywords of comments imported suucessful..");
	    				StartAnalyzeButton.setEnabled(true);
	    				windowStatus=-1;
	                }else {
						return;
					}
				} catch (Exception e2) {
					MyLog.INFO("import failed..");
					e2.printStackTrace();
				}
			}
		});
		
		AnalyzeFilePathChooserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSystemView fsv = FileSystemView.getFileSystemView();
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i=fileChooser.showDialog(new JLabel(), "Choose save path");
                if(i==JFileChooser.APPROVE_OPTION)  //判断是否为打开的按钮
                {
                	File file=fileChooser.getSelectedFile();
    				AnalyzeFileSavePathTextfield.setText(file.getAbsolutePath()+File.separator+"analyze.txt");  //取得选中的文件
    				CommonValues.analyzeFilePath=file.getAbsolutePath()+File.separator+"analyze.txt";
    				MyLog.INFO("analyze file will save at "+CommonValues.analyzeFilePath);
                }else {
					return;
				}
			}
		});
		
		StartAnalyzeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				analyzedKeywordList=CommonValues.AW_keywordList;
				if (analyzedKeywordList.size()<=0) {
					MyLog.INFO("Your comment file is empty..");
					return;
				}
				new SwingWorker<Void, Void>(){
					@Override
					protected Void doInBackground() throws Exception {
						
						analysisThread=new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									AnalyzeFilePathChooserButton.setEnabled(false);
									CommentFileChooseButton.setEnabled(false);
									StartAnalyzeButton.setEnabled(false);
									UseSubObjAnaChkBX.setEnabled(false);
									ShowReportButton.setEnabled(false);
									BackButton.setEnabled(false);
									windowStatus=0;
									keywordModel=new DefaultListModel<>();
									MyLog.INFO("analyzing start....");
									for (int i = 0; i < analyzedKeywordList.size(); i++) {
										MyLog.INFO("analyzing keyword "+analyzedKeywordList.get(i).getKeywords());
										double avgKvalue=0;
										if (analyzedKeywordList.get(i).getNews().size()>0) {
											for (int j = 0; j < analyzedKeywordList.get(i).getNews().size(); j++) {
												ProgressLabel.setText("analyzing news "+(j+1)+"/"+analyzedKeywordList.get(i).getNews().size());
												progressBar.setValue((int)((double)j/(double)analyzedKeywordList.get(i).getNews().size()*100));
												double avgNValue=0;
												if (analyzedKeywordList.get(i).getNews().get(j).getCommonList()!=null&&analyzedKeywordList.get(i).getNews().get(j).getCommonList().size()>0) {
													MyLog.INFO("analyzing "+analyzedKeywordList.get(i).getNews().get(j).getCommonList().size()+" comments");
													String[] comments=new String[analyzedKeywordList.get(i).getNews().get(j).getCommonList().size()];
													for (int k = 0; k < comments.length; k++) {
														comments[k]=analyzedKeywordList.get(i).getNews().get(j).getCommonList().get(k).getContent();
													}
													AnalysisBaseOnLIBSVM analyzer=new AnalysisBaseOnLIBSVM(comments, "data/tmp_analyze.txt", UseSubObjAnaChkBX.isSelected()?true:false);
													analyzer.predict();
													double[] values=MyFileUtil.readTmpAnalyzeValue(new File("data/tmp_analyze.txt"));
													for (int k = 0; k < values.length; k++) {
														analyzedKeywordList.get(i).getNews().get(j).getCommonList().get(k).setEmotion(values[k]);
														avgNValue+=values[k];
													}
													avgNValue=avgNValue/values.length;
												}
												analyzedKeywordList.get(i).getNews().get(j).setEmotion(avgNValue);
												avgKvalue+=avgNValue;
											}
											avgKvalue=avgKvalue/analyzedKeywordList.get(i).getNews().size();
										}
										analyzedKeywordList.get(i).setEmotion(avgKvalue);
										keywordModel.addElement(analyzedKeywordList.get(i).getKeywords()+" |emo: "+analyzedKeywordList.get(i).getEmotion());
									}
									CommonValues.analyzedKeywordList=analyzedKeywordList;
									MyLog.INFO("writing analyze file to "+CommonValues.analyzeFilePath);
									MyFileUtil.writeAnalyzeToFile(CommonValues.analyzedKeywordList,new File(CommonValues.analyzeFilePath));
									MyLog.INFO("writing successful");
									progressBar.setValue(100);
									ProgressLabel.setText("done");
									NewsJList.setModel(new DefaultListModel<>());
									CommentsJList.setModel(new DefaultListModel<>());
									KeywordsJList.setModel(keywordModel);
									AnalyzeFilePathChooserButton.setEnabled(true);
									CommentFileChooseButton.setEnabled(true);
									StartAnalyzeButton.setEnabled(true);
									UseSubObjAnaChkBX.setEnabled(true);
									ShowReportButton.setEnabled(true);
									BackButton.setEnabled(true);
									windowStatus=1;
								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
						});
						analysisThread.start();
						return null;
					}}.execute();
			}
		});
		
		KeywordsJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				newsModel=new DefaultListModel<>();
				int index=KeywordsJList.getSelectedIndex();
				if (index!=-1) {
					for (CommonNewsBean newsBean : CommonValues.analyzedKeywordList.get(index).getNews()) {
						newsModel.addElement(newsBean.getTitle()+" |emo: "+newsBean.getEmotion());
					}
				}
				NewsJList.setModel(newsModel);
			}
		});
		NewsJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				commentModel=new DefaultListModel<>();
				int index=NewsJList.getSelectedIndex();
				if (index!=-1) {
					for (CommonCommentBean commentBean : CommonValues.analyzedKeywordList.get(KeywordsJList.getSelectedIndex()).getNews().get(index).getCommonList()) {
						commentModel.addElement(commentBean.getContent()+" |emo: "+(commentBean.getEmotion()>0?"positive":"negative"));
					}
				}
				CommentsJList.setModel(commentModel);
			}
		});
		
		ShowReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideView();
				ReporterWindow.showView();
			}
		});
		
		AnalyzerFrame.addWindowListener(new WindowAdapter() {
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
