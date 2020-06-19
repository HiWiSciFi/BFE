package bfeditor.gui;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;

public class Preferences {
	
	private static JFrame frame;
	
	private static int width = 300;
	private static int height = 300;
	
	public static void init() {
		frame = new JFrame("Preferences");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(true);
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setLocation(screenCenter.x - (width/2), screenCenter.y - (height/2));
		frame.setSize(width, height);
		loadPrefs();
		
	}
	
	public static void show() {
		if (frame.isVisible()) {
			frame.toFront();
			frame.requestFocus();
		} else {
			frame.setVisible(true);
		}
	}
	
	public static void savePrefs() {
		
	}
	
	public static void loadPrefs() {
		
	}
}