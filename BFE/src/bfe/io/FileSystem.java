package bfe.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import bfe.logic.Main;

public class FileSystem {
	
	public static FileSystem instance;
	
	public FileSystem() {
		if (instance == null) {
			instance = this;
		} else {
			return;
		}
	}
	
	private File currFile = null;
	
	public void NewFile() {
		currFile = null;
		Main.frame.setTitle(Main.TITLE_BASE);
	}
	
	public void Save(String data) throws IOException  {
		if (currFile == null) {
			SaveAsDialog(data);
		} else {
			SaveAs(currFile, data);
		}
	}
	
	public void SaveAsDialog(String data) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify save location");
		if (currFile == null) {
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setCurrentDirectory(new File(currFile.getAbsolutePath()));
		}

		int userSelection = fileChooser.showSaveDialog(Main.frame);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			try {
				SaveAs(fileToSave, data);
				Main.frame.setTitle(Main.TITLE_BASE + " \"" + currFile.getAbsolutePath() + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void SaveAs(File file, String data) throws IOException {
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.close();
		fw.close();
		currFile = file;
	}
	
	public String OpenDialog() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify file to open");
		if (currFile == null) {
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setCurrentDirectory(new File(currFile.getAbsolutePath()));
		}
		int result = fileChooser.showOpenDialog(Main.frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			currFile = selectedFile;
			Main.frame.setTitle(Main.TITLE_BASE + " \"" + selectedFile.getAbsolutePath() + "\"");
			return Open(selectedFile);
		}
		return Main.NOTHING_SELECTED;
	}
	
	private String Open(File file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line + "\r\n");
		}
		br.close();
		fr.close();
		return sb.toString();
	}
}