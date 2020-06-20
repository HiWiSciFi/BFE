package bfeditor.logic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bfeditor.gui.Console;
import bfeditor.gui.MemoryInsight;
import bfeditor.gui.Editor;

public class Interpreter {
	
	private int instructionPointer = 0;
	private int dataPointer = 0;
	private ArrayList<Character> data = new ArrayList<Character>();
	private String code = " ";
	public long delay = 0l;
	private Console c;
	private Editor e;
	
	private class queuedStep extends TimerTask {
		@Override
		public void run() {
			if (nextStep() == 0) {
				timer.schedule(new queuedStep(), delay);
			} else {
				stopExecuting();
			}
		}
	};
	
	public TimerTask tt = new queuedStep();
	public Timer timer = new Timer();
	
	public Interpreter(Console c, Editor e) {
		this.c = c;
		this.e = e;
	}
	
	public void stopExecuting() {
		timer.cancel();
		timer.purge();
		e.setContent(code);
		e.setBlocked(false);
	}
	
	public int executeCode(String code) {
		if (code.length() < 1) {
			return 2;
		}
		//reset variables
		dataPointer = 0;
		data = new ArrayList<Character>();
		data.add((char) 0);
		this.code = code;
		
		// validate code brackets
		int openCount = 0;
		int closedCount = 0;
		for (instructionPointer = 0; instructionPointer < code.length(); instructionPointer++) {
			char c = code.charAt(instructionPointer);
			if (c == '[') {
				openCount++;
			} else if (c == ']') {
				closedCount++;
				if (openCount > closedCount) {
					return 1;
				}
			}
		}
		if (openCount != closedCount) {
			return 1;
		}
		
		instructionPointer = 0;
		e.setBlocked(true);
		MemoryInsight.clear();
		return 0;
	}
	
	private void setCursorPosition(int pos) {
		StringBuffer toPush = new StringBuffer(code);
		toPush.insert(pos, Main.EXEC_INDICATOR);
		e.setContent(toPush.toString());
	}
	
	public int nextStep() {
		boolean done = true;
		do {
			done = true;
			setCursorPosition(instructionPointer+1);
			char instruction = code.charAt(instructionPointer);
			if (instruction == '+') {
				// + increment cell by one
				data.set(dataPointer, (char) (data.get(dataPointer).charValue() + 1));
				MemoryInsight.setMemoryCell(dataPointer, data.get(dataPointer).charValue());
				
			} else if (instruction == '-') {
				// - decrement cell by one
				data.set(dataPointer, (char) (data.get(dataPointer).charValue() - 1));
				MemoryInsight.setMemoryCell(dataPointer, data.get(dataPointer).charValue());
				
			} else if (instruction == '<') {
				// < decrement dataPointer by one
				dataPointer--;
				if (dataPointer < 0) {
					return 2;
				}
				MemoryInsight.selectMemoryCell(dataPointer);
				
			} else if (instruction == '>') {
				// > increment dataPointer by one
				dataPointer++;
				if (dataPointer >= data.size()) {
					data.add((char) 0);
					MemoryInsight.addMemoryCell();
				}
				MemoryInsight.selectMemoryCell(dataPointer);
				
			} else if (instruction == '.') {
				// . output current cell as ASCII
				System.out.print(data.get(dataPointer));
				c.print(data.get(dataPointer).toString());
				System.out.print("[" + (int)data.get(dataPointer) + "]");
				
			} else if (instruction == ',') {
				// , wait for input (1 byte) and write to current cell (OVERWRITE)
				
				
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
						char c = code.charAt(instructionPointer); // +++++[->+++++<]+++++[->++<]>.
						if (c == '[') {
							currDex--;
						} else if (c == ']') {
							currDex++;
						}
						instructionPointer--;
					}
				}
				
			} else {
				done = false;
			}
			
			instructionPointer++;
			if (instructionPointer >= code.length()) {
				stopExecuting();
				return 1;
			}
		} while (!done);
		
		return 0;
	}
}