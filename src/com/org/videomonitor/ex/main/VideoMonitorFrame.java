package com.org.videomonitor.ex.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class VideoMonitorFrame extends JFrame {

	private static final long serialVersionUID = -9180248262504475041L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("run VideoMonitorFrame");
					VideoMonitorFrame frame = new VideoMonitorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VideoMonitorFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 640 + 2 * border, 960 + 2 * border);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(border, border, border, border));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

	}

	int border = 5;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g = contentPane.getGraphics();

	}

	public void windowIconified(WindowEvent e) {
		// add to the system tray if not already done so
		e.getWindow().setVisible(false);
		this.setVisible(true);
		// remove from the system tray if you want to
		// the following three lines take the current state, remove the
		// iconified flag from it, then sets the new state
		int state = this.getExtendedState();
		state = state & ~Frame.ICONIFIED;
		this.setExtendedState(state);
		System.out.println("run windowIconified");
	}

	public void windowClosing(WindowEvent e) {
		// no minimizing here
		// add to the system tray if not already done so
		e.getWindow().setVisible(false);
		int state = this.getExtendedState();
		state = state | Frame.ICONIFIED;
		this.setExtendedState(state);
		System.out.println("run windowClosing");
	}
}
