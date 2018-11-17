package com.org.videomonitor.main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.ffpojo.exception.FFPojoException;
import com.org.videomonitor.configs.ConfigSingleton;
import com.org.videomonitor.models.ComboBoxItemInfor;
import com.org.videomonitor.tables.UrlTableModel;
import com.org.videomonitor.utilities.FfpojoUtils;
import com.org.videomonitor.utilities.Log4jUtils;
import com.org.videomonitor.utilities.TimeUtils;

import javafx.application.Platform;

/**
 * https://stackoverflow.com/questions/7461477/how-to-hide-a-jframe-in-system-tray-of-taskbar
 * 
 * 
 * @author Mohammad Faisal ermohammadfaisal.blogspot.com
 *         facebook.com/m.faisal6621
 *
 */
public class VideoMonitorSystemTrayFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6001275668059105321L;
	static Logger logger = Logger.getLogger(VideoMonitorSystemTrayFrame.class.getName());
	TrayIcon trayIcon;
	SystemTray tray;
	private static final String iconImageLoc = "http://icons.iconarchive.com/icons/scafer31000/bubble-circle-3/16/GameCenter-icon.png";
	// a timer allowing the tray icon to provide a periodic notification event.
	private Timer notificationTimer = new Timer();

	// format used to display the current time in a tray icon notification.
	private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
	public static Boolean isOpened = false;
	public static int timer = 500;
	public static int indexTimer = 1;
	public static int sleep = 5000;
	public static int INDEX = 0;
	public static int browsers = 1;

	/**
	 * http://play.sohatv.vn/?v=b3VLaE1hVWZQcnM9&__t=true
	 * https://github.com/iheartradio/open-m3u8
	 * https://github.com/caprica/vlcj-player
	 */
	VideoMonitorSystemTrayFrame() {
		super("Video Monitor System");
		listDrivers = new ArrayList<>();
		if (logger.isDebugEnabled())
			logger.debug("creating instance");
		try {
			if (logger.isDebugEnabled())
				logger.debug("setting look and feel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to set LookAndFeel");
		}
		isOpened = true;
		// frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		if (SystemTray.isSupported()) {
			try {
				if (logger.isDebugEnabled())
					logger.debug("system tray supported");
				tray = SystemTray.getSystemTray();

				// Image image =
				// Toolkit.getDefaultToolkit().getImage("/media/faisal/DukeImg/Duke256.png");
				// java.awt.SystemTray tray =
				// java.awt.SystemTray.getSystemTray();
				URL imageLoc = new URL(iconImageLoc);
				java.awt.Image image = ImageIO.read(imageLoc);

				ActionListener exitListener = new ActionListener() {
					@SuppressWarnings("restriction")
					public void actionPerformed(ActionEvent e) {
						notificationTimer.cancel();
						if (logger.isDebugEnabled())
							logger.debug("Exiting....");
						Platform.exit();
						tray.remove(trayIcon);
						System.exit(0);
					}
				};
				PopupMenu popup = new PopupMenu();
				MenuItem defaultItem = new MenuItem("Thoát");
				defaultItem.addActionListener(exitListener);
				popup.add(defaultItem);
//				defaultItem = new MenuItem("Open");
//				defaultItem.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						if (logger.isDebugEnabled())
//							logger.debug("Openning....");
//						setVisible(true);
//						setExtendedState(JFrame.NORMAL);						
//						//isOpened = true;
//					}
//				});
//				popup.add(defaultItem);
				trayIcon = new TrayIcon(image, "Video Monitor System", popup);
				trayIcon.setImageAutoSize(true);
				// setup the popup menu for the application.

				// create a timer which periodically displays a notification
				// message.
				notificationTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						// javax.swing.SwingUtilities.invokeLater(() ->
						// trayIcon.displayMessage("hello",
						// "The time is now " + timeFormat.format(new Date()),
						// java.awt.TrayIcon.MessageType.INFO));
						if (indexTimer % timer == 0) {
							if (modelObject.getRowCount() > 0) {
								if (INDEX < modelObject.getRowCount()) {
									String url = modelObject.getValueAt(INDEX, 1).toString();
									if (logger.isDebugEnabled())
										logger.debug("goto :" + url + ":" + new Date().toString());
									runChromeBrowser(url, browsers);
									try {
										Thread.sleep(sleep);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									for (int index = 0; index < browsers; index++) {
										listDrivers.get(index).close();
										listDrivers.remove(index);
									}

									indexTimer++;
								} else {
									INDEX = 0;
								}
							}
						} else {
							if (logger.isDebugEnabled())
								logger.debug("current[" + indexTimer + "/" + timer + "]:" + new Date().toString());
							indexTimer++;
						}
					}
				}, 1000L, 60000L);
				try {
					tray.add(trayIcon);
					setVisible(false);
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already
				// there
				// setExtendedState(JFrame.MAXIMIZED_BOTH);
				// setUndecorated(true);
				// JFrame.setType(javax.swing.JFrame.Type.UTILITY);
				// add the application t
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			if (logger.isDebugEnabled())
				logger.debug("system tray not supported");
		}
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
//				if (e.getNewState() == ICONIFIED) {
//					try {
//						tray.add(trayIcon);
//						setVisible(false);
//						if (logger.isDebugEnabled())
//							logger.debug("added to SystemTray");
//					} catch (Exception ex) {
//						ex.printStackTrace();
//						if (logger.isDebugEnabled())
//							logger.debug("unable to add to tray");
//					}
//				}
//				if (e.getNewState() == 7) {
//					try {
//						tray.add(trayIcon);
//						setVisible(false);
//						if (logger.isDebugEnabled())
//							logger.debug("added to SystemTray");
//					} catch (AWTException ex) {
//						ex.printStackTrace();
//						if (logger.isDebugEnabled())
//							logger.debug("unable to add to system tray");
//					}
//				}
//				if (e.getNewState() == MAXIMIZED_BOTH) {
//					tray.remove(trayIcon);
//					setVisible(true);
//					if (logger.isDebugEnabled())
//						logger.debug("Tray icon removed");
//				}
//				if (e.getNewState() == NORMAL) {
//					tray.remove(trayIcon);
//					setVisible(true);
//					if (logger.isDebugEnabled())
//						logger.debug("Tray icon removed");
//				}
			}

		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				if (logger.isDebugEnabled())
					logger.debug("run windowClosing");
				isOpened = false;
			}
		});
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// add to the system tray if not already done so
				// e.getWindow().setVisible(false);
				// setVisible(true);
				// remove from the system tray if you want to
				// the following three lines take the current state, remove the
				// iconified flag from it, then sets the new state
