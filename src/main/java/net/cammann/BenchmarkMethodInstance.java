package net.cammann;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.NoReturn;
import net.cammann.annotations.Range;
import net.cammann.results.MethodRangeResult;

public class BenchmarkMethodInstance {

	private final Method method;
	private Object[] arguments;
	private long methodRuntime;
	private static final int NUM_RUNS = 10;
	private int rangeSize = 1;
	private final MethodRangeResult results;

	public BenchmarkMethodInstance(Method method) {
		if (!method.isAnnotationPresent(Benchmark.class)) {
			throw new BenchmarkException("Method does not have benchmark annotation");
		}
		results = new MethodRangeResult(method);
		method.setAccessible(true);
		this.method = method;
		assessRange();
	}

	public MethodRangeResult executeMethodBenchmark(BenchmarkObjectInstance instance) {
		instance.setFields();
		results.clear();
		for (int k = 0; k < rangeSize; k++) {
			setMethodArguments(k);
			for (int run = 0; run < executions(); run++) {
				for (int i = 0; i < NUM_RUNS; i++) {
					invokeMethod(instance.getInstance());
				}
			}
		}
		return results;
	}

	public void invokeMethod(Object instance) {
		try {
			long startTime = System.nanoTime();
			Object returned = method.invoke(instance, arguments);
			methodRuntime = System.nanoTime() - startTime;

			if (hasAnnotation(NoReturn.class)) {
				results.recordResult(arguments, methodRuntime);
			} else {
				results.recordResult(arguments, methodRuntime, returned);
			}
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

	public void setMethodArguments(int rangeSpec) {
		if (method.getParameterTypes().length == 0) {
			arguments = null;
			return;
		}

		arguments = new Object[method.getParameterTypes().length];
		int count = 0;
		boolean set = false;
		for (Annotation[] array : method.getParameterAnnotations()) {
			if (array.length == 0) {
				throw new BenchmarkException("Argument needs to be set for: " + method.getName() + " in "
						+ method.getDeclaringClass().getName());
			}
			for (Annotation a : array) {
				Class<?> type = method.getParameterTypes()[count];
				if (a.annotationType().equals(Fixed.class)) {
					Fixed fixed = (Fixed) a;
					String var = fixed.value();

					arguments[count] = BenchmarkUtil.createObjectFromString(var, type);
					set = true;
				} else if (a.annotationType().equals(Range.class)) {
					Range range = (Range) a;
					String[] rangeVals = range.value();
					int n = rangeSpec % rangeVals.length;
					arguments[count] = BenchmarkUtil.createObjectFromString(rangeVals[n], type);
					set = true;
				}
			}
			if (!set) {
				throw new BenchmarkException("Argument needs to be set for: " + method.getName() + " in "
						+ method.getDeclaringClass().getName());
			}
			count++;
		}

	}

	private void assessRange() {
		for (Annotation[] array : method.getParameterAnnotations()) {
			for (Annotation a : array) {
				if (a.annotationType().equals(Range.class)) {
					Range range = (Range) a;
					int len = range.value().length;
					if (len > rangeSize) {
						rangeSize = len;
					}
				}
			}
		}
	}

	public MethodRangeResult getResults() {
		return results;
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
