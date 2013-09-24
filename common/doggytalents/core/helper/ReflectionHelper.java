package doggytalents.core.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author ProPercivalalb
 * For any of these method if it requires an instance but it is a static thing
 * you are try to look up just put the instance as null
 */
public class ReflectionHelper {

	public static Class getClass(String className) {
		try {
	        return Class.forName(className);
	    }
		catch(Exception e) {
		    return null;
		}
	}
 	
	public static Method getMethod(Class<?> class1, int methodIndex) {
		try {
			Method method = class1.getDeclaredMethods()[methodIndex];
			method.setAccessible(true);
	        return method;
	    }
		catch(Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}
	
	public static Method getMethod(Class<?> class1, String methodName, Class<?>... classParam) {
		try {
			Method method = class1.getDeclaredMethod(methodName, classParam);
			method.setAccessible(true);
	        return method;
	    }
		catch(Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}
	
	public static Field getField(Class<?> class1, Object instance, int fieldIndex) {
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
	
	public static Field getField(Class<?> class1, Object instance, String fieldIndex) {
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
			Field field = getField(class1, instance, fieldIndex);
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
	         Field field = getField(class1, instance, fieldName);
	         return (T) field.get(instance);
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
        	Field field = getField(class1, instance, fieldIndex);
            field.set(instance, value);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void setField(Class<?> class1, Object instance, String fieldName, Object value) {
        try {
        	Field field = getField(class1, instance, fieldName);
            field.set(instance, value);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
