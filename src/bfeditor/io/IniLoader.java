package bfeditor.io;

import java.io.BufferedReader;
import java.io.FileReader;
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
	
	public IniLoader(String path) throws IOException {
		this.path = path;
		FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
            System.out.println(line);
        }
        bufferedReader.close();
        fileContents = lines.toArray(new String[lines.size()]);
	}
	
	public int readInt(String plugin, String varname) {
		return Integer.parseInt(getVarVal(plugin, varname));
	}
	
	public float readSingle(String plugin, String varname) {
		return Float.parseFloat(getVarVal(plugin, varname));
	}
	
	public String readString(String plugin, String varname) {
		return getVarVal(plugin, varname);
	}
	
	private String getVarVal(String plugin, String varname) {
		System.out.println(plugin + ":" + varname);
		
		String currPlugin = null;
		for (int s = 0; s < fileContents.length; s++) {
			
			System.out.println(fileContents[s]);
			
			if (fileContents[s].startsWith("[")) {
				System.out.println("new plugin");
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < fileContents[s].length(); i++) {
					if (fileContents[s].charAt(i) == 93) { // ascii: 93 = ]
						break;
					} else {
						sb.append(fileContents[s].charAt(i));
					}
				}
				currPlugin = sb.toString();
				System.out.println(currPlugin);
			}
			if (fileContents[s].startsWith(varname) && currPlugin.equals(plugin)) {
				System.out.println("found var");
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
	
	public void SaveToFile(String plugin, String varname, String value) {
		
	}
}
