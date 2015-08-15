package org.common.view;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileSystemView;

import org.common.config.CommonValues;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.io.File;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EventCrawlerSettingWindow {

	private static EventCrawlerSettingWindow window;
	private JFrame CrawlerSettingFrame;
	private JTextField WUsernameTextField;
	private JLabel WeiboUsernameLebel;
	private JLayeredPane layeredPane;
	private JLabel WeiboPasswordLabel;
	private JLabel TNumberLabel;
	private JLabel ClusterNumberLabel;
	private JLabel KeywordPerClusterLabel;
	private JLabel SentencePerClusterLabel;
	private JSlider TNumberSlider;
	private JSlider ClusterNumberSlider;
	private JSlider KeyWordPCluSlider;
	private JSlider SentencePClusterSlider;
	private JButton NextButton;
	private JButton ExitButton;
	private JLabel TNumberFigure = new JLabel("20");
	private JLabel ClusterNumberFigure = new JLabel("100");
	private JLabel KeywordPClusterFigure = new JLabel("10");
	private JLabel SentencePClusterFigure = new JLabel("3");
	private JLabel SentenceNumberLabel;
	private JSlider SentenceNumberSlider;
	private JLabel SentenceNumberFigure;
	private JPasswordField WPasswdTextField;
	private JTextField KeywordSavePathTextField;
	private JLabel MaterialFilepathLabel;
	private JButton KeywordFilePathChooseButton;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new EventCrawlerSettingWindow();
					window.CrawlerSettingFrame.setVisible(true);
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
			window.CrawlerSettingFrame.setVisible(true);
		}
	}
	
	public static void hideView(){
		if (window!=null) {
			window.CrawlerSettingFrame.setVisible(false);
		}
	}
	
	/**
	 * Create the application.
	 */
	public EventCrawlerSettingWindow() {
		initView();
		initialize();
	}

	
	private void initView(){
		WeiboUsernameLebel= new JLabel("WeiboUsername");
		layeredPane = new JLayeredPane();
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		WeiboPasswordLabel = new JLabel("WeiboPassword");
		TNumberLabel = new JLabel("CrawlerThreadNumber");
		ClusterNumberLabel = new JLabel("ClusterNumber");
		KeywordPerClusterLabel = new JLabel("KeywordPerCluster");
		SentencePerClusterLabel = new JLabel("SentencePerCluster");
		WUsernameTextField = new JTextField();
		TNumberSlider = new JSlider();
		
		ClusterNumberSlider = new JSlider();
		
		KeyWordPCluSlider = new JSlider();
		
		SentencePClusterSlider = new JSlider();
		
		NextButton = new JButton("Next");
		
		ExitButton = new JButton("Exit");
		
		CrawlerSettingFrame = new JFrame();
		
		CrawlerSettingFrame.setTitle("EventCrawlerSetting");
		CrawlerSettingFrame.setResizable(false);
		CrawlerSettingFrame.setBounds(100, 100, 450, 492);
		CrawlerSettingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CrawlerSettingFrame.getContentPane().setLayout(null);
		
		layeredPane.setBounds(10, 10, 424, 378);
		CrawlerSettingFrame.getContentPane().add(layeredPane);
		layeredPane.setLayout(null);
		
		
		WeiboUsernameLebel.setBounds(10, 30, 150, 20);
		layeredPane.add(WeiboUsernameLebel);
		
		WeiboPasswordLabel.setBounds(10, 70, 150, 20);
		layeredPane.add(WeiboPasswordLabel);
		
		TNumberLabel.setBounds(10, 114, 150, 20);
		layeredPane.add(TNumberLabel);
		
		ClusterNumberLabel.setBounds(10, 144, 150, 20);
		layeredPane.add(ClusterNumberLabel);
		
		KeywordPerClusterLabel.setBounds(10, 174, 150, 20);
		layeredPane.add(KeywordPerClusterLabel);
		
		SentencePerClusterLabel.setBounds(10, 204, 150, 20);
		layeredPane.add(SentencePerClusterLabel);
		
		
		WUsernameTextField.setBounds(160, 30, 150, 21);
		layeredPane.add(WUsernameTextField);
		WUsernameTextField.setColumns(10);
		
		TNumberSlider.setValue(20);
		TNumberSlider.setMinimum(1);
		TNumberSlider.setMaximum(20);
		TNumberSlider.setBounds(160, 114, 150, 20);
		layeredPane.add(TNumberSlider);
		
		ClusterNumberSlider.setValue(100);
		ClusterNumberSlider.setMinimum(1);
		ClusterNumberSlider.setBounds(160, 144, 150, 20);
		layeredPane.add(ClusterNumberSlider);
		
		KeyWordPCluSlider.setValue(10);
		KeyWordPCluSlider.setMinimum(1);
		KeyWordPCluSlider.setMaximum(10);
		KeyWordPCluSlider.setBounds(160, 174, 150, 20);
		layeredPane.add(KeyWordPCluSlider);
		
		SentencePClusterSlider.setValue(3);
		SentencePClusterSlider.setMinimum(2);
		SentencePClusterSlider.setMaximum(10);
		SentencePClusterSlider.setBounds(160, 204, 150, 20);
		layeredPane.add(SentencePClusterSlider);
		
		
		TNumberFigure.setBounds(320, 117, 50, 20);
		layeredPane.add(TNumberFigure);
		
		
		ClusterNumberFigure.setBounds(320, 144, 50, 20);
		layeredPane.add(ClusterNumberFigure);
		
		
		KeywordPClusterFigure.setBounds(320, 174, 50, 20);
		layeredPane.add(KeywordPClusterFigure);
		
		
		SentencePClusterFigure.setHorizontalAlignment(SwingConstants.LEFT);
		SentencePClusterFigure.setBounds(320, 204, 50, 20);
		layeredPane.add(SentencePClusterFigure);
		
		SentenceNumberLabel = new JLabel("SentenceNumber");
		SentenceNumberLabel.setBounds(10, 234, 150, 20);
		layeredPane.add(SentenceNumberLabel);
		
		SentenceNumberSlider = new JSlider();
		SentenceNumberSlider.setMaximum(1000);
		SentenceNumberSlider.setSnapToTicks(true);
		SentenceNumberSlider.setMajorTickSpacing(10);
		SentenceNumberSlider.setValue(200);
		SentenceNumberSlider.setMinimum(10);
		SentenceNumberSlider.setBounds(160, 234, 150, 20);
		layeredPane.add(SentenceNumberSlider);
		
		SentenceNumberFigure = new JLabel("200");
		SentenceNumberFigure.setHorizontalAlignment(SwingConstants.LEFT);
		SentenceNumberFigure.setBounds(320, 234, 50, 20);
		layeredPane.add(SentenceNumberFigure);
		
		WPasswdTextField = new JPasswordField();
		WPasswdTextField.setBounds(160, 70, 150, 21);
		layeredPane.add(WPasswdTextField);
		
		KeywordSavePathTextField = new JTextField();
		KeywordSavePathTextField.setEditable(false);
		KeywordSavePathTextField.setBounds(160, 264, 182, 21);
		layeredPane.add(KeywordSavePathTextField);
		KeywordSavePathTextField.setColumns(10);
		
		MaterialFilepathLabel = new JLabel("MaterialSavePath");
		MaterialFilepathLabel.setBounds(10, 264, 140, 20);
		layeredPane.add(MaterialFilepathLabel);
		
		KeywordFilePathChooseButton = new JButton("Browse");
		KeywordFilePathChooseButton.setBounds(249, 295, 93, 23);
		layeredPane.add(KeywordFilePathChooseButton);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_1.setBounds(10, 396, 424, 58);
		CrawlerSettingFrame.getContentPane().add(layeredPane_1);
		layeredPane_1.setLayout(null);
		
		NextButton.setBounds(334, 10, 80, 38);
		layeredPane_1.add(NextButton);
		
		
		ExitButton.setBounds(244, 10, 80, 38);
		layeredPane_1.add(ExitButton);
		
		KeywordSavePathTextField.setText(System.getProperty("user.dir")+File.separator+"data");
		WUsernameTextField.setText(CommonValues.weiboUsername);
		WPasswdTextField.setText(CommonValues.weiboPassword);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		TNumberSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				TNumberFigure.setText(TNumberSlider.getValue()+"");
			}
		});
		
		ClusterNumberSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ClusterNumberFigure.setText(ClusterNumberSlider.getValue()+"");
			}
		});
		
		KeyWordPCluSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				KeywordPClusterFigure.setText(KeyWordPCluSlider.getValue()+"");
			}
		});
		
		SentencePClusterSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SentencePClusterFigure.setText(SentencePClusterSlider.getValue()+"");
			}
		});
		
		NextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonValues.weiboUsername=WUsernameTextField.getText().trim();
				CommonValues.weiboPassword=new String(WPasswdTextField.getPassword()).trim();
				CommonValues.clusterNumber=ClusterNumberSlider.getValue();
				CommonValues.keywordNumberPerCluster=KeyWordPCluSlider.getValue();
				CommonValues.sentencePerCluster=SentencePClusterSlider.getValue();
				CommonValues.sentenceNumber=SentenceNumberSlider.getValue();
				CommonValues.materialFilePath=KeywordSavePathTextField.getText()+File.separator+"event.txt";
				EventCrawlerWindow.showView();
				hideView();
			}
		});
		
		ExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				MainWindow.showView();
//				hideView();
				System.exit(1);
			}
		});
		
		SentenceNumberSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SentenceNumberFigure.setText(SentenceNumberSlider.getValue()+"");
			}
		});
		
		KeywordFilePathChooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSystemView fsv = FileSystemView.getFileSystemView();
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i=fileChooser.showDialog(new JLabel(), "Choose save path");
                if(i==JFileChooser.APPROVE_OPTION)  //判断是否为打开的按钮
                {
                	File file=fileChooser.getSelectedFile();
    				KeywordSavePathTextField.setText(file.getAbsolutePath());  //取得选中的文件
                }
			}
		});
		
		CrawlerSettingFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				EventCrawlerWindow.showView();
			}
		});
	}
}
