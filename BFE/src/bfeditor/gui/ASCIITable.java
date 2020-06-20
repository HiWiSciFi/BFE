package bfeditor.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bfeditor.logic.Main;

public class ASCIITable {
	
	private static JFrame frame;
	private static final int width = 300;
	private static final int height = 500;
	
	private static JTextField inputField = new JTextField();
	private static JLabel queueResult = new JLabel();
	
	private static final String asciiTableStart = "decimal\tASCII\r\n"
			+ "-----------------------------------------------------\r\n"
			+ "0\t(null)\r\n"
			+ "1\t(start of heading)\r\n"
			+ "2\t(start of text)\r\n"
			+ "3\t(end of text)\r\n"
			+ "4\t(end of transmission)\r\n"
			+ "5\t(enquiry)\r\n"
			+ "6\t(acknowledge)\r\n"
			+ "7\t(bell)\r\n"
			+ "8\t(backspace)\r\n"
			+ "9\t(horizontal tab)\r\n"
			+ "10\t(NL line feed, new line)\r\n"
			+ "11\t(vertical tab)\r\n"
			+ "12\t(NP form feed, new page)\r\n"
			+ "13\t(carriage return)\r\n"
			+ "14\t(shift out)\r\n"
			+ "15\t(shift in)\r\n"
			+ "16\t(data link escape)\r\n"
			+ "17\t(device control 1)\r\n"
			+ "18\t(device control 2)\r\n"
			+ "19\t(device control 3)\r\n"
			+ "20\t(device control 4)\r\n"
			+ "21\t(negative acknowledge)\r\n"
			+ "22\t(synchronous idle)\r\n"
			+ "23\t(end of trans. block)\r\n"
			+ "24\t(cancel)\r\n"
			+ "25\t(end of medium)\r\n"
			+ "26\t(substitute)\r\n"
			+ "27\t(escape)\r\n"
			+ "28\t(file separator)\r\n"
			+ "29\t(group sepatator)\r\n"
			+ "30\t(record separator)\r\n"
			+ "31\t(unit separator)\r\n"
			+ "32\tSPACE\r\n";
	private static final String asciiTableEnd = "127\tDEL";
	
	public static void Init() {
		frame = new JFrame("ASCII Table");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(true);
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setLocation(screenCenter.x - (width/2), screenCenter.y - (height/2));
		frame.setSize(width, height);
		frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		frame.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 2));
		
		inputField.addKeyListener(new KeyAdapter() {
		    public void keyPressed(KeyEvent e) { adjustContent(e); }
		    public void keyReleased(KeyEvent e) { adjustContent(e); }
		    private void adjustContent(KeyEvent e) {
		    	JTextField textField = (JTextField) e.getSource();
		        String text = textField.getText();
		        if (text.length() > 0) {
		        	textField.setText(text.charAt(0) + Main.EMPTY_STRING);
		        	queueResult.setText((int)text.charAt(0) + Main.EMPTY_STRING);
		        } else {
		        	queueResult.setText(Main.EMPTY_STRING);
		        }
		    }
		});
		topPanel.add(inputField);
		topPanel.add(queueResult);
		panel.add(topPanel, BorderLayout.NORTH);
		
		JTextArea jta = new JTextArea();
		jta.setEditable(false);
		StringBuilder tableBuilder = new StringBuilder();
		tableBuilder.append(asciiTableStart);
		for (int i = 33; i < 127; i++) {
			tableBuilder.append(i + "\t" + (char)(i) + "\r\n");
		}
		tableBuilder.append(asciiTableEnd);
		jta.setText(tableBuilder.toString());
		JScrollPane jsp = new JScrollPane(jta);
		panel.add(jsp, BorderLayout.CENTER);
	}
	
	public static void Show() {
		if (frame.isVisible()) {
			frame.toFront();
			frame.requestFocus();
		} else {
			frame.setVisible(true);
		}
	}
}