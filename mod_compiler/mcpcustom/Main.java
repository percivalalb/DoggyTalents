package mcpcustom;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

	public static final String[] _1_7_10 = new String[] {"12", "1.7.10", "10.13.4.1614-1.7.10", "690fe46a9208004f0336c0378b223ffd5f9b2cae"};
	public static final String[] _1_8_9 = new String[] {"22", "1.8.9", "11.15.1.2318-1.8.9", "a0ac11737e5e8100f91c65b66da02b1fbbf08809"};
	
	public static String[] SELECT = _1_7_10;
	
	public static final String PATH_1 = "C:\\Users\\alexl\\.gradle\\caches\\modules-2\\files-2.1\\de.oceanlabs.mcp\\mcp_stable\\"+SELECT[0]+"-"+SELECT[1]+"\\"+SELECT[3];
	public static final String PATH_2 = "C:\\Users\\alexl\\.gradle\\caches\\minecraft\\de\\oceanlabs\\mcp\\mcp_stable\\"+SELECT[0];
	public static final String PATH_3 = "C:\\Users\\alexl\\.gradle\\caches\\minecraft\\net\\minecraftforge\\forge\\"+SELECT[1]+"-"+SELECT[2]+"\\stable\\"+SELECT[0];
	
	public static final String PATH = "C:\\Users\\alexl\\Desktop\\MCP Mappings";
	public static final String NAME = "mcp_stable-"+SELECT[0]+"-"+SELECT[1];
	public static final String MCP_MAPPINGS = PATH + "\\"+NAME;
	
	public static void main(String[] args) throws Exception {
		File file1 = new File(PATH_1);
		File file2 = new File(PATH_2);
		File file3 = new File(PATH_3);
		if(file2.exists()) deleteDirectory(file2);
		if(file3.exists()) deleteDirectory(file3);
		Iterator<String> stream = Files.lines(new File(PATH, "Mappings for DT.txt").toPath(), StandardCharsets.UTF_8)
				.filter(line -> !line.startsWith("#"))
				.filter(line -> !line.isEmpty()).iterator();
		
		Map<String, String> mapping = new HashMap<>(); 
		
		while(stream.hasNext()) {
			String[] line = stream.next().split(",");
			mapping.put(line[0], line[1]);
			System.out.println(line);
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(MCP_MAPPINGS, "fields.csv")));
		List<String> list = new ArrayList<>();
		String nextLine;
		while ((nextLine = reader.readLine()) != null) {
			list.add(nextLine);
			System.out.println(nextLine);
		}
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(MCP_MAPPINGS, "fields.csv")));
		for(String line : list) {
			String[] split = line.split(",");
		    int first = line.indexOf(",");
		    int second = line.indexOf(",", first + 1);
		    //System.out.println(first + "   --- " + second);
			
		    if(mapping.containsKey(split[0])) {
		    	System.out.println(split[0] + " Replaced: " + split[1] + " with " + mapping.get(split[0]));
			    line = line.substring(0, first + 1) + mapping.get(split[0]) + line.substring(second, line.length());
		    	System.out.println("new line" + line);
			    //lineAsList.set(1, mapping.get(split[0]));
		    }

		    // Add stuff using linesAsList.add(index, newValue) as many times as you need.
		    writer.write(line + "\n");
		}
		
		writer.flush();
		writer.close();
		
		reader = new BufferedReader(new FileReader(new File(MCP_MAPPINGS, "methods.csv")));
		list = new ArrayList<>();
		while ((nextLine = reader.readLine()) != null) {
			list.add(nextLine);
			System.out.println(nextLine);
		}
		reader.close();
		
		writer = new BufferedWriter(new FileWriter(new File(MCP_MAPPINGS, "methods.csv")));
		for(String line : list) {
			String[] split = line.split(",");
		    int first = line.indexOf(",");
		    int second = line.indexOf(",", first + 1);
		    //System.out.println(first + "   --- " + second);
			
		    if(mapping.containsKey(split[0])) {
		    	System.out.println(split[0] + " Replaced: " + split[1] + " with " + mapping.get(split[0]));
			    line = line.substring(0, first + 1) + mapping.get(split[0]) + line.substring(second, line.length());
		    	System.out.println("new line" + line);
			    //lineAsList.set(1, mapping.get(split[0]));
		    }

		    // Add stuff using linesAsList.add(index, newValue) as many times as you need.
		    writer.write(line + "\n");
		}
		
		writer.flush();
		writer.close();
	
		omtZip(MCP_MAPPINGS + "\\", PATH_1 + "\\" + NAME+".zip");
	}
	
	public static void omtZip(String path,String outputFile) {
	    final int BUFFER = 2048;
	    boolean isEntry = false;
	    ArrayList<String> directoryList = new ArrayList<String>();
	    File f = new File(path);
	    if(f.exists()) {
	    try {
	            FileOutputStream fos = new FileOutputStream(outputFile);
	            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
	            byte data[] = new byte[BUFFER];

	            if(f.isDirectory())
	            {
	               //This is Directory
	                do{
	                    String directoryName = "";
	                    if(directoryList.size() > 0)
	                    {
	                        directoryName = directoryList.get(0);
	                        System.out.println("Directory Name At 0 :"+directoryName);
	                    }
	                    String fullPath = path+directoryName;
	                    File fileList = null;
	                    if(directoryList.size() == 0)
	                    {
	                        //Main path (Root Directory)
	                        fileList = f;
	                    }else
	                    {
	                        //Child Directory
	                        fileList = new File(fullPath);
	                    }
	                    String[] filesName = fileList.list();

	                    int totalFiles = filesName.length;
	                    for(int i = 0 ; i < totalFiles ; i++)
	                    {
	                        String name = filesName[i];
	                        File filesOrDir = new File(fullPath+name);
	                        if(filesOrDir.isDirectory())
	                        {
	                            System.out.println("New Directory Entry :"+directoryName+name+"/");
	                            ZipEntry entry = new ZipEntry(directoryName+name+"/");
	                            zos.putNextEntry(entry);
	                            isEntry = true;
	                            directoryList.add(directoryName+name+"/");
	                        }else
	                        {
	                            System.out.println("New File Entry :"+directoryName+name);
	                            ZipEntry entry = new ZipEntry(directoryName+name);
	                            zos.putNextEntry(entry);
	                            isEntry = true;
	                            FileInputStream fileInputStream = new FileInputStream(filesOrDir);
	                            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER);
	                            int size = -1;
	                            while(  (size = bufferedInputStream.read(data, 0, BUFFER)) != -1  )
	                            {
	                                zos.write(data, 0, size);
	                            }
	                            bufferedInputStream.close();
	                        }
	                    }
	                    if(directoryList.size() > 0 && directoryName.trim().length() > 0)
	                    {
	                        System.out.println("Directory removed :"+directoryName);
	                        directoryList.remove(0);
	                    }

	                }while(directoryList.size() > 0);
	            }else
	            {
	                //This is File
	                //Zip this file
	                System.out.println("Zip this file :"+f.getPath());
	                FileInputStream fis = new FileInputStream(f);
	                BufferedInputStream bis = new BufferedInputStream(fis,BUFFER);
	                ZipEntry entry = new ZipEntry(f.getName());
	                zos.putNextEntry(entry);
	                isEntry = true;
	                int size = -1 ;
	                while(( size = bis.read(data,0,BUFFER)) != -1)
	                {
	                    zos.write(data, 0, size);
	                }
	            }               

	            //CHECK IS THERE ANY ENTRY IN ZIP ? ----START
	            if(isEntry)
	            {
	              zos.close();
	            }else
	            {
	                zos = null;
	                System.out.println("No Entry Found in Zip");
	            }
	            //CHECK IS THERE ANY ENTRY IN ZIP ? ----START
	        }catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	    }else
	    {
	        System.out.println("File or Directory not found");
	    }
	 }

	
	public static void deleteDirectory(File sourceDir) throws IOException {
		for(File file : sourceDir.listFiles()) {
			if(file.isDirectory())
				deleteDirectory(file);

			file.delete();
		}
	}
}
