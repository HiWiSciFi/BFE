package bfe.gui;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MemoryInsight {
	
	public static MemoryInsight instance;
	
	private JFrame frame;
	private JPanel panel;
	
	private static int width = 300;
	private static int height = 300;
	
	private ArrayList<MemoryCell> cells = new ArrayList<MemoryCell>();
	
	public MemoryInsight() {
		if (instance == null) {
			instance = this;
		} else {
			return;
		}
		frame = new JFrame("Memory");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(true);
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setLocation(screenCenter.x - (width/2), screenCenter.y - (height/2));
		frame.setSize(width, height);
		panel = new JPanel();
		frame.add(panel);
		Clear();
	}
	
	public void Show() {
		if (frame.isVisible()) {
			frame.toFront();
			frame.requestFocus();
		} else {
			frame.setVisible(true);
		}
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		frame.pack();
	}
	
	public void Clear() {
		panel.removeAll();
		cells.clear();
		AddMemoryCell();
		SelectMemoryCell(cells.size()-1);
	}
	
	public void AddMemoryCell() {
		MemoryCell mc = new MemoryCell();
		mc.SetValue(0);
		panel.add(mc);
		cells.add(mc);
		frame.pack();
		frame.repaint();
	}
	
	public void SetMemoryCell(int index, int value) {
		MemoryCell element = cells.get(index);
		element.SetValue(value);
		cells.set(index, element);
		frame.pack();
		frame.repaint();
	}
	
	public void SelectMemoryCell(int index) {
		for (int i = 0; i < cells.size(); i++) {
			MemoryCell mc = cells.get(i);
			if (i == index) {
				mc.Select();
			} else {
				mc.Unselect();
			}
			cells.set(i, mc);
		}
		frame.pack();
		frame.repaint();
	}
}
