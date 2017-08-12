package doggytalents.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

import net.minecraftforge.common.MinecraftForge;

public class VersionControl {
	
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
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
			return (T)path.getConstructor(parameterTypes).newInstance(parameter);
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T createObject(String name, Class<T> type) {
		Class<T> path = chooseClassBasedOnVersion(name, type);
		
		try {
			return (T)path.newInstance();
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> Class<T> forName(String path, Class<T> type) {
		try {
			return (Class<T>)Class.forName(path);
		} 
		catch(ClassNotFoundException e) {
			return null;
		}
	}
	
	public static <T> Class<T> chooseClassBasedOnVersion(String name, Class<T> type) {
		int index = getIndex();
		
		String path;
		Class<T> clazz;
		
		//Tries to find class in current version folder and moves down if it can't find one
		do {
			path = String.format("%s.%s", getDirectionBaseOnVersion(index--), name);
		}
		while((clazz = forName(path, type)) == null);

		return clazz;
	}
	
	public static Class chooseClassBasedOnVersion(String name) {
		return chooseClassBasedOnVersion(name, Object.class);
	}
	
	public static int getIndex() {
		switch(MinecraftForge.MC_VERSION) {
		case "1.8.9":	return 0;
		case "1.9.4":	return 1;
		case "1.10.2":	return 2;
		case "1.11.2":	return 3;
		case "1.12":
		case "1.12.1":	return 4;
		default:		return 5;
		}
	}
	
	public static String getDirectionBaseOnVersion(int index) { 
		switch(index) {
		case 0:		return "doggytalents.base.b";
		case 1:		return "doggytalents.base.c";
		case 2:		return "doggytalents.base.d";
		case 3:		return "doggytalents.base.e";
		case 4:		return "doggytalents.base.f";
		default:	return "doggytalents.base";
		}
	}
	
	/** Any class has this annotation will only be copied to the compiler if it has the minecraft version */
	@Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.TYPE/**, ElementType.METHOD**/})
    public @interface VersionConfig {
        String[] value() default { "" };
    }
}
