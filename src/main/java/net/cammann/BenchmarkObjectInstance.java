package net.cammann;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import net.cammann.annotations.BenchConstructor;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.Lookup;

public class BenchmarkObjectInstance {

	private final Class<?> type;
	private Object instance;
	private Object constructorArgs[];
	private Map<String, Object> lookup;

	public BenchmarkObjectInstance(Class<?> type) {
		this.type = type;
	}

	public Object newInstance() {
		try {
			System.out.println("Creating Instance of " + type.getCanonicalName());
			for (Constructor<?> c : type.getConstructors()) {
				if (c.isAnnotationPresent(BenchConstructor.class)) {
					setConstructorArgs(c);
					System.out.println("Using annotated constructor");
					instance = c.newInstance(constructorArgs);
				}
			}
			if (instance == null) {
				this.instance = type.newInstance();
				System.out.println("Using default constructor");
			}
			setFields();
			return instance;
		} catch (InstantiationException e) {
			throw new BenchmarkException(e);
		} catch (IllegalAccessException e) {
			throw new BenchmarkException(e);
		} catch (IllegalArgumentException e) {
			throw new BenchmarkException(e);
		} catch (InvocationTargetException e) {
			throw new BenchmarkException(e);
		}
	}

	private void setConstructorArgs(Constructor<?> c) {
		int count = 0;
		constructorArgs = new Object[c.getParameterTypes().length];
		for (Annotation[] array : c.getParameterAnnotations()) {
			boolean set = false;
			for (Annotation a : array) {
				if (a.annotationType().equals(Fixed.class)) {
					Fixed fixed = (Fixed) a;
					String var = fixed.value();
					Class<?> paramType = c.getParameterTypes()[count];
					constructorArgs[count] = BenchmarkUtil.createObjectFromString(var, paramType);
					set = true;
				} else if (a.annotationType().equals(Lookup.class)) {
					Lookup lookupAnnotation = (Lookup) a;
					String key = lookupAnnotation.value();
					constructorArgs[count] = lookup.get(key);
					set = true;
				}
			}
			if (!set) {
				throw new BenchmarkException("Argument needs to be set for constructor: " + c.getName() + " in "
						+ c.getDeclaringClass().getName());
			}
			count++;
		}
	}

	public void setFields() {
		try {
			for (Field field : type.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.getAnnotation(Fixed.class) != null) {
					Fixed f = field.getAnnotation(Fixed.class);
					Object value = BenchmarkUtil.createObjectFromString(f.value(), field.getType());
					field.set(instance, value);
				} else if (field.getAnnotation(Lookup.class) != null) {
					Lookup lookupAnnotation = field.getAnnotation(Lookup.class);
					String key = lookupAnnotation.value();
					field.set(instance, lookup.get(key));
				}
			}
		} catch (IllegalArgumentException e) {
			throw new BenchmarkException(e);
		} catch (IllegalAccessException e) {
			throw new BenchmarkException(e);
		}
	}

	public Object getInstance() {
		return instance;
	}

	public void setLookup(Map<String, Object> lookup) {
		this.lookup = lookup;
	}

	public Map<String, Object> getLookup() {
		return lookup;
	}


}
