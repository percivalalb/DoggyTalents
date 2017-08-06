package doggytalents.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author ProPercivalalb
 * For any of these method if it requires an instance but it is a static thing
 * you are try to look up just put the instance as null
 */
public class ReflectionUtil {

	public static Field getField(Class<?> class1, int fieldIndex) {
		try {
			Field field = class1.getDeclaredFields()[fieldIndex];
	        field.setAccessible(true);
	        return field;
	    }
		catch(Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}
	
	public static Field getField(Class<?> class1, String fieldIndex) {
		try {
			Field field = class1.getDeclaredField(fieldIndex);
	        field.setAccessible(true);
	        return field;
	    }
		catch(Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}
	
	/**
	 * Gets the object a field holds
	 * @param class1 The class the field is in
	 * @param fieldType The object the field contains
	 * @param instance The instance
	 * @param fieldName The field name
	 * @return The object that the class contains
	 */
	public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, int fieldIndex) {
		try {
			Field field = getField(class1, fieldIndex);
			return (T)field.get(instance);
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static <T> T getField(Field field, Class<T> fieldType, Object instance) {
		try {
			return (T)field.get(instance);
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * Gets the object a field holds
	 * @param class1 The class the field is in
	 * @param fieldType The object the field contains
	 * @param instance The instance
	 * @param fieldName The field name
	 * @return The object that the class contains
	 */
	public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, String fieldName) {
		try {
	         Field field = getField(class1, fieldName);
	         return (T)field.get(instance);
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * Sets a field to the giving values.
	 * @param field The target field
	 * @param instance The instance
	 * @param value The value to set the field to
	 */
	public static void setField(Field field, Object instance, Object value) {
        try {
            field.set(instance, value);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	public static void setField(Class<?> class1, Object instance, int fieldIndex, Object value) {
        try {
        	Field field = getField(class1, fieldIndex);
            field.set(instance, value);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void setField(Class<?> class1, Object instance, String fieldName, Object value) {
        try {
        	Field field = getField(class1, fieldName);
            field.set(instance, value);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	public static <T> Constructor<T> getConstructor(Class<T> class1, Class<?>... args) {
		try {
			Constructor<T> constructor = class1.getConstructor(args);
			constructor.setAccessible(true);
			return constructor;
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T invokeConstructor(Constructor<T> constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static <T> T invokeMethod(Class<?> class1, String methodName, Class<T> methodType, Object instance, Class[] argsClass, Object[] args) {
		try {
			return (T)getMethod(class1, methodName, argsClass).invoke(instance, args);
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	//Invoke void method
	public static void invokeMethod(Class<?> class1, String methodName, Object instance, Class[] argsClass, Object[] args) {
		try {
			getMethod(class1, methodName, argsClass).invoke(instance, args);
		} 
		catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static Method getMethod(Class<?> class1, String methodName, Class... argsClass) {
		try {
			
	        Method method = class1.getDeclaredMethod(methodName, argsClass);
	        method.setAccessible(true);
	        return method;
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static Class getClass(String name) {
		try {
			
			Class clasz = Class.forName(name);
	        return clasz;
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
