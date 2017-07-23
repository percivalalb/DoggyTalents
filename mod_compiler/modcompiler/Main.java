package modcompiler;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
	
	public static final String FINAL_DES = "C:\\Users\\alexl\\Documents\\Minecraft\\CompiledMods";
	public static Mod MOD = Mod.DOGGY_TALENTS;
	public static ForgeEnvironment FORGE_ENVIR = ForgeEnvironment._1_7_10;
	public static Output output;
	
	public static final boolean UNIVERSAL = true;
	
	
	public enum Mod {
		
		DOGGY_TALENTS("DoggyTalents", "doggytalents", "C:\\Users\\alexl\\Documents\\GitHub\\DoggyTalents"),
		MAP_MAKING_TOOLS("MapMakingTools", "mapmakingtools", "C:\\Users\\alexl\\Documents\\GitHub\\MapMakingTools"),
		TRAPCRAFT("Trapcraft", "trapcraft", "C:\\Users\\alexl\\Documents\\GitHub\\Trapcraft");
		
		public String name;
		public String packageLoc;
		private String pathBase;
		
		Mod(String name, String packageLoc, String pathBase) {
			this.name = name;
			this.packageLoc = packageLoc;
			this.pathBase = pathBase;
		}
		
		public String getPathToVersion(ForgeEnvironment forgeEnvi) {
			return this.getPath() + "\\src-mc-" + (UNIVERSAL ? "universal" : forgeEnvi.getVersion());
		}
		
		public String getPath() {
			return this.pathBase;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	public enum ForgeEnvironment {
		
		_1_7_10("1.7.10", "C:\\Users\\alexl\\Documents\\Minecraft\\Forge\\forge-1.7.10-10.13.4.1614"),
		_1_8_9("1.8.9", "C:\\Users\\alexl\\Documents\\Minecraft\\Forge\\forge-1.8.9-11.15.1.1902"),
		_1_9_4("1.9.4", "C:\\Users\\alexl\\Documents\\Minecraft\\Forge\\forge-1.9.4-12.17.0.2051"),
		_1_10_2("1.10.2", "C:\\Users\\alexl\\Documents\\Minecraft\\Forge\\forge-1.10.2"),
		_1_11_2("1.11.2", "C:\\Users\\alexl\\Documents\\Minecraft\\Forge\\forge-1.11.2-13.20.0.2282"),
		_1_12("1.12", "C:\\Users\\alexl\\Documents\\Minecraft\\Forge\\forge-1.12");
		
		private String pathBase;
		private String version;
		
		ForgeEnvironment(String version, String pathBase) {
			this.version = version;
			this.pathBase = pathBase;
		}
		
		public String getPath() {
			return this.pathBase;
		}
		
		public String getVersion() {
			return this.version;
		}
		
		@Override
		public String toString() {
			return this.version;
		}
	}

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		        } 
		        catch(Exception ex) {}
				
				JFrame frame = new JFrame("Custom Mod Compiler");
				
				JTextField versionInput = new JTextField("1.14.0");
				versionInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
				
				JButton buttonDT = new JButton("Compile ALL versions");
				buttonDT.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
				buttonDT.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
				
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									for(ForgeEnvironment envir : new ForgeEnvironment[] {ForgeEnvironment._1_9_4, ForgeEnvironment._1_10_2, ForgeEnvironment._1_11_2, ForgeEnvironment._1_12})
										Main.compileMod(envir, Main.MOD, versionInput.getText());
								}
								catch(IOException e) {
									e.printStackTrace();
								}
							}
								
						}).start();

					}
				});
				
				JButton buttonCompile = new JButton("Compile");
				buttonCompile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
				buttonCompile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
				
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									Main.compileMod(Main.FORGE_ENVIR, Main.MOD, versionInput.getText());
								}
								catch(IOException e) {
									e.printStackTrace();
								}
							}
								
						}).start();

					}
				});
		
				JComboBox<ForgeEnvironment> forgeEnviList = new JComboBox<ForgeEnvironment>(ForgeEnvironment.values());
				forgeEnviList.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
				forgeEnviList.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						Main.FORGE_ENVIR = (ForgeEnvironment)forgeEnviList.getSelectedItem();
					}
				});
				
				JComboBox<Mod> modList = new JComboBox<Mod>(Mod.values());
				modList.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
				modList.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						Main.MOD = (Mod)modList.getSelectedItem();
					}
				});
				
			    JScrollPane outputTextScroll;
			    JTextArea outputTextArea;
			 	outputTextScroll = new JScrollPane();
		        outputTextArea = new JTextArea(1, 1);
			    output = new Output.TextComponent(outputTextArea, outputTextScroll);
			    
			    outputTextArea.setEditable(false);
		        outputTextScroll.setViewportView(outputTextArea);
		        outputTextScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
				frame.getContentPane().add(forgeEnviList);
				frame.getContentPane().add(modList);
				frame.getContentPane().add(versionInput);
				frame.getContentPane().add(buttonDT);
				frame.getContentPane().add(buttonDT);
				frame.getContentPane().add(buttonCompile);
		        frame.getContentPane().add(outputTextScroll);
				frame.pack();
				frame.setSize(600, 600);
				frame.setVisible(true);
            }
		});
    }
	
	/**
	 * 
	 * @return Whether the compilation was successful
	 * @throws IOException 
	 */
	public static boolean compileMod(ForgeEnvironment forgeEnvi, Mod mod, String modVersion) throws IOException {
        File minecraftFolder = getMinecraftFolder();
        File modFolder = new File(minecraftFolder, "mods\\" + forgeEnvi.getVersion());
		
		Main.output.println(forgeEnvi + " | " + mod);
		File modSrc = new File(mod.getPathToVersion(forgeEnvi));
		File forgeSrc = new File(forgeEnvi.getPath(), "src\\main\\java");
		File forgeResources = new File(forgeEnvi.getPath(), "src\\main\\resources");
		
		File buildClasses = new File(forgeEnvi.getPath(), "build\\classes");
		File buildResources = new File(forgeEnvi.getPath(), "build\\resources");
		
		File finalJar = new File(forgeEnvi.getPath(), "build\\libs\\modid-1.0.jar");
		
		Main.output.println("Doing some error checking");
		Main.output.println("----------------------------------------------------------------");
		if(new File(forgeEnvi.getPath(), "gradlew.bat").exists())
			Main.output.println("   Found gradlew.bat file, forge enivironment looks good.");
		else {
			Main.output.println("   No gradlew.bat file, are you sure this is a forge environment?.");
			return false;
		}
		Main.output.println("----------------------------------------------------------------\n");
		
		
		Main.output.println("Deleting old mod files.");
		//Deletes previous mod src and build directories
		deleteDirectory(forgeSrc);
		deleteDirectory(forgeResources);
		if(buildClasses.exists()) deleteDirectory(buildClasses);
		if(buildResources.exists()) deleteDirectory(buildResources);
		
		String versionSpecificLoc = getDirectionBaseOnVersion(mod, getIndex(forgeEnvi));
		
		FileFilter javaFiles = new FileFilter() {
		    @Override
		    public boolean accept(File file) {
		        if(file.isDirectory()) return true;
		        
		        boolean javaFile = file.getName().endsWith(".java");
		        boolean isSpecific = !file.getParentFile().getAbsolutePath().contains("base\\") || file.getAbsolutePath().contains(versionSpecificLoc);
		        
		        return javaFile && isSpecific;
		    }
		};
		
		boolean is1_12 = forgeEnvi.getVersion().equals("1.12");
		
		FileFilter notJavaFiles = new FileFilter() {
		    @Override
		    public boolean accept(File file) {
		        if(file.isDirectory()) return true;
		        boolean isJsonRecipes = file.getParentFile().getName().equals("recipes") ? is1_12 : true;
		        
		        return !file.getName().endsWith(".java") && !file.getName().endsWith(".db") && !file.getName().equals("compile.cfg") && isJsonRecipes;
		    }
		};
		
		Main.output.println(calculateFile(modSrc, javaFiles) + " java files to copy.");
		copyDirectory(modSrc, forgeSrc, javaFiles);
		Main.output.println("Succesfully copied all java files.");
		
		Main.output.println(calculateFile(modSrc, notJavaFiles) + " other resource files to copy.");
		copyDirectory(modSrc, forgeResources, notJavaFiles);
		Main.output.println("Succesfully copied all resource files.");
		
		if(UNIVERSAL) {
			File config = new File(modSrc, mod.packageLoc + "\\base\\compile.cfg");
			Iterator<String> stream = Files.lines(config.toPath(), StandardCharsets.UTF_8).iterator();
			Main.output.println("Reading config file to determine which version specific files to copy, this is designed to avoid compile errors");
			while(stream.hasNext()) {
				String line = stream.next();
				if(line.isEmpty() || line.startsWith("#")) continue;
				
				
				Main.output.println(line);
				
				
				int index = getIndex(forgeEnvi);
				
				String path;
				File clazz;
				
				//Tries to find class in current version folder and moves down if it can't find one
				do {
					path = String.format("%s\\%s.java", getDirectionBaseOnVersion(mod, index--), line);
				}
				while(!(clazz = new File(modSrc, path)).exists() && index >= 0);
				
				if(!clazz.exists()) {
					Main.output.println("Could not locate %s. This could be an error or simply not required", clazz.getName());
					continue;
				}
				
				File newFile = new File(forgeSrc, path);
				File parent = newFile.getParentFile();
				if(!parent.exists() && !parent.mkdirs()) 
				    throw new IllegalStateException("Couldn't create dir: " + parent);
				
				copyFile(clazz, newFile);
			}
		}
		
		String content = "";
		try {
			URLConnection connection =  new URL("https://github.com/ProPercivalalb/DoggyTalents").openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
		}
		catch(Exception ex) {
		    ex.printStackTrace();
		}
		
		Pattern pattern1 = Pattern.compile("\\<span class\\=\"num text-emphasized\"\\>\\s*(\\d+)");
        Matcher matcher1 = pattern1.matcher(content);
        if(matcher1.find())
        	modVersion += "." + matcher1.group(1);
        else
    		modVersion = new SimpleDateFormat("dd_MM_yyyy_HH_mm").format(Calendar.getInstance().getTime());
		
        Main.output.println(" >>>> Mod Version will be: %s", modVersion);
        
		try {
			File file = new File(forgeSrc, "doggytalents\\lib\\Reference.java");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while((line = reader.readLine()) != null) {
				oldtext += line + "\r\n";
			}
			reader.close();
			int index = getIndex(forgeEnvi);
			String path;
			File clazz;
			do {
				path = String.format("%s\\%s.java", getDirectionBaseOnVersion(mod, index--), "GuiFactory");
			}
			while(!(clazz = new File(forgeSrc, path)).exists() && index >= 0);
			String guiFactory = clazz.getAbsolutePath();
			guiFactory = guiFactory.substring(guiFactory.indexOf("java") + 5, guiFactory.length() - 5);
			Main.output.println(guiFactory);
			guiFactory = guiFactory.replaceAll("\\\\", ".");
			Main.output.println(guiFactory);
			String replacedtext = oldtext.replaceAll("\\$\\{GUI_FACTORY\\}", guiFactory);
			replacedtext = replacedtext.replaceAll("\\$\\{MOD_VERSION\\}", modVersion);
	        
	        FileWriter writer = new FileWriter(file.getAbsoluteFile());
			writer.write(replacedtext);


			writer.close();

		}
     	catch(IOException ioe) {
     		ioe.printStackTrace();
     	}
		
		try {
			File file = new File(forgeResources, "mcmod.info");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while((line = reader.readLine()) != null) {
				oldtext += line + "\r\n";
			}
			reader.close();
			
			String replacedtext = oldtext.replaceAll("\\$\\{MOD_VERSION\\}", modVersion);
		
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			writer.write(replacedtext);


			writer.close();

		}
     	catch(IOException ioe) {
     		ioe.printStackTrace();
     	}
		
		//if(true) return false;
		Main.output.println("Starting compile sequence... \n\n");
		
		String[] command = {"cmd.exe", "/c", "cd \"" + forgeEnvi.getPath() + "\" && gradlew build"};
		ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while(true) {
            line = r.readLine();
            if (line == null) break;
            Main.output.println(line);
        }
        
        for(File file : modFolder.listFiles()) {
        	if(file.isFile()) {
        		if(file.getName().startsWith(mod.name)) {
        			file.delete();
        			Main.output.println("***** Deleting %s file *****", file.getName());
        		}
        	}
        }
        
        copyJarFile(new JarFile(finalJar), modFolder, mod.name + "-" + forgeEnvi.getVersion() + "-" + modVersion + "-universal.jar");
        
		Main.output.println("Deleting current mod files ready for use.");
		deleteDirectory(forgeSrc);
		deleteDirectory(forgeResources);
		
        Main.output.println("Opening window to output jar...");
        
        Desktop.getDesktop().open(modFolder);
        
        return true;
	}
	
	public static void copyJarFile(JarFile jarFile, File dirFile, String fileName) throws IOException {
	    if(!dirFile.exists())
	    	dirFile.mkdirs();

	    File destFile = new File(dirFile, fileName);
	 
	    JarOutputStream jos = new JarOutputStream(new FileOutputStream(destFile));
	    Enumeration<JarEntry> entries = jarFile.entries();
	 
	    while(entries.hasMoreElements()) {
	       JarEntry entry = entries.nextElement();
	       InputStream is = jarFile.getInputStream(entry);
	           
	       if(entry.getName().startsWith("codechicken")) continue;
	           
	       jos.putNextEntry(new JarEntry(entry.getName()));
	       byte[] buffer = new byte[4096];
	       int bytesRead = 0;
	       while ((bytesRead = is.read(buffer)) != -1)
	           jos.write(buffer, 0, bytesRead);
	       is.close();
	       jos.flush();
	       jos.closeEntry();
	   }
	   jos.close();
	}
	
	public static int getIndex(ForgeEnvironment forgeEnvi) {
		switch(forgeEnvi.version) {
		case "1.9.4":	return 0;
		case "1.10.2":	return 1;
		case "1.11.2":	return 2;
		case "1.12":	return 3;
		default:		return 4;
		}
	}
	
	public static String getDirectionBaseOnVersion(Mod mod, int index) { 
		switch(index) {
		case 0:		return mod.packageLoc + "\\base\\a";
		case 1:		return mod.packageLoc + "\\base\\b";
		case 2:		return mod.packageLoc + "\\base\\c";
		case 3:		return mod.packageLoc + "\\base\\d";
		default: 	return mod.packageLoc + "\\base\\default";
		}
	}
	
	public static int calculateFile(File dir, FileFilter filter) {
		int count = 0;
		for(File file : dir.listFiles(filter)) {
			if(file.isDirectory())
				count += calculateFile(file, filter);
			else
				count += 1;
		}
		return count;
	}
	
	public static void deleteDirectory(File sourceDir) throws IOException {
		for(File file : sourceDir.listFiles()) {
			if(file.isDirectory())
				deleteDirectory(file);

			file.delete();
		}
	}
	
	public static void copyDirectory(File sourceDir, File destDir, FileFilter filter) throws IOException {
		for(File file : sourceDir.listFiles(filter)) {
			if(file.isDirectory())
				copyDirectory(file, new File(destDir, file.getName()), filter);
			else {
				if(!destDir.exists()) destDir.mkdirs();
				
				copyFile(file, new File(destDir, file.getName()));
			}
		}
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists())
	        destFile.createNewFile();

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null)
	            source.close();
	        
	        if(destination != null)
	            destination.close();
	    }
	}
	
	public static File getMinecraftFolder() {
    	String userHome = System.getProperty("user.home", ".");
        File localFile;
        switch (getPlatform()) {
        	case LINUX:
        	case SOLARIS:
        		localFile = new File(userHome, ".minecraft/");
        		break;
	        case WINDOWS:
	        	String appdata = System.getenv("APPDATA");
	        	String finalPath = appdata != null ? appdata : userHome;
	        	localFile = new File(finalPath, ".minecraft/");
	        	break;
	        case MACOS:
	        	localFile = new File(userHome, "Library/Application Support/minecraft");
	        	break;
	        default:
	        	localFile = new File(userHome, "minecraft/");
        }
        
        if (!localFile.exists() && !localFile.mkdirs())
        	throw new RuntimeException("The minecraft directory could not be created: " + localFile);
        
        return localFile;
    }
	
	public static enum OS {
		LINUX,
		SOLARIS,
		WINDOWS,
    	MACOS,
    	UNKNOWN;
	}
	
    public static OS getPlatform() {
        String systemOs = System.getProperty("os.name").toLowerCase();
        if(systemOs.contains("win")) return OS.WINDOWS;
        if(systemOs.contains("mac")) return OS.MACOS;
        if(systemOs.contains("solaris") || systemOs.contains("sunos")) return OS.SOLARIS;
        if(systemOs.contains("linux") || systemOs.contains("unix")) return OS.LINUX;
        return OS.UNKNOWN;
    }
}
