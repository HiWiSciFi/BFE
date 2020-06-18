package bfeditor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Console extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JScrollPane outputPane;
	JTextArea output = new JTextArea();
	JTextField input = new JTextField();
	
	public Console() {
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		output.setEditable(false);
		output.setFont(new Font("monospaced", Font.BOLD, 20));
		outputPane = new JScrollPane(output);
		this.add(outputPane, BorderLayout.CENTER);
		
		input.setFont(new Font("monospaced", Font.PLAIN, 20));
		this.add(input, BorderLayout.SOUTH);
		
		output.addMouseWheelListener(mouseWheelEvent ->
		{
		    if (mouseWheelEvent.isControlDown())
		    {
		        int scrolled = mouseWheelEvent.getUnitsToScroll();
		        Font font = output.getFont();
		        int fontSize = font.getSize();
		        fontSize += -(scrolled / 3);
		        Font newFont = new Font(font.getFontName(), font.getStyle(), fontSize);
		        output.setFont(newFont);
		        input.setFont(new Font(newFont.getFontName(), Font.PLAIN, fontSize));
		        updateUI();
		    }
		     else
		    {
		    	 output.getParent().dispatchEvent(mouseWheelEvent);
		    }
		});
	}
	
	public void setOutputBackground(Color c) {
		output.setBackground(c);
	}
	
	public void setOutputForeground(Color c) {
		output.setForeground(c);
	}
	
	public void setOutputColorScheme(Color background, Color foreground) {
		setOutputBackground(background);
		setOutputForeground(foreground);
	}
	
	public void clear() {
		output.setText("");
	}
	
	public void print(String str) {
		output.append(str);
	}
	
	public void println(String str) {
		output.append(str + "\n");
	}
}

/*
package bfeditor.gui;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class ConsoleWindow extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JTextArea consoleOut;
	JTextField jtf;
	
	public ConsoleWindow() {
		super();
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		Console console = new Console();
		console.setOutputBackground(Color.black);
		console.setOutputForeground(Color.white);
		this.add(console, BorderLayout.CENTER);
		this.setBorder(new EtchedBorder());
	}
	
	public void Log(String message) {
		consoleOut.append(message);
	}
}

*/