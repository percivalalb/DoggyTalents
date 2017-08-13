package mcpcustom;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Main {

	public static final String PATH_1 = "C:\\Users\\alexl\\.gradle\\caches\\modules-2\\files-2.1\\de.oceanlabs.mcp\\mcp_stable\\22-1.8.9\\a0ac11737e5e8100f91c65b66da02b1fbbf08809";
	public static final String PATH_2 = "C:\\Users\\alexl\\.gradle\\caches\\minecraft\\de\\oceanlabs\\mcp\\mcp_stable\\22";
	public static final String PATH_3 = "C:\\Users\\alexl\\.gradle\\caches\\minecraft\\net\\minecraftforge\\forge\\1.8.9-11.15.1.2318-1.8.9\\stable\\22";
	public static final String PATH_4 = "C:\\Users\\alexl\\.gradle\\caches\\modules-2\\files-2.1\\de.oceanlabs.mcp\\mcp\\1.8.9\\7612cfa7e48787ce5aa989c6c513807d18b31a36";
	public static final String PATH_5 = "C:\\Users\\alexl\\.gradle\\caches\\minecraft\\net\\minecraftforge\\forge\\1.8.9-11.15.1.2318-1.8.9\\userdev";
	
	public static void main(String[] args) throws Exception {
		File file1 = new File(PATH_1);
		File file2 = new File(PATH_2);
		File file3 = new File(PATH_3);
		File file4 = new File(PATH_4);
		File file5 = new File(PATH_5);
		if(file2.exists()) deleteDirectory(file2);
		if(file3.exists()) deleteDirectory(file3);
		if(file5.exists()) deleteDirectory(file5);
		//if(file4.exists()) deleteDirectory(file4);
		
		Desktop.getDesktop().open(file1);
		Desktop.getDesktop().open(file4);
	}
	
	public static void deleteDirectory(File sourceDir) throws IOException {
		for(File file : sourceDir.listFiles()) {
			if(file.isDirectory())
				deleteDirectory(file);

			file.delete();
		}
	}
}
