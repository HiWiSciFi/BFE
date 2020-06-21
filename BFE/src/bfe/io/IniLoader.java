package bfe.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * .ini system
 * 
 * [MyApp]
 * var1 = "foo"
 * var2 = "bar"
 * 
 * [MyPlugin]
 * var1 = "qwe"
*/

public class IniLoader {
	
	private String path;
	private String[] fileContents;
	
	private ArrayList<ArrayList<String>> valuesToSave = new ArrayList<ArrayList<String>>();
	
	public IniLoader(String path) throws IOException {
		this.path = path;
		FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        int currPlID = 0;
        valuesToSave.clear();
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
            if (line.startsWith("[") && line.contains("]")) {
            	StringBuilder sb = new StringBuilder();
            	for (int i = 1; i < line.length(); i++) {
            		if (line.charAt(i) == ']') {
            			break;
            		} else {
            			sb.append(line.charAt(i));
            		}
            	}
            	valuesToSave.add(new ArrayList<String>() {private static final long serialVersionUID = 1L;{add(sb.toString());}});
            	currPlID = valuesToSave.size()-1;
            } else {
            	valuesToSave.get(currPlID).add(line);
            }
        }
        bufferedReader.close();
        fileContents = lines.toArray(new String[lines.size()]);
	}
	
	public int ReadInt(String plugin, String varname) {
		return Integer.parseInt(GetVarVal(plugin, varname));
	}
	
	public float ReadSingle(String plugin, String varname) {
		return Float.parseFloat(GetVarVal(plugin, varname));
	}
	
	public String ReadString(String plugin, String varname) {
		return GetVarVal(plugin, varname);
	}
	
	private String GetVarVal(String plugin, String varname) {
		String currPlugin = null;
		for (int s = 0; s < fileContents.length; s++) {
			
			if (fileContents[s].startsWith("[")) {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < fileContents[s].length(); i++) {
					if (fileContents[s].charAt(i) == 93) { // ascii: 93 = ]
						break;
					} else {
						sb.append(fileContents[s].charAt(i));
					}
				}
				currPlugin = sb.toString();
			}
			if (fileContents[s].startsWith(varname + " = ") && currPlugin.equals(plugin)) {
				for (int i = 0; i < fileContents[s].length(); i++) {
					if (fileContents[s].charAt(i) == 34) { // ascii: 34 = "
						StringBuilder sb = new StringBuilder();
						for (int m = i+1; m < fileContents[s].length(); m++) {
							if (fileContents[s].charAt(m) == 34) {
								break;
							} else {
								sb.append(fileContents[s].charAt(m));
							}
						}
						return sb.toString();
					}
				}
			}
		}
		return null;
	}
	
	public void Save(String plugin, String varname, String value) {
		int pluginID = -1;
		for (int i = 0; i < valuesToSave.size(); i++) {
			if (valuesToSave.get(i).get(0) == plugin) {
				pluginID = i;
			}
		}
		if (pluginID < 0) {
			valuesToSave.add(new ArrayList<String>() {private static final long serialVersionUID = 1L;{add(plugin);}});
			pluginID = valuesToSave.size()-1;
		}
		boolean valueSet = false;
		for (int i = 1; i < valuesToSave.get(pluginID).size(); i++) {
			if (valuesToSave.get(pluginID).get(i).startsWith(varname + " = ")) {
				valuesToSave.get(pluginID).set(i, varname + " = \"" + value + "\"");
				valueSet = true;
			}
		}
		if (!valueSet) {
			valuesToSave.get(pluginID).add(varname + " = \"" + value + "\"");
		}
	}
	
	public void SaveToFile() throws IOException {
		FileWriter fw = new FileWriter(path);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int p = 0; p < valuesToSave.size(); p++) {
			bw.write("\r\n["  +valuesToSave.get(p).get(0) + "]\r\n");
			for (int i = 1; i < valuesToSave.get(p).size(); i++) {
				bw.write(valuesToSave.get(p).get(i) + "\r\n");
			}
		}
		bw.close();
		fw.close();
	}
}
