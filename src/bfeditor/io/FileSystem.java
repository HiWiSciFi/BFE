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
	
	public static void newFile(JFrame context) {
		currFile = null;
		context.setTitle(Main.TITLE_BASE);
	}
	
	public static void save(JFrame context, String data) throws IOException  {
		if (currFile == null) {
			saveAsDialog(context, data);
		} else {
			saveAs(currFile, data);
		}
	}
	
	public static void saveAsDialog(JFrame context, String data) {
		// File file, String data
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");

		int userSelection = fileChooser.showSaveDialog(context);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			try {
				saveAs(fileToSave, data);
				context.setTitle(Main.TITLE_BASE + "\"" + currFile.getAbsolutePath() + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void saveAs(File file, String data) throws IOException {
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.close();
		fw.close();
		currFile = file;
	}
	
	public static String openDialog(JFrame context) throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		if (currFile == null) {
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setCurrentDirectory(new File(currFile.getAbsolutePath()));
		}
		int result = fileChooser.showOpenDialog(context);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			currFile = selectedFile;
			context.setTitle(Main.TITLE_BASE + "\"" + selectedFile.getAbsolutePath() + "\"");
			return open(selectedFile);
		}
		return Main.NOTHING_SELECTED;
	}
	
	private static String open(File file) throws IOException {
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