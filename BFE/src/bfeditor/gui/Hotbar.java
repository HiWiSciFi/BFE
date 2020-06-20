package bfeditor.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import bfeditor.logic.Interpreter;
import bfeditor.logic.Main;

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
			@Override public void keyReleased(KeyEvent e) { updateUI(); }
		});
		hotButtonsPane.add(timeField);
		
		stopB.AddClickListener(new ImageButtonListener() { @Override public void Preform() { StopBPressed(); }});
		hotButtonsPane.add(stopB);
		
		stepB.AddClickListener(new ImageButtonListener() { @Override public void Preform() { StepBPressed(); }});
		hotButtonsPane.add(stepB);
		
		// Find/Replace
		JPanel frPane = new JPanel();
		FlowLayout frfl = new FlowLayout();
		frPane.setLayout(frfl);
		this.add(frPane);
		
		frPane.add(new JLabel("Find:"));
		JTextField findField = new JTextField();
		frPane.add(findField);
		
		frPane.add(new JLabel("Replace:"));
		
		JButton findButton = new JButton();
		frPane.add(findButton);
		JButton replaceButton = new JButton();
		frPane.add(replaceButton);
		
		this.setBorder(new EtchedBorder());
		
		JPanel p = this;
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension t_buttonSize = new Dimension(getSize().height-10, getSize().height-10);
				playB.setPreferredSize(t_buttonSize);
				stopB.setPreferredSize(t_buttonSize);
				stepB.setPreferredSize(t_buttonSize);
				//updateUI();
				SwingUtilities.updateComponentTreeUI(p);
			}
		});
	}
	
	// +++++[->+++++<]+++++[->++<]>.
	Interpreter interpret = null;
	public void PlayBPressed() {
		System.out.println("Executing code");
		try {
			interpret = new Interpreter(c, ew);
			System.out.println(ew.GetCode());
			if (interpret.ExecuteCode(ew.GetCode()) == 0) {
				/*while (interpret.NextStep() != 1) {
					Thread.sleep(1000);
				}*/
				
				String timeText = timeField.getText();
				String time = Main.EMPTY_STRING;
				for (int i = 0; i < timeText.length(); i++) {
					char c = timeText.charAt(i);
					if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
						time += c;
					}
				}
				if (time.length() < 1) {
					time = "0";
				}
				timeField.setText(time);
				interpret.delay = Long.parseLong(time);
				interpret.tt.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void StopBPressed() {
		interpret.StopExecuting();
	}
	
	public void StepBPressed() {
		if (interpret != null) {
			interpret.NextStep();
		}
	}
}
