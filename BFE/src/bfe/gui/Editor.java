package bfe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import bfe.logic.Main;

public class Editor extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextArea jta;

	public Editor() {
		super();

		this.setLayout(new BorderLayout());

		jta = new JTextArea();
		jta.setText(Main.EMPTY_STRING);
		jta.setBackground(new Color(36, 33, 42));
		jta.setForeground(new Color(255, 69, 0));

		jta.setFont(new Font("monospaced", Font.BOLD, 20));
		jta.setCaretColor(new Color(255, 255, 255));
		JScrollPane scrollPane = new JScrollPane(jta);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setBorder(new EtchedBorder());

		jta.addMouseWheelListener(mouseWheelEvent -> {
			if (mouseWheelEvent.isControlDown()) {
				int scrolled = mouseWheelEvent.getUnitsToScroll();
				Font font = jta.getFont();
				int fontSize = font.getSize();
				fontSize += -(scrolled / 3);
				Font newFont = new Font(font.getFontName(), font.getStyle(), fontSize);
				jta.setFont(newFont);
			} else {
				jta.getParent().dispatchEvent(mouseWheelEvent);
			}
		});
	}

	public void SetContent(String content) {
		jta.setText(content);
	}

	public void SetBlocked(boolean state) {
		jta.setEditable(!state);
	}

	public String GetCode() {
		return jta.getText();
	}

	public void AddCharAtCursor(char toAdd) {
		jta.insert(toAdd + Main.EMPTY_STRING, jta.getCaretPosition());
	}

	public void HighlightString(String query) {
		Highlighter highlighter = jta.getHighlighter();
		highlighter.removeAllHighlights();
		if (query.length() < 1) { return; }
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
		
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < jta.getText().length(); i++) {
			int p = jta.getText().indexOf(query, i);
			if (p == -1) { break; }
			indices.add(p);
			i = p+1;
		}
		
		try {
			for (int i = 0; i < indices.size(); i++) {
				highlighter.addHighlight(indices.get(i), indices.get(i)+query.length(), painter);
			}
		} catch (BadLocationException e) {}
	}
}
