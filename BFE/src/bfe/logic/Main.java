package bfe.logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import bfe.gui.ASCIITable;
import bfe.gui.About;
import bfe.gui.Console;
import bfe.gui.Editor;
import bfe.gui.Hotbar;
import bfe.gui.MemoryInsight;
import bfe.gui.MenuBar;
import bfe.gui.Preferences;
import bfe.io.IniLoader;

public class Main {
	
	public static final String VERSION = "1.1 INDEV";
	public static final String NOTHING_SELECTED = "8e3242f8-23f5-4c28-b25b-ab802725d71a";
	public static final char BREAKPOINT_CHARACTER = '!';
	public static final String EMPTY_STRING = "";
	public static final String EXEC_INDICATOR = "|";
	public static final String TITLE_BASE = "Brainf*** Editor";

	private static JFrame frame;
	
	private static int width = 800;
	private static int height = 500;

	private static IniLoader prefsFile;
	private static final String pluginIniName = "BFE";

	public static void main(String[] args) {
		Preferences.Init();
		MemoryInsight.Init();
		ASCIITable.Init();
		About.Init();

		try {
			prefsFile = new IniLoader("data/prefs.ini");
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		frame = new JFrame(TITLE_BASE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setLocation(screenCenter.x - (width / 2), screenCenter.y - (height / 2));
		frame.setSize(new Dimension(width, height));

		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(new File("data/images/icon/16.png")));
			icons.add(ImageIO.read(new File("data/images/icon/32.png")));
			icons.add(ImageIO.read(new File("data/images/icon/48.png")));
			icons.add(ImageIO.read(new File("data/images/icon/64.png")));
			icons.add(ImageIO.read(new File("data/images/icon/96.png")));
			icons.add(ImageIO.read(new File("data/images/icon/128.png")));
			icons.add(ImageIO.read(new File("data/images/icon/256.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		frame.setIconImages(icons);

		BorderLayout MainLayout = new BorderLayout();
		frame.setLayout(MainLayout);

		Editor ew = new Editor();
		Console c = new Console();
		c.SetOutputColorScheme(Color.black, Color.white);
		Hotbar hotbar = new Hotbar(
				new Dimension(width, (int) (height * prefsFile.ReadSingle(pluginIniName, "HotbarVerticalPercentage"))),
				ew, c);
		
		MenuBar menuBar = new MenuBar(ew);
		frame.setJMenuBar(menuBar);

		frame.add(ew, BorderLayout.CENTER);
		frame.add(c, BorderLayout.EAST);
		frame.add(hotbar, BorderLayout.SOUTH);

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				width = frame.getWidth();
				height = frame.getHeight();
				c.setPreferredSize(new Dimension(
						(int) (width * prefsFile.ReadSingle(pluginIniName, "ConsoleHorizontalPercentage")),
						height - ((int) (height * prefsFile.ReadSingle(pluginIniName, "HotbarVerticalPercentage")))));
				hotbar.setPreferredSize(new Dimension(width,
						(int) (height * prefsFile.ReadSingle(pluginIniName, "HotbarVerticalPercentage"))));
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		frame.setVisible(true);
	}
}