package net.cammann;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import results.ClassResult;
import results.MethodResult;


public class ClassBenchmarker {

	public static final int NUM_RUNS = 10000;

	private final Class<?> clazz;
	private final List<Method> methods = new ArrayList<Method>();
	private Object instance;
	private final ClassResult results;
	private Object arguments[];
	private int executions;
	private Object returned;

	public ClassBenchmarker(Class<?> clazz) {
		this.clazz = clazz;
		results = new ClassResult(clazz);
	}

	public void execute() {
		findBenchmarkMethods();
		createInstance();
		runBenchmarkMethods();
	}

	private void findBenchmarkMethods() {
		for (Method i : clazz.getMethods()) {
			if (i.isAnnotationPresent(Benchmark.class)) {
				methods.add(i);
			}
		}
	}

	private void createInstance() {
		try {
			this.instance = clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.getAnnotation(Fixed.class) != null) {
					Fixed f = field.getAnnotation(Fixed.class);
					Object value = createObjectFromString(f.value(), field.getType());
					field.set(instance, value);
				}
			}
		} catch (InstantiationException e) {
			throw new BenchmarkException(e);
		} catch (IllegalAccessException e) {
			throw new BenchmarkException(e);
		}
	}

	private void runBenchmarkMethods() {
		for (Method m : methods) {
			executions = m.getAnnotation(Benchmark.class).value();
			m.setAccessible(true);
			setArguments(m);
			for (int run = 0; run < executions; run++) {
				Date start = new Date();
				for (int i = 0; i < NUM_RUNS; i++) {
					invokeMethod(m);
				}
				Date finish = new Date();
				recordResult(m, finish.getTime() - start.getTime());
			}
		}
	}

	private void recordResult(Method m, long time) {
		MethodResult r = new MethodResult(m, arguments, time, returned);
		System.out.println(r);
		results.add(r);
	}

	private void setArguments(Method m) {
		if (m.getParameterTypes().length == 0) {
			arguments = null;
			return;
		}

		arguments = new Object[m.getParameterTypes().length];
		int count = 0;
		for (Annotation[] array : m.getParameterAnnotations()) {
			if (array.length == 0) {
				throw new BenchmarkException("Argument needs to be set for: " + m.getName()
						+ " in " + clazz.getName());
			}
			for(Annotation a : array) {
				if (a.annotationType().equals(Fixed.class)) {
					Fixed fixed = (Fixed) a;
					String var = fixed.value();
					Class<?> type = m.getParameterTypes()[count];
					arguments[count] = createObjectFromString(var, type);
				}

				count++;
			}
		}
	}



	private Object createObjectFromString(String arg, Class<?> type) {
		if (type.isPrimitive()) {
			type = PrimitiveUtil.getNonPrimitiveType(type);
		}

		try {
			Constructor<?> constructor = type.getDeclaredConstructor(String.class);
			return constructor.newInstance(arg);
		} catch (Exception e) {
			throw new BenchmarkException(e);
		}

	}

	private void invokeMethod(Method m) {
		try {
			returned = m.invoke(instance, arguments);
		} catch (Exception e) {
			throw new BenchmarkException(e);
		}
	}

	public ClassResult getResult() {
		return results;
	}

}
