package org.common.view;

import java.awt.EventQueue;
import java.awt.Label;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JButton;

import org.common.config.CommonValues;
import org.yuriak.util.MyFileUtil;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class MainWindow {

	private static MainWindow window;
	private JFrame MainFrame;
	private JMenuItem ShowReportWindowItem = new JMenuItem("OpenReporter");
	private JMenu ReportMenu = new JMenu("Report");
	private JMenu StartMenu = new JMenu("Start");
	private JMenuBar MainMenuBar = new JMenuBar();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MainWindow();
					window.MainFrame.setVisible(true);
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
			window.MainFrame.setVisible(true);
		}
		
	}
	public static void hideView(){
		if (window!=null) {
			window.MainFrame.setVisible(false);
		}
	}
	
	private void initView(){
		MainFrame = new JFrame();
		MainFrame.setTitle("POMS");
		MainFrame.setMinimumSize(new Dimension(200, 200));
		MainFrame.setMaximumSize(new Dimension(200, 200));
		MainFrame.setBounds(100, 100, 450, 300);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.getContentPane().setLayout(null);
		
		JLabel AnonmyousLabel = new JLabel("Anonymous[0]");
		AnonmyousLabel.setFont(new Font("Matura MT Script Capitals", Font.PLAIN, 25));
		AnonmyousLabel.setBounds(253, 177, 171, 54);
		MainFrame.getContentPane().add(AnonmyousLabel);
		
		JLabel POMSLabel = new JLabel("Public Opinion Monitoring System");
		POMSLabel.setFont(new Font("Algerian", Font.PLAIN, 23));
		POMSLabel.setBounds(19, 25, 396, 115);
		MainFrame.getContentPane().add(POMSLabel);
		
		JLabel versionLabel = new JLabel("V1.0");
		versionLabel.setFont(new Font("Algerian", Font.PLAIN, 23));
		versionLabel.setBounds(349, 85, 57, 82);
		MainFrame.getContentPane().add(versionLabel);
		MainFrame.setJMenuBar(MainMenuBar);
		MainMenuBar.add(StartMenu);
		
		JMenuItem EventCrawler = new JMenuItem("EventCrawler");
		EventCrawler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideView();
				EventCrawlerWindow.showView();
			}
		});
		StartMenu.add(EventCrawler);
		
		JMenuItem CommentCrawler = new JMenuItem("CommentCrawler");
		CommentCrawler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideView();
				CommentCrawlerWindow.showView();
			}
		});
		StartMenu.add(CommentCrawler);
		
		JMenuItem Analyzer = new JMenuItem("Analyzer");
		Analyzer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnalyzerWindow.showView();
				hideView();
			}
		});
		StartMenu.add(Analyzer);
		MainMenuBar.add(ReportMenu);
		ShowReportWindowItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReporterWindow.showView();
				hideView();
			}
		});
		ReportMenu.add(ShowReportWindowItem);
	}
	/**
	 * Create the application.
	 */
	public MainWindow() {
		initView();
		initAction();
		if (!MyFileUtil.checkFile()) {
			JOptionPane.showMessageDialog(MainFrame, "File is missing", "POMS", JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initAction() {
	}
}
