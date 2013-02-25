package net.cammann;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Callback;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.Lookup;
import net.cammann.annotations.NoReturn;
import net.cammann.annotations.Range;
import net.cammann.callback.CallbackHandler;
import net.cammann.results.MethodRangeResult;

public class BenchmarkMethodInstance {

	private final Method method;
	private Object[] arguments;
	private static final int NUM_RUNS = 1;
	private int rangeSize = 1;
	private final MethodRangeResult results;
	private Map<String, Object> lookup;
	private CallbackHandler callbackHandler;

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
		results.clear();
		for (int k = 0; k < rangeSize; k++) {
			for (int run = 0; run < executions(); run++) {
				instance.setFields(run + 1);
				setMethodArguments(k, run + 1);
				invokeMethod(instance.getInstance());
			}
		}
		return results;
	}

	public void invokeMethod(Object instance) {
		try {
			long startTime = System.nanoTime();
			Object returned = method.invoke(instance, arguments);
			long endTime = System.nanoTime();

			if (doesReturn()) {
				results.recordResult(arguments, startTime, endTime);
			} else {
				results.recordResult(arguments, startTime, endTime, returned);
			}
		} catch (IllegalArgumentException e) {
			throw new BenchmarkException(e);
		} catch (IllegalAccessException e) {
			throw new BenchmarkException(e);
		} catch (InvocationTargetException e) {
			throw new BenchmarkException(e.getCause());
		}
	}

	private boolean doesReturn() {
		return hasAnnotation(NoReturn.class) || method.getReturnType().equals(void.class);
	}

	public int executions() {
		return method.getAnnotation(Benchmark.class).value();
	}

	public void setMethodArguments(int rangeSpec, int runNumber) {
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
				} else if (a.annotationType().equals(Lookup.class)) {
					Lookup lookupAnnotation = (Lookup) a;
					String key = lookupAnnotation.value();
					arguments[count] = lookup.get(key);
					set = true;
				} else if (a.annotationType().equals(Callback.class)) {
					Callback callback = (Callback) a;
					String key = callback.value();
					arguments[count] = callbackHandler.call(key, method, runNumber);
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

	public void setLookup(Map<String, Object> lookup) {
		this.lookup = lookup;
	}

	public Map<String, Object> getLookup() {
		return lookup;
	}

	public CallbackHandler getCallbackHandler() {
		return callbackHandler;
	}

	public void setCallbackHandler(CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}
}
