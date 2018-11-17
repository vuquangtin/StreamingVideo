package com.org.videomonitor.ex.main;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

import javafx.application.Platform;

/**
 * https://stackoverflow.com/questions/7461477/how-to-hide-a-jframe-in-system-tray-of-taskbar
 * 
 * 
 * @author Mohammad Faisal ermohammadfaisal.blogspot.com
 *         facebook.com/m.faisal6621
 *
 */

public class VideoMonitorFrameSystemTray extends JFrame {
	TrayIcon trayIcon;
	SystemTray tray;
	private static final String iconImageLoc = "http://icons.iconarchive.com/icons/scafer31000/bubble-circle-3/16/GameCenter-icon.png";
	// a timer allowing the tray icon to provide a periodic notification event.
	private Timer notificationTimer = new Timer();

	// format used to display the current time in a tray icon notification.
	private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();

	VideoMonitorFrameSystemTray() {
		super("SystemTray test");
		System.out.println("creating instance");
		try {
			System.out.println("setting look and feel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to set LookAndFeel");
		}
		if (SystemTray.isSupported()) {
			try {
				System.out.println("system tray supported");
				tray = SystemTray.getSystemTray();

				// Image image =
				// Toolkit.getDefaultToolkit().getImage("/media/faisal/DukeImg/Duke256.png");
				// java.awt.SystemTray tray =
				// java.awt.SystemTray.getSystemTray();
				URL imageLoc = new URL(iconImageLoc);
				java.awt.Image image = ImageIO.read(imageLoc);

				ActionListener exitListener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						notificationTimer.cancel();
						System.out.println("Exiting....");
						Platform.exit();
						tray.remove(trayIcon);
						System.exit(0);
					}
				};
				PopupMenu popup = new PopupMenu();
				MenuItem defaultItem = new MenuItem("Exit");
				defaultItem.addActionListener(exitListener);
				popup.add(defaultItem);
				defaultItem = new MenuItem("Open");
				defaultItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(true);
						setExtendedState(JFrame.NORMAL);
					}
				});
				popup.add(defaultItem);
				trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
				trayIcon.setImageAutoSize(true);
				// setup the popup menu for the application.

				// create a timer which periodically displays a notification
				// message.
				notificationTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						javax.swing.SwingUtilities.invokeLater(() -> trayIcon.displayMessage("hello",
								"The time is now " + timeFormat.format(new Date()),
								java.awt.TrayIcon.MessageType.INFO));
					}
				}, 5_000, 60_000);
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
			System.out.println("system tray not supported");
		}
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == ICONIFIED) {
					try {
						tray.add(trayIcon);
						setVisible(false);
						System.out.println("added to SystemTray");
					} catch (Exception ex) {
						System.out.println("unable to add to tray");
					}
				}
				if (e.getNewState() == 7) {
					try {
						tray.add(trayIcon);
						setVisible(false);
						System.out.println("added to SystemTray");
					} catch (AWTException ex) {
						System.out.println("unable to add to system tray");
					}
				}
				if (e.getNewState() == MAXIMIZED_BOTH) {
					tray.remove(trayIcon);
					setVisible(true);
					System.out.println("Tray icon removed");
				}
				if (e.getNewState() == NORMAL) {
					tray.remove(trayIcon);
					setVisible(true);
					System.out.println("Tray icon removed");
				}
			}

		});
		// addWindowListener(new WindowListener() {
		//
		// @Override
		// public void windowOpened(WindowEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void windowIconified(WindowEvent e) {
		// // add to the system tray if not already done so
		// e.getWindow().setVisible(false);
		// setVisible(true);
		// // remove from the system tray if you want to
		// // the following three lines take the current state, remove the
		// // iconified flag from it, then sets the new state
		// int state = getExtendedState();
		// state = state & ~Frame.ICONIFIED;
		// setExtendedState(state);
		// System.out.println("run windowIconified");
		//
		// }
		//
		// @Override
		// public void windowDeiconified(WindowEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void windowDeactivated(WindowEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void windowClosing(WindowEvent e) {
		// // no minimizing here
		// // add to the system tray if not already done so
		// e.getWindow().setVisible(false);
		// int state = getExtendedState();
		// state = state | Frame.ICONIFIED;
		// setExtendedState(state);
		// System.out.println("run windowClosing");
		//
		// }
		//
		// @Override
		// public void windowClosed(WindowEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void windowActivated(WindowEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));

		setVisible(true);
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new VideoMonitorFrameSystemTray();
	}
}