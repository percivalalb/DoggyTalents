package doggytalents.handler;

import java.io.File;
import java.net.URL;

/**
 * @author ProPercivalalb
 **/
public class DogTextureLoader {

	public static void loadYourTexures() {
		URL url = DogTextureLoader.class.getResource("/assets/doggytalents/textures/mob/dog/");
		try {
		File folder = new File(url.toURI());
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		}
		catch(Exception e) {
			
		}
	}
}