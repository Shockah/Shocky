package pl.shockah;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
	public static Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException,SecurityException {
		Field field = cls.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field;
	}
	public static void setField(Field field, Object instance, Object value) throws IllegalArgumentException,IllegalAccessException {
		field.set(instance,value);
	}
	
	public static <T> void setPrivateValue(Class<? super T> cls, String fieldName, T instance, Object value) throws IllegalArgumentException,IllegalAccessException,NoSuchFieldException,SecurityException {
		getField(cls,fieldName).set(instance,value);
	}
	@SuppressWarnings("unchecked") public static <T> T getPrivateValue(Class<?> cls, String fieldName, Object instance) throws IllegalArgumentException,IllegalAccessException,NoSuchFieldException,SecurityException {
		return (T)getField(cls,fieldName).get(instance);
	}
	
	public static Method getPrivateMethod(Class<?> cls, String methodName, Class<?>... argumentTypes) throws NoSuchMethodException,SecurityException {
		Method method = cls.getDeclaredMethod(methodName,argumentTypes);
		method.setAccessible(true);
		return method;
	}
	@SuppressWarnings("unchecked") public static <T> T invokePrivateMethod(Method method, Object instance, Object... arguments) throws IllegalAccessException,IllegalArgumentException,InvocationTargetException {
		return (T)method.invoke(instance,arguments);
	}
	
	public static <T> Constructor<T> getConstructor(Class<T> cls, Class<?>... argumentTypes) throws NoSuchMethodException,SecurityException {
		Constructor<T> constructor = cls.getConstructor(argumentTypes);
		constructor.setAccessible(true);
		return constructor;
	}
	public static <T> T newInstance(Constructor<T> ctr, Object... argumentTypes) throws InstantiationException,IllegalAccessException,IllegalArgumentException,InvocationTargetException {
		return ctr.newInstance(argumentTypes);
	}
	public static <T> T newInstance(Class<T> cls) throws InstantiationException,IllegalAccessException {
		return cls.newInstance();
	}
}