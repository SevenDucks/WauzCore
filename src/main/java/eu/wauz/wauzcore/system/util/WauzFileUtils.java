package eu.wauz.wauzcore.system.util;

import java.io.File;

public class WauzFileUtils {
	
	public static boolean removeFilesRecursive(File file) {
		if(file.exists()) {
		    File files[] = file.listFiles();
		    for(int i = 0; i < files.length; i++) {
		        if(files[i].isDirectory()) {
		            removeFilesRecursive(files[i]);
		        } else {
		            files[i].delete();
		        }
		    }
		    return file.delete();
		}
		return false;
	}

}
