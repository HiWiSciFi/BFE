package bfe.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import bfe.logic.Main;

public class MemoryCell extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel hex;
	private JLabel dec;

	public MemoryCell() {
		super();
		
		BoxLayout gl;
		gl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(gl);

		Font font = new Font("monospaced", Font.PLAIN, 20);

		hex = new JLabel();
		hex.setAlignmentX(CENTER_ALIGNMENT);
		hex.setFont(font);
		this.add(hex);
		dec = new JLabel();
		dec.setAlignmentX(CENTER_ALIGNMENT);
		dec.setFont(font);
		this.add(dec);

		this.setSize(new Dimension(50, 50));
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		SetValue(0);
		Unselect();
	}

	public void SetValue(int value) {
		hex.setText("#" + FormatHex(Integer.toHexString(value)));
		dec.setText(value + Main.EMPTY_STRING);
		repaint();
	}

	private String FormatHex(String hex) {
		switch (hex.length()) {
		case 0:
			return "000";
		case 1:
			return "00" + hex;
		case 2:
			return "0" + hex;
		default:
			return hex;
		}
	}

	public void SetValue(String value) {
		hex.setText("#" + FormatHex(value));
		dec.setText(Integer.parseInt(value, 16) + Main.EMPTY_STRING);
		repaint();
	}
	
	private static final Color unselectedFgColor = new Color(0, 0, 0);
	private static final Color unselectedBgColor = new Color(238, 238, 238);
	
	private static final Color selectedFgColor = new Color(0, 0, 0);
	private static final Color selectedBgColor = new Color(255, 255, 0);
	
	public void Select() {
		setBackground(selectedBgColor);
		setForeground(selectedFgColor);
		repaint();
	}
	
	public void Unselect() {
		setBackground(unselectedBgColor);
		setForeground(unselectedFgColor);
		repaint();
	}
}
