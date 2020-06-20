package bfeditor.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import bfeditor.io.FileSystem;
import bfeditor.logic.Main;

public class MenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JMenuItem file_new;
	private JMenuItem file_open;
	private JMenuItem file_save;
	private JMenuItem file_saveas;

	private JMenuItem preferences;

	private JMenuItem openDbg;
	private JMenuItem addBrkP;
	private JMenuItem rmAllBrkP;

	private JMenuItem asciiTable;
	private JMenuItem learn;
	private JMenuItem about;

	private Editor ew;
	
	public MenuBar(Editor ew) {
		super();
		
		this.ew = ew;
		
		JMenu fileMenu = new JMenu("File");
		file_new = new JMenuItem("New");
		file_new.addActionListener(this);
		file_new.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileMenu.add(file_new);
		file_open = new JMenuItem("Open");
		file_open.addActionListener(this);
		file_open.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(file_open);
		file_save = new JMenuItem("Save");
		file_save.addActionListener(this);
		file_save.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(file_save);
		file_saveas = new JMenuItem("Save As");
		file_saveas.addActionListener(this);
		file_saveas.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		fileMenu.add(file_saveas);
		this.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		preferences = new JMenuItem("Preferences");
		preferences.addActionListener(this);
		editMenu.add(preferences);
		this.add(editMenu);

		JMenu debugMenu = new JMenu("Debug");
		openDbg = new JMenuItem("Memory Insight");
		openDbg.addActionListener(this);
		openDbg.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		debugMenu.add(openDbg);
		addBrkP = new JMenuItem("Add Breakpoint");
		addBrkP.addActionListener(this);
		debugMenu.add(addBrkP);
		rmAllBrkP = new JMenuItem("Remove all Breakpoints");
		rmAllBrkP.addActionListener(this);
		debugMenu.add(rmAllBrkP);
		this.add(debugMenu);

		JMenu helpMenu = new JMenu("Help");
		asciiTable = new JMenuItem("ASCII Table");
		asciiTable.addActionListener(this);
		asciiTable.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_A, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		helpMenu.add(asciiTable);
		learn = new JMenuItem("Learn BF");
		learn.addActionListener(this);
		helpMenu.add(learn);
		about = new JMenuItem("About");
		about.addActionListener(this);
		helpMenu.add(about);
		this.add(helpMenu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object trigger = e.getSource();
		if (trigger == preferences) {
			Preferences.Show();
		} else if (trigger == openDbg) {
			MemoryInsight.Show();
		} else if (trigger == asciiTable) {
			ASCIITable.Show();
		} else if (trigger == about) {
			About.Show();
		} else if (trigger == learn) {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Brainfuck"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		} else if (trigger == file_saveas) {
			FileSystem.SaveAsDialog((JFrame) SwingUtilities.getWindowAncestor(this), ew.GetCode());
		} else if (trigger == file_save) {
			try {
				FileSystem.Save((JFrame) SwingUtilities.getWindowAncestor(this), ew.GetCode());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (trigger == file_new) {
			FileSystem.NewFile((JFrame) SwingUtilities.getWindowAncestor(this));
			ew.SetContent(Main.EMPTY_STRING);
		} else if (trigger == file_open) {
			try {
				String result = FileSystem.OpenDialog((JFrame) SwingUtilities.getWindowAncestor(this));
				if (result != Main.NOTHING_SELECTED) {
					ew.SetContent(result);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (trigger == addBrkP) {
			ew.AddCharAtCursor(Main.BREAKPOINT_CHARACTER);
		} else if (trigger == rmAllBrkP) {
			StringBuilder content = new StringBuilder();
			content.append(ew.GetCode());
			for (int i = content.length()-1; i >= 0; i--) {
				char curr = content.charAt(i);
				if (curr == Main.BREAKPOINT_CHARACTER) {
					content.deleteCharAt(i);
				}
			}
			ew.SetContent(content.toString());
		}
	}
}
