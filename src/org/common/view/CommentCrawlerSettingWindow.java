package org.common.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.common.config.CommonValues;
import org.yuriak.beans.KeyWordsBean;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CommentCrawlerSettingWindow {

	private static CommentCrawlerSettingWindow window;
	private JFrame CommentCrawlerSettingFrame;
	private JButton OKButton = new JButton("OK");
	private JButton ExitButton = new JButton("Exit");
	private JTextField FilePathTextField;
	private JButton FilePathChooseButton = new JButton("Browse");
	private JLabel CommentNumberFigure = new JLabel("0");
	private JLabel NewsNumberFigure = new JLabel("0");
	private JSlider NewsNumberSlider = new JSlider();
	private JSlider CommentNumberSlider = new JSlider();
	private JTextField KeywordsTextField;
	private JList keywordsJlist = new JList();
	private DefaultListModel<String> keywordsModel;
	private JButton AddButton = new JButton("Add");
	private String[] keywords;
	private final JButton DeleteButton = new JButton("Del");
	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new CommentCrawlerSettingWindow(args);
					window.CommentCrawlerSettingFrame.setVisible(true);
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
			window.keywordsModel=new DefaultListModel<>();
			for (KeyWordsBean bean : CommonValues.CW_keywordsList) {
				window.keywordsModel.addElement(bean.getKeywords());
			}
			window.keywordsJlist.setModel(window.keywordsModel);
			window.CommentCrawlerSettingFrame.setVisible(true);
		}
	}
	
	public static void hideView(){
		if (window!=null) {
			window.CommentCrawlerSettingFrame.setVisible(false);
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
	public CommentCrawlerSettingWindow(String[] keywords) {
		initView();
		initAction();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initView(){
		CommentCrawlerSettingFrame = new JFrame();
		CommentCrawlerSettingFrame.setTitle("CommentCrawlerSeeting");
		
		CommentCrawlerSettingFrame.setResizable(false);
		CommentCrawlerSettingFrame.setBounds(100, 100, 450, 492);
		CommentCrawlerSettingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CommentCrawlerSettingFrame.getContentPane().setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane.setBounds(10, 10, 424, 366);
		CommentCrawlerSettingFrame.getContentPane().add(layeredPane);
		
		JLabel NewsNumberLabel = new JLabel("MaxNewsNumberPerKeyword");
		NewsNumberLabel.setBounds(10, 158, 182, 30);
		layeredPane.add(NewsNumberLabel);
		
		JLabel CommentNumberLabel = new JLabel("CommentNumberPerNews");
		CommentNumberLabel.setBounds(10, 198, 170, 30);
		layeredPane.add(CommentNumberLabel);
		
		
		
		NewsNumberSlider.setValue(20);
		NewsNumberSlider.setSnapToTicks(true);
		NewsNumberSlider.setMaximum(1000);
		NewsNumberSlider.setMajorTickSpacing(20);
		NewsNumberSlider.setMinimum(20);
		NewsNumberSlider.setBounds(190, 158, 170, 30);
		layeredPane.add(NewsNumberSlider);
		CommentNumberSlider.setSnapToTicks(true);
		
		
		
		CommentNumberSlider.setValue(100);
		CommentNumberSlider.setMajorTickSpacing(100);
		CommentNumberSlider.setMinimum(100);
		CommentNumberSlider.setMaximum(1000);
		CommentNumberSlider.setBounds(190, 198, 170, 30);
		layeredPane.add(CommentNumberSlider);
		
		JLabel CommentFileSaveLabel = new JLabel("CommentFileSavePath");
		CommentFileSaveLabel.setBounds(10, 238, 170, 30);
		layeredPane.add(CommentFileSaveLabel);
		
		FilePathTextField=new JTextField();
		FilePathTextField.setEditable(false);
		FilePathTextField.setBounds(167, 243, 223, 21);
		layeredPane.add(FilePathTextField);
		FilePathTextField.setColumns(10);
		
		
		
		FilePathChooseButton.setBounds(297, 274, 93, 23);
		layeredPane.add(FilePathChooseButton);
		
		
		NewsNumberFigure.setBounds(370, 158, 33, 30);
		layeredPane.add(NewsNumberFigure);
		
		
		CommentNumberFigure.setBounds(370, 198, 34, 30);
		layeredPane.add(CommentNumberFigure);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_1.setBounds(10, 386, 424, 68);
		CommentCrawlerSettingFrame.getContentPane().add(layeredPane_1);
		
		
		
		OKButton.setBounds(311, 10, 93, 48);
		layeredPane_1.add(OKButton);
		ExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		
		ExitButton.setBounds(209, 10, 93, 48);
		layeredPane_1.add(ExitButton);
		
		NewsNumberFigure.setText(NewsNumberSlider.getValue()+"");
		CommentNumberFigure.setText(CommentNumberSlider.getValue()+"");
		FilePathTextField.setText(System.getProperty("user.dir")+File.separator+"data");
		
		JLabel KeywordsLabel = new JLabel("Keywords");
		KeywordsLabel.setBounds(10, 36, 110, 30);
		layeredPane.add(KeywordsLabel);
		
		KeywordsTextField = new JTextField();
		KeywordsTextField.setBounds(167, 45, 93, 21);
		layeredPane.add(KeywordsTextField);
		KeywordsTextField.setColumns(10);
		
		keywordsModel=new DefaultListModel<String>();
		for (KeyWordsBean bean : CommonValues.CW_keywordsList) {
			keywordsModel.addElement(bean.getKeywords());
		}
		keywordsJlist.setModel(keywordsModel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(167, 77, 191, 71);
		layeredPane.add(scrollPane);
		keywordsJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scrollPane.setViewportView(keywordsJlist);
		AddButton.setBounds(270, 44, 61, 21);
		layeredPane.add(AddButton);
		
		DeleteButton.setBounds(341, 45, 61, 21);
		
		layeredPane.add(DeleteButton);
	}
	private void initAction() {
		NewsNumberSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				NewsNumberFigure.setText(NewsNumberSlider.getValue()+"");
			}
		});
		
		CommentNumberSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				CommentNumberFigure.setText(CommentNumberSlider.getValue()+"");
			}
		});
		
		
		FilePathChooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSystemView fsv = FileSystemView.getFileSystemView();
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i=fileChooser.showDialog(new JLabel(), "Choose save path");
                if(i==JFileChooser.APPROVE_OPTION)  //判断是否为打开的按钮
                {
                	File file=fileChooser.getSelectedFile();
    				FilePathTextField.setText(file.getAbsolutePath());  //取得选中的文件
                }
			}
		});
		
		AddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!KeywordsTextField.getText().trim().equals("")) {
					if (!keywordsModel.contains(new String(KeywordsTextField.getText().trim()))) {
						keywordsModel.addElement(KeywordsTextField.getText().trim());
					}
					KeywordsTextField.setText("");
				}
			}
		});
		
		DeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=keywordsJlist.getSelectedIndex();
				if (index!=-1) {
					keywordsModel.remove(index);
				}
			}
		});
		
		OKButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FilePathTextField.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(CommentCrawlerSettingFrame, "Please choose a place to save");
					return;
				}
				CommonValues.commentFilePath=FilePathTextField.getText()+File.separator+"comment.txt";
				CommonValues.newsNumberPerKeyword=NewsNumberSlider.getValue();
				CommonValues.commentNumberPerNews=CommentNumberSlider.getValue();
				String[] k=new String[keywordsModel.size()];
				ArrayList<KeyWordsBean> keyWordsBeans=new ArrayList<>();
				for (int i = 0; i < keywordsModel.size(); i++) {
					k[i]=keywordsModel.getElementAt(i);
					KeyWordsBean bean=new KeyWordsBean();
					bean.setKeywords(keywordsModel.getElementAt(i));
					keyWordsBeans.add(bean);
				}
				CommonValues.keywords=k;
				CommonValues.CW_keywordsList=keyWordsBeans;
				hideView();
				CommentCrawlerWindow.showView();
			}
		});
		
		CommentCrawlerSettingFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				CommentCrawlerWindow.showView();
			}
		});
	}
}
