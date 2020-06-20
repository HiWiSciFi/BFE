package bfeditor.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import bfeditor.logic.Main;

public class FileSystem {
	
	private static File currFile = null;
	
	public static void NewFile(JFrame context) {
		currFile = null;
		context.setTitle(Main.TITLE_BASE);
	}
	
	public static void Save(JFrame context, String data) throws IOException  {
		if (currFile == null) {
			SaveAsDialog(context, data);
		} else {
			SaveAs(currFile, data);
		}
	}
	
	public static void SaveAsDialog(JFrame context, String data) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify save location");
		if (currFile == null) {
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setCurrentDirectory(new File(currFile.getAbsolutePath()));
		}

		int userSelection = fileChooser.showSaveDialog(context);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			try {
				SaveAs(fileToSave, data);
				context.setTitle(Main.TITLE_BASE + " \"" + currFile.getAbsolutePath() + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void SaveAs(File file, String data) throws IOException {
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.close();
		fw.close();
		currFile = file;
	}
	
	public static String OpenDialog(JFrame context) throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify file to open");
		if (currFile == null) {
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setCurrentDirectory(new File(currFile.getAbsolutePath()));
		}
		int result = fileChooser.showOpenDialog(context);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			currFile = selectedFile;
			context.setTitle(Main.TITLE_BASE + " \"" + selectedFile.getAbsolutePath() + "\"");
			return Open(selectedFile);
		}
		return Main.NOTHING_SELECTED;
	}
	
	private static String Open(File file) throws IOException {
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