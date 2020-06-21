package bfe.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import bfe.io.IniLoader;
import bfe.logic.Main;

public class Preferences {
	
	private static JFrame frame;
	
	private static int width = 300;
	private static int height = 300;
	
	private static IniLoader prefsFile;
	
	private static JTextField consoleHorizontalInput;
	private static JTextField hotbarVerticalInput;
	
	public static float HotbarVerticalPercentage() {
		return prefsFile.ReadSingle(Main.pluginIniName, "HotbarVerticalPercentage");
	}
	
	public static float ConsoleHorizontalPercentage() {
		return prefsFile.ReadSingle(Main.pluginIniName, "ConsoleHorizontalPercentage");
	}
	
	public static void Init() {
		frame = new JFrame("Preferences");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(false);
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setLocation(screenCenter.x - (width/2), screenCenter.y - (height/2));
		frame.setSize(width, height);
		
		try {
			prefsFile = new IniLoader("data/prefs.ini");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		frame.setLayout(new BorderLayout());
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(2, 1));
		frame.add(settingsPanel, BorderLayout.CENTER);
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 3));
		frame.add(buttonsPanel, BorderLayout.SOUTH);
		
		KeyListener numberFieldListener = new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {
				JTextField source = (JTextField)e.getSource();
				StringBuilder sb = new StringBuilder();
				boolean dotFound = false;
				for (int i = 0; i < source.getText().length(); i++) {
					char c = source.getText().charAt(i);
					if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
						sb.append(c);
					} else if (c == '.' && !dotFound) {
						sb.append(c);
						dotFound = true;
					}
				}
				source.setText(sb.toString());
			}
		};
		
		JPanel hotbarVerticalSetting = new JPanel();
		hotbarVerticalSetting.setLayout(new GridLayout(1, 2));
		hotbarVerticalSetting.add(new JLabel("Hotbar vertical percentage:"));
		hotbarVerticalInput = new JTextField();
		hotbarVerticalInput.addKeyListener(numberFieldListener);
		hotbarVerticalSetting.add(hotbarVerticalInput);
		settingsPanel.add(hotbarVerticalSetting);
		
		JPanel consoleHorizontalSetting = new JPanel();
		consoleHorizontalSetting.setLayout(new GridLayout(1, 2));
		consoleHorizontalSetting.add(new JLabel("Console horizontal percentage:"));
		consoleHorizontalInput = new JTextField();
		consoleHorizontalInput.addKeyListener(numberFieldListener);
		consoleHorizontalSetting.add(consoleHorizontalInput);
		settingsPanel.add(consoleHorizontalSetting);
		
		JButton saveButton = new JButton("Save");
		buttonsPanel.add(saveButton);
		JButton revertButton = new JButton("Revert to default");
		buttonsPanel.add(revertButton);
		JButton cancelButton = new JButton("Cancel");
		buttonsPanel.add(cancelButton);
		
		ActionListener buttonListener = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (e.getSource() == saveButton) {
					Save();
					Reload();
					frame.setVisible(false);
				} else if (e.getSource() == revertButton) {
					RevertToDefault();
					Save();
					Reload();
				} else if (e.getSource() == cancelButton) {
					Reload();
					frame.setVisible(false);
				}
				frame.validate();
				SwingUtilities.updateComponentTreeUI(Main.frame);
			}
		};
		
		saveButton.addActionListener(buttonListener);
		revertButton.addActionListener(buttonListener);
		cancelButton.addActionListener(buttonListener);
		
		frame.pack();
	}
	
	private static void Save() {
		prefsFile.Save(Main.pluginIniName, "HotbarVerticalPercentage", (Float.parseFloat(hotbarVerticalInput.getText())/100.0f) + Main.EMPTY_STRING);
		prefsFile.Save(Main.pluginIniName, "ConsoleHorizontalPercentage", (Float.parseFloat(consoleHorizontalInput.getText())/100.0f) + Main.EMPTY_STRING);
		try {
			prefsFile.SaveToFile();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	private static void Reload() {
		try { prefsFile.reloadFile(); } catch (IOException e) {}
		hotbarVerticalInput.setText(HotbarVerticalPercentage()*100 + Main.EMPTY_STRING);
		consoleHorizontalInput.setText(ConsoleHorizontalPercentage()*100 + Main.EMPTY_STRING);
	}
	
	private static void RevertToDefault() {
		consoleHorizontalInput.setText(ConsoleHorizontalPercentage()*100 + Main.EMPTY_STRING);
		consoleHorizontalInput.setText(ConsoleHorizontalPercentage()*100 + Main.EMPTY_STRING);
	}
	
	public static void Show() {
		if (frame.isVisible()) {
			frame.toFront();
			frame.requestFocus();
		} else {
			frame.setVisible(true);
		}
		Reload();
	}
}