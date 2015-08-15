package org.common.view;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JList;

import org.common.config.CommonValues;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.demo.BarChartDemo1;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.chart.labels.*;

import org.yuriak.beans.CommonCommentBean;
import org.yuriak.beans.CommonNewsBean;
import org.yuriak.beans.KeyWordsBean;
import org.yuriak.beans.ReportBean;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.MyLog;
import org.yuriak.util.TimeUtil;

import com.orsoncharts.plot.CategoryPlot3D;

import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ReporterWindow {

	public static int opened=0;
	private static ReporterWindow window;
	private JFrame ReportFrame;
	private JTextField AnalyzeFilePathTextField;
	private static JList KeywordsJList = new JList();
	private JList NewsJList = new JList();
	private JList CommentsJList = new JList();
	private static JButton ShowTimeTrendButton = new JButton("TimeTrendChart");
	private  JButton AnalyzeFileImportButton = new JButton("Import");
	private static JButton ShowPieChartButton = new JButton("PropotionChart");
	private JLabel StatusText = new JLabel("");
	private JButton showKeywordsPropotionButton = new JButton("KeywordPropotionChart");
	private JButton showEmotionValueChart = new JButton("EmotionValueChart");
	private File importedFile;
	private static int windowStatus;
	
	private static ArrayList<ReportBean> reportList;
	
	private static DefaultListModel<String> keywordsModel;
	private DefaultListModel<String> modelNews;
	private DefaultListModel<String> commentModel;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					window = new ReporterWindow();
//					window.ReportFrame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		try {
			window = new ReporterWindow();
			window.ReportFrame.setVisible(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * Create the application.
	 */
	
	public ReporterWindow() {
		initView();
		initAction();
	}
	
	public static void showView(){
		if (window==null) {
			main(null);
		}else {
			window.ReportFrame.setVisible(true);
		}
		opened=1;
		try {
			if(CommonValues.analyzedKeywordList!=null&&CommonValues.analyzedKeywordList.size()!=0){
				window.initKeywordList();
				window.initReportList();
				window.windowStatus=1;
				window.ShowPieChartButton.setEnabled(true);
				window.ShowTimeTrendButton.setEnabled(true);
				window.showKeywordsPropotionButton.setEnabled(true);
				window.showEmotionValueChart.setEnabled(true);
				window.StatusText.setText("file imported");
			}else {
				window.initKeywordList();
				window.initReportList();
				window.windowStatus=-1;
				window.ShowPieChartButton.setEnabled(false);
				window.ShowTimeTrendButton.setEnabled(false);
				window.showKeywordsPropotionButton.setEnabled(false);
				window.showEmotionValueChart.setEnabled(false);
				window.StatusText.setText("no analyze file to report,please import");
			}
		} catch (Exception e) {
		}
	}
	
	public static void hideView(){
		if (window!=null) {
			window.ReportFrame.setVisible(false);
		}
		opened=0;
	}
	
	public void closeWindow(){
		if (window!=null) {
			window=null;
		}
	}
	
	private void initKeywordList(){
		keywordsModel=new DefaultListModel<>();
		KeywordsJList.setModel(keywordsModel);
		for (KeyWordsBean kBean : CommonValues.analyzedKeywordList) {
			keywordsModel.addElement(kBean.getKeywords()+" |emo: "+kBean.getEmotion());
		}
	}
	
	private void initReportList() throws Exception{
		reportList=new ArrayList<>();
		for (KeyWordsBean bean : CommonValues.analyzedKeywordList) {
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
	/**
	 * Initialize the contents of the frame.
	 */
	private void initView() {
		ReportFrame = new JFrame();
		ReportFrame.setTitle("Reporter");
		
		ReportFrame.setResizable(false);
		ReportFrame.setMinimumSize(new Dimension(1000, 600));
		ReportFrame.setMaximumSize(new Dimension(1000, 600));
		ReportFrame.setMaximizedBounds(new Rectangle(0, 0, 1000, 600));
		ReportFrame.setBounds(new Rectangle(0, 0, 1000, 600));
		ReportFrame.setBounds(100, 100, 450, 300);
		ReportFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ReportFrame.getContentPane().setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane.setBounds(10, 10, 974, 552);
		ReportFrame.getContentPane().add(layeredPane);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_1.setBounds(10, 10, 146, 454);
		layeredPane.add(layeredPane_1);
		
		JLabel KeywordsLabel = new JLabel("Keywords:");
		KeywordsLabel.setBounds(10, 10, 54, 15);
		layeredPane_1.add(KeywordsLabel);
		
		JScrollPane KeywordsScrollPane = new JScrollPane();
		KeywordsScrollPane.setBounds(10, 33, 126, 411);
		layeredPane_1.add(KeywordsScrollPane);
		
		
		KeywordsScrollPane.setViewportView(KeywordsJList);
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_2.setBounds(166, 10, 339, 454);
		layeredPane.add(layeredPane_2);
		
		JLabel NewsLabel = new JLabel("News:");
		NewsLabel.setBounds(10, 10, 54, 15);
		layeredPane_2.add(NewsLabel);
		
		JScrollPane NewsScrollPanel = new JScrollPane();
		NewsScrollPanel.setBounds(10, 35, 319, 409);
		layeredPane_2.add(NewsScrollPanel);
		
		
		NewsScrollPanel.setViewportView(NewsJList);
		
		JLayeredPane layeredPane_3 = new JLayeredPane();
		layeredPane_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane_3.setBounds(515, 10, 449, 454);
		layeredPane.add(layeredPane_3);
		
		JLabel CommentsLabel = new JLabel("Comments:");
		CommentsLabel.setBounds(10, 10, 76, 15);
		layeredPane_3.add(CommentsLabel);
		
		JScrollPane CommentsPanel = new JScrollPane();
		CommentsPanel.setBounds(10, 35, 429, 409);
		layeredPane_3.add(CommentsPanel);
		
		
		CommentsPanel.setViewportView(CommentsJList);
		
		
		
		ShowTimeTrendButton.setEnabled(false);
		ShowTimeTrendButton.setBounds(10, 474, 133, 35);
		layeredPane.add(ShowTimeTrendButton);
		
		JLabel AnalyzeFileImportLabel = new JLabel("CommentDataFilePath");
		AnalyzeFileImportLabel.setBounds(116, 519, 140, 20);
		layeredPane.add(AnalyzeFileImportLabel);
		
		AnalyzeFilePathTextField = new JTextField();
		AnalyzeFilePathTextField.setEditable(false);
		AnalyzeFilePathTextField.setColumns(10);
		AnalyzeFilePathTextField.setBounds(266, 519, 189, 21);
		layeredPane.add(AnalyzeFilePathTextField);
		
		
		
		AnalyzeFileImportButton.setBounds(465, 518, 93, 23);
		layeredPane.add(AnalyzeFileImportButton);
		
		
		
		ShowPieChartButton.setEnabled(false);
		ShowPieChartButton.setBounds(153, 474, 133, 35);
		layeredPane.add(ShowPieChartButton);
		
		JLabel StatusLabel = new JLabel("Status:");
		StatusLabel.setBounds(568, 522, 54, 15);
		layeredPane.add(StatusLabel);
		
		
		StatusText.setBounds(632, 522, 332, 15);
		layeredPane.add(StatusText);
		
		JButton BackButton = new JButton("Exit");
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideView();
				MainWindow.showView();
			}
		});
		BackButton.setBounds(10, 518, 93, 23);
		layeredPane.add(BackButton);
		showKeywordsPropotionButton.setEnabled(false);
		showKeywordsPropotionButton.setBounds(296, 474, 209, 35);
		layeredPane.add(showKeywordsPropotionButton);
		showEmotionValueChart.setEnabled(false);
		showEmotionValueChart.setBounds(515, 474, 189, 35);
		layeredPane.add(showEmotionValueChart);
	}
	
	public void initAction(){
		windowStatus=-1;
		AnalyzeFileImportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileSystemView fsv = FileSystemView.getFileSystemView();
					JFileChooser fileChooser=new JFileChooser();
					fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int i=fileChooser.showDialog(new JLabel(), "Open Analyze File");
	                if(i==JFileChooser.APPROVE_OPTION)  //判断是否为打开的按钮
	                {
	                	File file=fileChooser.getSelectedFile();
	    				AnalyzeFilePathTextField.setText(file.getAbsolutePath());  //取得选中的文件
	    				importedFile=file;
	    				CommonValues.analyzedKeywordList=MyFileUtil.readAnalyzeFromFile(file);
	    				StatusText.setText("file imported");
	    				
	    				keywordsModel=new DefaultListModel<>();
	    				KeywordsJList.setModel(keywordsModel);
	    				for (KeyWordsBean bean : CommonValues.analyzedKeywordList) {
							keywordsModel.addElement(bean.getKeywords()+" |emo: "+bean.getEmotion()+" |newsNumber: "+bean.getNews().size());
						}
	    				initReportList();
	    				windowStatus=1;
	    				ShowPieChartButton.setEnabled(true);
	    				ShowTimeTrendButton.setEnabled(true);
	    				showKeywordsPropotionButton.setEnabled(true);
	    				showEmotionValueChart.setEnabled(true);
	                }else {
						return;
					}
				} catch (Exception e2) {
					StatusText.setText("import failed");
				}
			}
		});
		
		KeywordsJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				modelNews=new DefaultListModel<>();
				int index=KeywordsJList.getSelectedIndex();
				if (index!=-1) {
					for (CommonNewsBean newsBean : CommonValues.analyzedKeywordList.get(index).getNews()) {
						modelNews.addElement(newsBean.getTitle()+" |emo: "+newsBean.getEmotion()+" |commentNumber: "+newsBean.getCommonList().size()+" |time: "+newsBean.getTime());
					}
				}
				NewsJList.setModel(modelNews);
			}
		});
		
		NewsJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				commentModel=new DefaultListModel<>();
				int index=NewsJList.getSelectedIndex();
				if (index!=-1) {
					for (CommonCommentBean commentBean : CommonValues.analyzedKeywordList.get(KeywordsJList.getSelectedIndex()).getNews().get(index).getCommonList()) {
						commentModel.addElement(commentBean.getContent()+" |emo: "+(commentBean.getEmotion()>0?"positive":"negative")+" |time: "+commentBean.getTime());
					}
				}
				CommentsJList.setModel(commentModel);
			}
		});
		
		ShowTimeTrendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ArrayList<String> xAixStr=new ArrayList<>();
					ArrayList<Timestamp> timeList=new ArrayList<>();
					ArrayList<Double> xValue=new ArrayList<>();
					ArrayList<Double> gyValue=new ArrayList<>();
					ArrayList<Double> byValue=new ArrayList<>();
					ArrayList<Double> ayValue=new ArrayList<>();
					
					
					DefaultXYDataset dataset=new DefaultXYDataset();
					Timestamp firstDay=reportList.get(0).getTime();
					DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH");
					String tmpDay=df.format(firstDay);
					Timestamp tDay=firstDay;
					
					
					double x=0;
					double gy=0;
					double by=0;
					double ay=0;
					for (ReportBean bean : reportList) {
						if (df.format(bean.getTime()).equals(tmpDay)) {
							if (bean.getEmotion()>0) {
								gy++;
							}else {
								by++;
							}
							ay++;
						}else {
							xAixStr.add(tmpDay);
							xValue.add(x);
							gyValue.add(gy);
							byValue.add(by);
							ayValue.add(ay);
							tmpDay=df.format(bean.getTime());
							tDay=bean.getTime();
							gy=0;
							by=0;
							ay=0;
							x++;
							if (bean.getEmotion()>0) {
								gy++;
							}else {
								by++;
							}
							ay++;
						}
					}
					xAixStr.add(tmpDay);
					timeList.add(tDay);
					xValue.add(x);
					gyValue.add(gy);
					byValue.add(by);
					ayValue.add(ay);
					
					double[] xv=new double[xValue.size()];
					double[] gyV=new double[gyValue.size()];
					double[] byV=new double[byValue.size()];
					double[] ayV=new double[ayValue.size()];
					for (int i=0;i<xValue.size();i++) {
						xv[i]=xValue.get(i);
					}
					for (int i=0;i<byValue.size();i++) {
						byV[i]=byValue.get(i);
					}
					for (int i=0;i<gyValue.size();i++) {
						gyV[i]=gyValue.get(i);
					}
					for (int i = 0; i < ayValue.size(); i++) {
						ayV[i]=ayValue.get(i);
					}
					dataset.addSeries("goodEmotion", new double[][]{xv,gyV});
					dataset.addSeries("badEmotion", new double[][]{xv,byV});
					dataset.addSeries("totalCommentNumber", new double[][]{xv,ayV});
					TimeSeries gtimeSeries = new TimeSeries("goodEmotion", Hour.class);
					TimeSeries btimeSeries = new TimeSeries("badEmotion", Hour.class);
					TimeSeries atimeSeries=new TimeSeries("totalComment",Hour.class);
					TimeSeriesCollection lineDataset = new TimeSeriesCollection();
					lineDataset.setDomainIsPointsInTime(true);
					for (int i=0;i<xAixStr.size();i++) {
						Date d=df.parse(xAixStr.get(i));
						Hour hour=new Hour(d);
						gtimeSeries.add(hour,gyValue.get(i));
						btimeSeries.add(hour,byValue.get(i));
						atimeSeries.add(hour,ayValue.get(i));
					}
					String string="";
					for (KeyWordsBean bean : CommonValues.analyzedKeywordList) {
						string+=bean.getKeywords()+" ";
					}
					lineDataset.addSeries(gtimeSeries);
					lineDataset.addSeries(btimeSeries);
					lineDataset.addSeries(atimeSeries);
					JFreeChart chart=ChartFactory.createXYLineChart("Emotion Time Average Trend Chart For Event :"+string, "x", "y", dataset);
					JFreeChart Tchart = ChartFactory.createTimeSeriesChart("Emotion Time Trend Chart For Event :"+string, "time", "amount", lineDataset, true, true, true);
					chart.getTitle().setFont((new Font("宋体", Font.BOLD, 20)));
					Tchart.getTitle().setFont((new Font("宋体", Font.BOLD, 20)));
					XYPlot plot = (XYPlot) Tchart.getPlot(); 
					XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer(); 
					//设置网格背景颜色 
					plot.setBackgroundPaint(Color.white); 
					//设置网格竖线颜色 
					plot.setDomainGridlinePaint(Color.pink); 
					//设置网格横线颜色 
					plot.setRangeGridlinePaint(Color.pink); 
					//设置曲线图与xy轴的距离 
					plot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D)); 
					//设置曲线是否显示数据点 
					xylineandshaperenderer.setBaseShapesVisible(false); 
					//设置曲线显示各数据点的值 

					
					ValueAxis domainAxis=plot.getDomainAxis();  
					domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,10));//设置x轴坐标上的字体  
					domainAxis.setLabelFont(new Font("宋体",Font.BOLD,10));//设置x轴坐标上的标题的字体
					DateAxis dateAxis=(DateAxis)plot.getDomainAxis();
					dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 1,df));
					dateAxis.setVerticalTickLabels(true);
					dateAxis.setAutoTickUnitSelection(true); // 由于横轴标签过多，这里设置为自动格式 。  
		            dateAxis.setDateFormatOverride(df);  
		            dateAxis.setTickMarkPosition(DateTickMarkPosition.START); 
					ValueAxis rangeAxis=plot.getRangeAxis();  
					rangeAxis.setTickLabelFont(new Font("宋体",Font.BOLD,10));//设置y轴坐标上的字体  
					rangeAxis.setLabelFont(new Font("宋体",Font.BOLD,10));//设置y轴坐标上的标题的字体  
					
					
					ChartFrame TchartFrame=new ChartFrame("Emotion Time Trend Chart for Event: "+string, Tchart);
					TchartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					TchartFrame.pack();
					TchartFrame.setVisible(true);
					
					ChartFrame chartFrame=new ChartFrame("Emotion Average Time Trend Chart For Event"+string, chart);
					chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					chartFrame.pack();
					chartFrame.setLocation(700, 0);
					chartFrame.setVisible(true);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		
		
		
		ShowPieChartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int goodEmotion=0;
					int badEmotion=0;
					for (ReportBean bean : reportList) {
						if (bean.getEmotion()>0) {
							goodEmotion++;
						}else if (bean.getEmotion()<=0) {
							badEmotion++;
						}
					}
					DefaultPieDataset dataset=new DefaultPieDataset();
					dataset.setValue("Good Emotion Number", goodEmotion);
					dataset.setValue("Bad Emotion Number", badEmotion);
					String string="";
					for (KeyWordsBean bean : CommonValues.analyzedKeywordList) {
						string+=bean.getKeywords()+" ";
					}
					JFreeChart pieChart=ChartFactory.createPieChart("Emotion Proportion for event:"+string, dataset, true, true, true);
					pieChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,  
			                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);  
					pieChart.getTitle().setFont((new Font("宋体", Font.BOLD, 20)));
					PiePlot plot=(PiePlot)pieChart.getPlot();
					plot.setLabelGenerator(new StandardPieSectionLabelGenerator(  
			                "{0}：{1}({2} percent)")); 
					plot.setLabelBackgroundPaint(new Color(220, 220, 220));
			        plot.setLabelFont((new Font("宋体", Font.PLAIN, 12)));
					ChartFrame pieChartFrame=new ChartFrame("Proportion Chart", pieChart);
					pieChartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					pieChartFrame.pack();
					pieChartFrame.setVisible(true);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		
		
		
		ReportFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				
				MainWindow.showView();
			}
		});
		
		showKeywordsPropotionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultCategoryDataset dataset=new DefaultCategoryDataset();
				for (KeyWordsBean kb : CommonValues.analyzedKeywordList) {
					int gv=0;
					int bv=0;
					int tv=0;
					for (CommonNewsBean nb : kb.getNews()) {
						for (CommonCommentBean cb : nb.getCommonList()) {
							if (cb.getEmotion()>0) {
								gv++;
							}else {
								bv++;
							}
							tv++;
						}
					}
					dataset.addValue(gv, "goodEmotionNumber", kb.getKeywords());
					dataset.addValue(bv, "badEmotionNumber", kb.getKeywords());
					dataset.addValue(tv, "totalCommentNumber", kb.getKeywords());
				}
				JFreeChart barChart=ChartFactory.createBarChart3D("Keywords Proportion Chart", "Keywords", "Amount", dataset);
				CategoryPlot plot=(CategoryPlot)barChart.getPlot();
				BarRenderer3D xylineandshaperenderer = (BarRenderer3D)plot.getRenderer();
				xylineandshaperenderer.setBaseItemLabelsVisible(true);
				xylineandshaperenderer.setBaseSeriesVisibleInLegend(true);
				CategoryAxis domainAxis=plot.getDomainAxis();
				domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,20));//设置x轴坐标上的字体  
				domainAxis.setLabelFont(new Font("宋体",Font.BOLD,20));//设置x轴坐标上的标题的字体  
				ValueAxis rangeAxis=plot.getRangeAxis();  
				rangeAxis.setTickLabelFont(new Font("宋体",Font.BOLD,20));//设置y轴坐标上的字体  
				rangeAxis.setLabelFont(new Font("宋体",Font.BOLD,20));//设置y轴坐标上的标题的字体
				BarRenderer3D renderer = new BarRenderer3D();
				renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
				renderer.setBaseItemLabelsVisible(true);
				renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
				renderer.setItemLabelAnchorOffset(10D);
				plot.setRenderer(renderer);
				ChartFrame frame=new ChartFrame("Keywords Proportion Chart", barChart);
				frame.pack();
				frame.setVisible(true);
			}
		});
		
		showEmotionValueChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultCategoryDataset dataset=new DefaultCategoryDataset();
				
