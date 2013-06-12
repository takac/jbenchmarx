package net.cammann;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import net.cammann.annotations.BenchConstructor;
import net.cammann.annotations.Callback;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.Lookup;
import net.cammann.annotations.Range;

public class BenchmarkClassInstance {

	private final Class<?> type;
	private Object instance;
	private Object constructorArgs[];
	private final ParameterResolver parameterResolver;

	public BenchmarkClassInstance(Class<?> type, ParameterResolver parameterResolver) {
		this.type = type;
		this.parameterResolver = parameterResolver;
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
			setFields(0);
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
					constructorArgs[count] = parameterResolver.lookup(key);
					set = true;
				} else if (a.annotationType().equals(Callback.class)) {
					throw new BenchmarkException("Cannot use callback for constructor");
				} else if (a.annotationType().equals(Range.class)) {
					throw new BenchmarkException("Cannot use range for constructor");
				}
			}
			if (!set) {
				throw new BenchmarkException("Argument needs to be set for constructor: " + c.getName() + " in "
						+ c.getDeclaringClass().getName());
			}
			count++;
		}
	}

	/**
	 * 
	 * @param runNumber
	 *            0 for after construtor, > 0 during testing
	 */
	public void setFields(int run) {
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
					Object value = parameterResolver.lookup(key);
					if (value == null) {
						throw new LookupException("value does not exist");
					}
					field.set(instance, value);
				} else if (field.getAnnotation(Callback.class) != null) {
					Callback callback = field.getAnnotation(Callback.class);
					String key = callback.value();
					field.set(instance, parameterResolver.call(key, field, run));
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

}
