package net.cammann;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;

public class BenchmarkMethodInstance {

	private final Method method;
	private Object[] arguments;
	private long methodRuntime;

	public BenchmarkMethodInstance(Method method) {
		if (!method.isAnnotationPresent(Benchmark.class)) {
			throw new BenchmarkException("Method does not have benchmark annotation");
		}
		method.setAccessible(true);
		this.method = method;
	}


	public Object invokeMethod(Object instance) {
		try {
			long startTime = System.nanoTime();
			Object obj = method.invoke(instance, arguments);
			methodRuntime = System.nanoTime() - startTime;
			return obj;
		} catch (IllegalArgumentException e) {
			throw new BenchmarkException(e);
		} catch (IllegalAccessException e) {
			throw new BenchmarkException(e);
		} catch (InvocationTargetException e) {
			throw new BenchmarkException(e);
		}
	}

	public long getMethodRuntime() {
		return methodRuntime;
	}

	public int executions() {
		return method.getAnnotation(Benchmark.class).value();
	}

	public void setMethodArguments() {
		if (method.getParameterTypes().length == 0) {
			arguments = null;
			return;
		}

		arguments = new Object[method.getParameterTypes().length];
		int count = 0;
		boolean set = false;
		for (Annotation[] array : method.getParameterAnnotations()) {
			if (array.length == 0) {
				throw new BenchmarkException("Argument needs to be set for: " + method.getName()
						+ " in " + method.getDeclaringClass().getName());
			}
			for(Annotation a : array) {
				if (a.annotationType().equals(Fixed.class)) {
					Fixed fixed = (Fixed) a;
					String var = fixed.value();
					Class<?> type = method.getParameterTypes()[count];
					arguments[count] = BenchmarkUtil.createObjectFromString(var, type);
					set = true;
				}
			}
			if (!set) {
				throw new BenchmarkException("Argument needs to be set for: " + method.getName()
						+ " in " + method.getDeclaringClass().getName());
			}
			count++;
		}

	}

	public boolean hasAnnotation(Class<? extends Annotation> type) {
		for (Annotation a : method.getAnnotations()) {
			if (a.annotationType().equals(type)) {
				return true;
		}
	}
		return false;
	}

	public Method getMethod() {
		return method;
	}

}
