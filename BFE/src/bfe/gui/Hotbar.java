package bfe.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import bfe.logic.Interpreter;
import bfe.logic.Main;

public class Hotbar extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ImageButton playB;
	private ImageButton stopB;
	private ImageButton stepB;
	private JTextField timeField;
	private Editor ew;
	private Console c;
	
	public Hotbar(Dimension preferredSize, Editor ew, Console c) {
		super();
		
		this.ew = ew;
		this.c = c;
		
		try {
			playB = new ImageButton("data/images/Play.png");
			playB.SetPressedImage("data/images/Play_pressed.png");
			stopB = new ImageButton("data/images/Stop.png");
			stopB.SetPressedImage("data/images/Stop_pressed.png");
			stepB = new ImageButton("data/images/Step.png");
			stepB.SetPressedImage("data/images/Step_pressed.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		JPanel hotButtonsPane = new JPanel();
		FlowLayout fl = new FlowLayout();
		hotButtonsPane.setLayout(fl);
		this.add(hotButtonsPane, BorderLayout.WEST);
		
		playB.AddClickListener(new ImageButtonListener() { @Override public void Preform() { PlayBPressed(); }});
		hotButtonsPane.add(playB);
		
		// Time input
		timeField = new JTextField();
		timeField.setText("0");
		timeField.setToolTipText("Time to wait until executing next command in milliseconds when executing code");
		timeField.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {  }
			@Override public void keyPressed(KeyEvent e) { updateUI(); }
			@Override public void keyReleased(KeyEvent e) {
				String time = Main.EMPTY_STRING;
				for (int i = 0; i < timeField.getText().length(); i++) {
					char c = timeField.getText().charAt(i);
					if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
						time += c;
					}
				}
				if (time.length() < 1) {
					time = 0 + Main.EMPTY_STRING;
				}
				timeField.setText(time);
				updateUI();
			}
		});
		hotButtonsPane.add(timeField);
		
		stopB.AddClickListener(new ImageButtonListener() { @Override public void Preform() { StopBPressed(); }});
		hotButtonsPane.add(stopB);
		
		stepB.AddClickListener(new ImageButtonListener() { @Override public void Preform() { StepBPressed(); }});
		hotButtonsPane.add(stepB);
		
		// Find
		JPanel frPane = new JPanel();
		frPane.setLayout(new FlowLayout());
		this.add(frPane);
		
		JLabel findLabel = new JLabel("Find:");
		frPane.add(findLabel);
		JTextField findField = new JTextField();
		findField.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) { updateUI(); }
			@Override public void keyReleased(KeyEvent e) { updateUI(); }
		});
		frPane.add(findField);
		JButton findButton = new JButton("Find Next");
		frPane.add(findButton);
		
		this.setBorder(new EtchedBorder());
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension t_buttonSize = new Dimension(getSize().height-10, getSize().height-10);
				playB.setPreferredSize(t_buttonSize);
				stopB.setPreferredSize(t_buttonSize);
				stepB.setPreferredSize(t_buttonSize);
				timeField.setFont(new Font(timeField.getFont().getFontName(), timeField.getFont().getStyle(), (int)(t_buttonSize.height * 0.5f)));
				findLabel.setFont(new Font(findLabel.getFont().getFontName(), findLabel.getFont().getStyle(), (int)(t_buttonSize.height * 0.5f)));
				findField.setFont(new Font(findField.getFont().getName(), findField.getFont().getStyle(), (int)(t_buttonSize.height * 0.5f)));
				findButton.setFont(new Font(findButton.getFont().getName(), findButton.getFont().getStyle(), (int)(t_buttonSize.height * 0.5f)));
				SwingUtilities.updateComponentTreeUI((JPanel) e.getSource());
			}
		});
	}
	
	private Interpreter interpret = null;
	public void PlayBPressed() {
		try {
			if (interpret == null || !interpret.IsRunning()) {
				interpret = new Interpreter(c, ew, Long.parseLong(timeField.getText()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		interpret.Start();
	}
	
	public void StopBPressed() {
		if (interpret != null) {
			if (interpret.SetupSuccessful()) {
				interpret.Stop();
			}
		}
	}
	
	public void StepBPressed() {
		if (interpret != null) {
			if (interpret.SetupSuccessful()) {
				interpret.Step();
			}
		}
	}
}
