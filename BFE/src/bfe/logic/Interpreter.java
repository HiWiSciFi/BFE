package bfe.logic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import bfe.gui.Console;
import bfe.gui.Editor;
import bfe.gui.MemoryInsight;

public class Interpreter {
	
	private int instructionPointer = 0;
	private int dataPointer = 0;
	private ArrayList<Character> data = new ArrayList<Character>();
	private String code = Main.EMPTY_STRING;
	private long delay = 0l;
	
	private Console console;
	private Editor editor;
	
	private boolean running = false;
	public boolean IsRunning() { return running; }
	
	private boolean successful = true;
	public boolean SetupSuccessful() { return successful; }
	
	private Timer timer = new Timer();
	private boolean automaticExecutionRunning = false;
	private Thread currentStep = null;
	private ReentrantLock lock = new ReentrantLock();
	
	public Interpreter(Console console, Editor editor, long delay) {
		this.console = console;
		this.editor = editor;
		this.delay = delay;
		code = editor.GetCode();
		if (code.length() <= 0) { successful = false; return; }
		
		//reset variables
		dataPointer = 0;
		data = new ArrayList<Character>();
		data.add((char) 0);
		
		// validate code brackets
		int openCount = 0;
		int closedCount = 0;
		for (instructionPointer = 0; instructionPointer < code.length(); instructionPointer++) {
			char c = code.charAt(instructionPointer);
			if (c == '[') {
				openCount++;
			} else if (c == ']') {
				closedCount++;
				if (openCount != closedCount) {
					successful = false;
					return;
				}
			}
		}
		if (openCount != closedCount) {
			successful = false;
			return;
		}
		
		instructionPointer = 0;
		editor.SetBlocked(true);
		MemoryInsight.Clear();
		
		running = true;
		StartAutomaticExecution();
	}
	
	private class QueuedStep extends TimerTask {
		@Override
		public void run() {
			if (currentStep == null || !currentStep.isAlive()) {
				Step();
			}
			lock.lock();
			try {
				timer.schedule(new QueuedStep(), delay);
			} catch(Exception e) {}
			lock.unlock();
		}
	}
	
	private void StartAutomaticExecution() {
		if (!automaticExecutionRunning) {
			lock.lock();
			try {
				timer = new Timer();
				timer.schedule(new QueuedStep(), delay);
			} catch(Exception e) {}
			lock.unlock();
			automaticExecutionRunning = true;
		}
	}
	
	private void StopAutomaticExecution() {
		if (automaticExecutionRunning) {
			lock.lock();
			try {
				timer.cancel();
				timer.purge();
			} catch(Exception e) {}
			lock.unlock();
			automaticExecutionRunning = false;
		}
	}
	
	private void SetCursorPos(int pos) {
		StringBuffer toPush = new StringBuffer(code);
		toPush.insert(pos, Main.EXEC_INDICATOR);
		editor.SetContent(toPush.toString());
	}
	
	public void Start() {
		StartAutomaticExecution();
		running = true;
	}
	
	public void Stop() {
		lock.lock();
		try {
			timer.cancel();
			timer.purge();
		} catch(Exception e) {}
		lock.unlock();
		editor.SetContent(code);
		editor.SetBlocked(false);
		running = false;
	}
	
	public void Step() {
		if (currentStep == null || !currentStep.isAlive()) {
			(currentStep = new ThreadedStep()).start();
		}
	}
	
	private class ThreadedStep extends Thread {
		@Override
		public void run() {
			NextThreadedStep();
		}
		
		private void NextThreadedStep() {
			boolean done = true;
			do {
				done = true;
				SetCursorPos(instructionPointer+1);
				char instruction = code.charAt(instructionPointer);
				if (instruction == '+') {
					// + increment cell by one
					data.set(dataPointer, (char) (data.get(dataPointer).charValue() + 1));
					MemoryInsight.SetMemoryCell(dataPointer, data.get(dataPointer).charValue());
					
				} else if (instruction == '-') {
					// - decrement cell by one
					data.set(dataPointer, (char) (data.get(dataPointer).charValue() - 1));
					MemoryInsight.SetMemoryCell(dataPointer, data.get(dataPointer).charValue());
					
				} else if (instruction == '<') {
					// < decrement dataPointer by one
					dataPointer--;
					if (dataPointer < 0) {
						return;
					}
					MemoryInsight.SelectMemoryCell(dataPointer);
					
				} else if (instruction == '>') {
					// > increment dataPointer by one
					dataPointer++;
					if (dataPointer >= data.size()) {
						data.add((char) 0);
						MemoryInsight.AddMemoryCell();
					}
					MemoryInsight.SelectMemoryCell(dataPointer);
					
				} else if (instruction == '.') {
					// . output current cell as ASCII
					System.out.print(data.get(dataPointer).toString());
					console.Print(data.get(dataPointer).toString());
					
				} else if (instruction == ',') {
					// , wait for input (1 byte) and write to current cell (OVERWRITE)
					while (!console.InputAvailable());
					data.set(dataPointer, console.GetLastInput());
					
				} else if (instruction == '[') {
					// [ if current cell = 0 jump to corresponding ]+1
					if (data.get(dataPointer) == 0) {
						// jump to corresponding ] -->
						instructionPointer++;
						int currDex = 0;
						while (currDex > -1) {
							char c = code.charAt(instructionPointer);
							if (c == '[') {
								currDex++;
							} else if (c == ']') {
								currDex--;
							}
							instructionPointer++;
						}
					}
					
				} else if (instruction == ']') {
					// ] if current cell != 0 jump to corresponding [+1
					if (data.get(dataPointer) != 0) {
						// jump to corresponding [ <--
						instructionPointer--;
						int currDex = 0;
						while (currDex > -1) {
							char c = code.charAt(instructionPointer);
							if (c == '[') {
								currDex--;
							} else if (c == ']') {
								currDex++;
							}
							instructionPointer--;
						}
					}
					
				} else if (instruction == Main.BREAKPOINT_CHARACTER) {
					StopAutomaticExecution();
				} else {
					done = false;
				}
				
				instructionPointer++;
				if (instructionPointer >= code.length()) {
					Stop();
					return;
				}
			} while (!done);
			
			return;
		}
	}
}