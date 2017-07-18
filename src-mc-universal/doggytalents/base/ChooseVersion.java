package doggytalents.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ChooseVersion {
	
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?> parameterTypes) {
		try {
			return clazz.getConstructor(parameterTypes);
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T createObject(String name, Class<T> type, Class<?> parameterTypes, Object parameter) {
		Class<T> path = chooseClassBasedOnVersion(name, type);
		
		try {
			return path.getConstructor(parameterTypes).newInstance(parameter);
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T createObject(String name, Class<T> type) {
		Class<T> path = chooseClassBasedOnVersion(name, type);
		
		try {
			return path.newInstance();
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> Class<T> chooseClassBasedOnVersion(String name, Class<T> type) {
		String path = String.format("%s.%s", getDirectionBaseOnVersion(), name);
		
		try {
			return (Class<T>)Class.forName(path);
		} 
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			
			try {
				return (Class<T>)Class.forName(String.format("doggytalents.base.fallback.%s", name));
			} 
			catch(ClassNotFoundException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static Class chooseClassBasedOnVersion(String name) {
		return chooseClassBasedOnVersion(name, Object.class);
	}
	
	public static String getDirectionBaseOnVersion() { 
		switch(MinecraftForge.MC_VERSION) {
		case "1.9.4":	return "doggytalents.base.a";
		case "1.10.2":	return "doggytalents.base.b";
		case "1.11.2":	return "doggytalents.base.c";
		case "1.12":	return "doggytalents.base.d";
		default:		return "doggytalents.base";
		}
	}
}
