package bfe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bfe.logic.Main;

public class Console extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JScrollPane outputPane;
	private JTextArea output = new JTextArea();
	private JTextField input = new JTextField();
	
	private boolean inputAvailable = false;
	private char inputVal = 10;
	
	private ReentrantLock lock = new ReentrantLock();
	
	public Console() {
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		output.setEditable(false);
		output.setFont(new Font("monospaced", Font.BOLD, 20));
		outputPane = new JScrollPane(output);
		this.add(outputPane, BorderLayout.CENTER);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new BorderLayout());
		input.setFont(new Font("monospaced", Font.PLAIN, 20));
		inputPane.add(input, BorderLayout.CENTER);
		JButton clearButton = new JButton("clear");
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Clear();
			}
		});
		inputPane.add(clearButton, BorderLayout.EAST);
		
		this.add(inputPane, BorderLayout.SOUTH);
		
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
		
		input.addKeyListener(new KeyAdapter() {
		    public void keyPressed(KeyEvent e) { adjustContent(e); }
		    public void keyReleased(KeyEvent e) { adjustContent(e); }
		    private void adjustContent(KeyEvent e) {
		        String text = input.getText();
		        if (text.length() > 0) {
		        	input.setText(text.charAt(0) + Main.EMPTY_STRING);
		        } else {
		        	input.setText(Main.EMPTY_STRING);
		        }
		    }
		});
		
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (input.getText().length() < 1) {
					input.setText((char)10 + Main.EMPTY_STRING);
				}
				inputVal = input.getText().charAt(0);
				input.setText(Main.EMPTY_STRING);
				inputAvailable = true;
			}
		});
	}
	
	public void SetOutputBackground(Color c) {
		output.setBackground(c);
	}
	
	public void SetOutputForeground(Color c) {
		output.setForeground(c);
	}
	
	public void SetOutputColorScheme(Color background, Color foreground) {
		SetOutputBackground(background);
		SetOutputForeground(foreground);
	}
	
	public void Clear() {
		output.setText(Main.EMPTY_STRING);
	}
	
	public void Print(Object str) {
		output.append(str.toString());
	}
	
	public void Println(Object str) {
		output.append(str.toString() + "\r\n");
	}
	
	public boolean InputAvailable() {
		lock.lock();
		boolean toReturn = inputAvailable;
		lock.unlock();
		return toReturn;
	}
	
	public char GetLastInput() {
		lock.lock();
		inputAvailable = false;
		lock.unlock();
		return inputVal;
	}
}