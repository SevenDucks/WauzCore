package eu.wauz.wauzcore.system.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * An util class to read or modify files.
 * 
 * @author Wauzmons
 */
public class WauzFileUtils {
	
	/**
	 * Finds all files in a folder and returns a list of relative paths.
	 * Removes ".yml" from file names.
	 * 
	 * @param folder The folder to search in.
	 * @param prefix The file path prefix.
	 * 
	 * @return The list of relative file paths.
	 */
	public static List<String> findRelativePathsRecursive(File folder, String prefix) {
		List<String> paths = new ArrayList<>();
		if(folder.exists()) {
			for(File file : folder.listFiles()) {
				if(file.isDirectory()) {
					paths.addAll(findRelativePathsRecursive(file, prefix + file.getName() + "/"));
				}
				else {
					paths.add(prefix + file.getName().replace(".yml", ""));
				}
			}
		}
		return paths;
	}
	
	/**
	 * Deletes a folder with all of its contents.
	 * 
	 * @param folder The folder to delete.
	 * 
	 * @return If the deletion was successful.
	 */
	public static boolean removeFilesRecursive(File folder) {
		if(folder.exists()) {
		    File files[] = folder.listFiles();
		    for(int i = 0; i < files.length; i++) {
		        if(files[i].isDirectory()) {
		            removeFilesRecursive(files[i]);
		        }
		        else {
		            files[i].delete();
		        }
		    }
		    return folder.delete();
		}
		return false;
	}
	
}
