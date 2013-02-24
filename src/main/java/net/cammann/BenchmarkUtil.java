package net.cammann;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BenchmarkUtil {

	public static Object createObjectFromString(String arg, Class<?> type) {

		if (type.isPrimitive()) {
			type = getNonPrimitiveType(type);
		}

		try {
			Constructor<?> constructor = type.getDeclaredConstructor(String.class);
			return constructor.newInstance(arg);
		} catch (Exception e) {
			throw new BenchmarkException(e);
		}

	}

	private static final Map<Class<?>, Class<?>> primitiveToObject = new HashMap<Class<?>, Class<?>>();
	static {
		primitiveToObject.put(int.class, Integer.class);
		primitiveToObject.put(long.class, Long.class);
		primitiveToObject.put(double.class, Double.class);
		primitiveToObject.put(float.class, Float.class);
		primitiveToObject.put(boolean.class, Boolean.class);
		primitiveToObject.put(char.class, Character.class);
		primitiveToObject.put(byte.class, Byte.class);
		primitiveToObject.put(void.class, Void.class);
		primitiveToObject.put(short.class, Short.class);
	}

	public static Class<?> getNonPrimitiveType(Class<?> type) {
		Class<?> nonPrim = primitiveToObject.get(type);
		return nonPrim == null ? type : nonPrim;
	}

	public static Class<?> getPrimitiveType(Class<?> type) {

		for (Entry<Class<?>, Class<?>> e : primitiveToObject.entrySet()) {
			if (e.getValue().equals(type)) {
				return e.getKey();
			}
		}
		return type;

	}
}