//				int state = getExtendedState();
//				if (logger.isDebugEnabled())
//					logger.debug("state:" + state+":isOpened:"+isOpened);
//				state = state & ~Frame.ICONIFIED;
//				if (isOpened = false) {
//					setExtendedState(state);
//					isOpened = true;
//				}
//
//				if (logger.isDebugEnabled())
//					logger.debug("run windowIconified");

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				// no minimizing here
				// add to the system tray if not already done so
				e.getWindow().setVisible(false);
				int state = getExtendedState();
				state = state | Frame.ICONIFIED;
				setExtendedState(state);
				if (logger.isDebugEnabled())
					logger.debug("run windowClosing");
				isOpened = false;

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		URL imageLoc;
		try {
			imageLoc = new URL(iconImageLoc);
			java.awt.Image image = ImageIO.read(imageLoc);
			setIconImage(image);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));

		initControl();
		setVisible(true);
		// setSize(300, 200);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	JTextField jTextFieldURL;
	JLabel jLabelURL;
	JButton jButtonAdd = null;
	private JTable jTableMyObject;
	protected JComboBox<ComboBoxItemInfor> cmbSelectTimer = null;
	protected JComboBox<ComboBoxItemInfor> cmbSelectDelay = null;
	protected JComboBox<ComboBoxItemInfor> cmbSelectBrowsers = null;

	void initControl() {

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(90, 235, dim.width - 100, 600);
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		this.getContentPane().setLayout(new BorderLayout());
		jLabelURL = new JLabel("URL (Video)");
		jTextFieldURL = new JTextField(25);
		jButtonAdd = new JButton("Thêm tự động chạy");
		cmbSelectTimer = genComboBoxSelectRunable(0);
		cmbSelectDelay = genComboBoxRelaySelectRunable(0);
		cmbSelectBrowsers = genComboBoxBrowsersSelectRunable(0);
		jButtonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (logger.isDebugEnabled())
					logger.debug("click Thêm tự động chạy");
				if (!jTextFieldURL.getText().isEmpty() || jTableMyObject.getModel().getRowCount() > 0) {
					try {
						String url = jTextFieldURL.getText();
						if (!url.isEmpty())
							FfpojoUtils.writeObject(url);
						int id = ((ComboBoxItemInfor) cmbSelectTimer.getSelectedItem()).getId();
						if (logger.isDebugEnabled())
							logger.debug("id:" + id);
						timer = id;
						if (logger.isDebugEnabled())
							logger.debug("timer:" + timer);
						int sleepId = ((ComboBoxItemInfor) cmbSelectDelay.getSelectedItem()).getId();
						if (logger.isDebugEnabled())
							logger.debug("sleepId:" + sleepId);

						browsers = ((ComboBoxItemInfor) cmbSelectBrowsers.getSelectedItem()).getId();
						if (logger.isDebugEnabled())
							logger.debug("browsers:" + browsers);
						sleep = sleepId * 1000;
						if (logger.isDebugEnabled())
							logger.debug("sleep:" + sleep);
						jTextFieldURL.setText("");
						if (!url.isEmpty())
							modelObject.addRow(url, true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FFPojoException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		this.add(new JPanel() {

			private static final long serialVersionUID = 2001167537556826160L;

			{

				setLayout(new FlowLayout(FlowLayout.CENTER));
				add(jLabelURL);
				add(jTextFieldURL);
				add(new JLabel("Chạy tuần tự sau:"));
				add(cmbSelectTimer);
				add(cmbSelectDelay);
				add(new JLabel("Số trình duyệt:"));
				add(cmbSelectBrowsers);
				add(jButtonAdd);

			}
		}, BorderLayout.NORTH);

		jTableMyObject = new JTable();
		jTableMyObject.setFillsViewportHeight(false);

		jTableMyObject.setModel(modelObject);
		JTableHeader header = jTableMyObject.getTableHeader();
		header.setDefaultRenderer(new HeaderRenderer(jTableMyObject));
		modelObject = new UrlTableModel();
		jTableMyObject.setModel(modelObject);
		// STT
		jTableMyObject.getColumnModel().getColumn(0).setMinWidth(30);
		jTableMyObject.getColumnModel().getColumn(0).setMaxWidth(50);
		// id
//		 jTableMyObject.getColumnModel().getColumn(1).setMinWidth(140);
//		 jTableMyObject.getColumnModel().getColumn(1).setMaxWidth(160);

		JScrollPane pane = new JScrollPane(jTableMyObject);
		showMyTable();
		add(pane, BorderLayout.CENTER);
	}

	// UrlTableModel urlTableModel;
	public static JComboBox<ComboBoxItemInfor> genComboBoxSelectRunable(int selected) {
		if (logger.isDebugEnabled())
			logger.debug("run genComboBoxSelectRunable");
		ComboBoxItemInfor[] arrCombo = new ComboBoxItemInfor[50];

		int j = 1;
		// int index = 0;
		boolean isStep = true;
		for (int i = 0; i < arrCombo.length; i++) {
			arrCombo[i] = new ComboBoxItemInfor(j, j + " phút", j + "");
			if (isStep) {
				if (j < 5)
					j++;
				else
					j = j + 5;
			} else {
				j++;
			}
		}
		if (selected < 0 || selected > 49)
			selected = 0;
		JComboBox<ComboBoxItemInfor> cbox = new JComboBox<ComboBoxItemInfor>(arrCombo);
		cbox.setSelectedIndex(selected);

		return cbox;
	}

	public static JComboBox<ComboBoxItemInfor> genComboBoxRelaySelectRunable(int selected) {
		if (logger.isDebugEnabled())
			logger.debug("run genComboBoxRelaySelectRunable");
		ComboBoxItemInfor[] arrCombo = new ComboBoxItemInfor[10];

		int j = 5;

		boolean isStep = true;
		for (int i = 0; i < arrCombo.length; i++) {
			arrCombo[i] = new ComboBoxItemInfor(j, "Dừng " + j + " giây", j + "");
			if (isStep) {
				j = j + 5;
			} else {
				j++;
			}
		}
		if (selected < 0 || selected > 9)
			selected = 0;
		JComboBox<ComboBoxItemInfor> cbox = new JComboBox<ComboBoxItemInfor>(arrCombo);
		cbox.setSelectedIndex(selected);

		return cbox;
	}

	public static JComboBox<ComboBoxItemInfor> genComboBoxBrowsersSelectRunable(int selected) {
		if (logger.isDebugEnabled())
			logger.debug("run genComboBoxBrowsersSelectRunable");
		ComboBoxItemInfor[] arrCombo = new ComboBoxItemInfor[10];

		int j = 1;

		for (int i = 0; i < arrCombo.length; i++) {
			arrCombo[i] = new ComboBoxItemInfor(j, j + "", j + "");
			j++;

		}
		if (selected < 0 || selected > 9)
			selected = 0;
		JComboBox<ComboBoxItemInfor> cbox = new JComboBox<ComboBoxItemInfor>(arrCombo);
		cbox.setSelectedIndex(selected);

		return cbox;
	}

	public void showMyTable() {
		int limit = 100;
		List<String> listUrl = null;
		try {
			listUrl = FfpojoUtils.readObject(1, limit);
			if (listUrl != null && listUrl.size() > 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("listUrl.size:" + listUrl.size());
				}
				int index = 1;
				for (String item : listUrl) {
					if (logger.isDebugEnabled()) {
						logger.debug("addRow:" + item);
					}
					// modelObject.addRow(new Object[] { index + "", item });
					index++;

				}
				modelObject.updateListRow(listUrl);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("listUrl.size:0");
				}
			}
		} catch (Exception e) {
			logger.error("showMyTable1", e);
		}
	}

	UrlTableModel modelObject = new UrlTableModel();

	private class HeaderRenderer implements TableCellRenderer {

		DefaultTableCellRenderer renderer;

		public HeaderRenderer(JTable table) {
			renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);

		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int col) {
			if (col == 1) {
				renderer.setHorizontalAlignment(JLabel.CENTER);
				return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			} else {
				renderer.setHorizontalAlignment(JLabel.LEFT);
				return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			}
		}
	}

	public static final String SESSION_FOLDER = "sessions";
	List<WebDriver> listDrivers;
	static final int pageLoadTimeout = 30;

	public boolean runChromeBrowser(String url, int browsers) {

		if (url != null) {
			for (int index = 0; index < browsers; index++) {
				if (logger.isDebugEnabled())
					logger.debug("SESSION_FOLDER:" + SESSION_FOLDER + "_" + index);
				initWebDriver(true, SESSION_FOLDER + "_" + index, index);
				try {
					if (listDrivers.get(index) != null) {
						listDrivers.get(index).get(url);
						listDrivers.get(index).manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
					}
				} catch (org.openqa.selenium.TimeoutException e) {
					e.printStackTrace();
				}
			}

			return true;
		} else
			return false;
	}

	public void initWebDriver(boolean isSaveSessionBrowser, String sessionFolder, int index) {

		if (listDrivers.size() == index) {
			ConfigSingleton.getInstance().setChromeDriverProperty();
			ConfigSingleton.getInstance().setChromeLogs();
			// System.setProperty("webdriver.chrome.logfile",
			// "/home/tin/eclipse-workspace/FBGrapher/FBGrapher/crawl_data/logs/chromedriver.log");
			ChromeOptions options = new ChromeOptions();
			Map<String, Object> chromePreferences = new HashMap<String, Object>();
			chromePreferences.put("profile.default_content_setting_values.notifications", 2);
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			chromePreferences.put("profile.default_content_settings.popups", 0);
			// options.setC
			String downloadDirectory = ConfigSingleton.getInstance().getChromeDownloadDirectory();
			if (downloadDirectory != null) {
				chromePreferences.put("download.default_directory", downloadDirectory);
				if (logger.isDebugEnabled())
					logger.debug("download.default_directory:" + downloadDirectory);
			} else {
				if (logger.isDebugEnabled())
					logger.debug("download.default_directory:null");
			}
			// profileId=randomId;
			logger.debug("sessionFolder=" + sessionFolder);

			// ----------------- Bước 2: sử dụng chrome bình thường
			// 350685531728 : Facebook for Android
			options.addArguments("--user-agent="
					+ "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/67.0.3396.62 Chrome/67.0.3396.62 Safari/537.36");

			if (isSaveSessionBrowser) {
				options.addArguments("user-data-dir=" + sessionFolder);
				logger.debug("user-data-dir=" + sessionFolder);
			}

			try {
				// https://stackoverflow.com/questions/23834413/pass-driver-chromeoptions-and-desiredcapabilities
				// http://chromedriver.chromium.org/capabilities
				// https://stackoverflow.com/questions/26772793/org-openqa-selenium-unhandledalertexception-unexpected-alert-open
				options.setExperimentalOption("prefs", chromePreferences);
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				options.addArguments("disable-infobars");
				options.merge(capabilities);
				listDrivers.add(new ChromeDriver(options));
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (listDrivers.get(index) != null) {
				if (logger.isDebugEnabled())
					logger.debug("driver[" + index + "]");
				// driver.manage().timeouts().implicitlyWait(10,
				// TimeUnit.SECONDS);
				listDrivers.get(index).manage().timeouts().implicitlyWait(12000, TimeUnit.SECONDS);
				listDrivers.get(index).manage().window().maximize();

				if (!isSaveSessionBrowser)
					listDrivers.get(index).manage().deleteAllCookies();
			}

		}
	}

	public static void main(String[] args) {
		logger = Log4jUtils.initLog4j();
		Properties properties = new Properties();
		properties.setProperty("log4j.rootLogger", "TRACE,stdout,MyFile");
		properties.setProperty("log4j.rootCategory", "TRACE");

		properties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		properties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		// properties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%d{yyyy/MM/dd
		// HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");

		// properties.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");

		properties.setProperty("log4j.appender.MyFile", "org.apache.log4j.RollingFileAppender");
		properties.setProperty("log4j.appender.MyFile.File", "logs/video-monitor-" + TimeUtils.getNow() + ".log");
		properties.setProperty("log4j.appender.MyFile.MaxFileSize", "10mb");
		properties.setProperty("log4j.appender.MyFile.MaxBackupIndex", "1");
		properties.setProperty("log4j.appender.MyFile.layout", "org.apache.log4j.PatternLayout");
		// properties.setProperty("log4j.appender.MyFile.layout.ConversionPattern","%d{yyyy/MM/dd
		// HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");
		properties.setProperty("log4j.appender.MyFile.layout.ConversionPattern", "%d %5p [%l] - %m%n");

		properties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		// properties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%5p
		// | %d | %F | %L | %m%n");
		properties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d %5p %l - %m%n");
		// %d %5p [%l] - %m%n
		PropertyConfigurator.configure(properties);

		Logger logger = Logger.getLogger("log4j");
		if (logger.isDebugEnabled())
			logger.debug("run VideoMonitorSystemTrayFrame");
		new VideoMonitorSystemTrayFrame();
	}
}