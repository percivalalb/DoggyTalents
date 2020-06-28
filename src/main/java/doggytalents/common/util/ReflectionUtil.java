package doggytalents.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Object invokeMethod(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeStaticMethod(Method method, Object... params) {
        return invokeMethod(method, null, params);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz == null) {
                throw new RuntimeException("Null class " + className);
            }

            return clazz;
        } catch (ClassNotFoundException e) {
            // Class not present
            throw new RuntimeException(e);
        }
    }
}