//				for (KeyWordsBean bean : CommonValues.analyzedKeywordList) {
//					dataset.addValue(bean.getEmotion(),  "keyword",bean.getKeywords());
//					int i=0;
//					for (CommonNewsBean nBean : bean.getNews()) {
//						dataset.addValue(nBean.getEmotion(), "news",i+"");
//						i++;
//					}
//				}
				double ke=0;
				double ne=0;
				double ce=0;
				int Ncounter=0;
				for (KeyWordsBean kbean : CommonValues.analyzedKeywordList) {
					Ncounter=0;
					ne=0;
					for (CommonNewsBean nBean : kbean.getNews()) {
						ce=0;
						if (nBean.getCommonList().size()==0) {
							continue;
						}
						for (CommonCommentBean cBean : nBean.getCommonList()) {
							ce+=cBean.getEmotion();
						}
						ne+=(ce/(double)nBean.getCommonList().size());
						dataset.addValue(ce/(double)nBean.getCommonList().size(), "news", kbean.getKeywords()+Ncounter);
						Ncounter++;
					}
					ke=(ne/(double)Ncounter);
					dataset.addValue(ke, "keyword", kbean.getKeywords());
				}
				JFreeChart barchart=ChartFactory.createBarChart("Emotion Value Chart", "News and Keywords", "value", dataset, PlotOrientation.VERTICAL, true, true, true);
				CategoryPlot plot=(CategoryPlot) barchart.getPlot();
				CategoryAxis domainAxis=plot.getDomainAxis();
				BarRenderer renderer = new BarRenderer();
				renderer.setBaseItemLabelPaint(Color.BLACK);//数据字体的颜色 
				renderer.setBaseItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());  
		        renderer.setBaseItemLabelsVisible(true); 
		        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.TOP_CENTER));
		        renderer.setItemLabelAnchorOffset(5D);
				renderer.setItemMargin(0.4);
		        plot.setRenderer(renderer);
				domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,15));//设置x轴坐标上的字体  
				domainAxis.setLabelFont(new Font("宋体",Font.BOLD,15));//设置x轴坐标上的标题的字体
				
				domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
				
//				BarRenderer3D renderer = new BarRenderer3D();
//				renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
//				renderer.setBaseItemLabelsVisible(true);
//				 
//				//默认的数字显示在柱子中，通过如下两句可调整数字的显示
//				//注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
//				renderer.setBaseItemLabelFont(new Font("宋体",Font.BOLD,5));
//				renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
//				renderer.setItemLabelAnchorOffset(10D);
//				 
//				//设置每个地区所包含的平行柱的之间距离

//				plot.setRenderer(renderer);
				
				ChartFrame frame=new ChartFrame("News Emotion Value Chart", barchart);
				frame.pack();
				frame.setVisible(true);
			}
		});
		
	}
}
