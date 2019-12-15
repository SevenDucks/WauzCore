package eu.wauz.wauzcore.system.util;

import java.io.File;

/**
 * An util class to modify files.
 * 
 * @author Wauzmons
 */
public class WauzFileUtils {
	
	/**
	 * Deletes a folder with all of its contents.
	 * 
	 * @param file The folder to delete.
	 * 
	 * @return If the deletion was successful.
	 */
	public static boolean removeFilesRecursive(File file) {
		if(file.exists()) {
		    File files[] = file.listFiles();
		    for(int i = 0; i < files.length; i++) {
		        if(files[i].isDirectory()) {
		            removeFilesRecursive(files[i]);
		        }
		        else {
		            files[i].delete();
		        }
		    }
		    return file.delete();
		}
		return false;
	}

}
