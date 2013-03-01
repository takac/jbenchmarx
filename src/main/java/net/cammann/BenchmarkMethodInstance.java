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
import net.cammann.results.MethodResultStore;

public class BenchmarkMethodInstance {

	private final Method method;
	private Object[] arguments;
	private int largestRange = 1;
	private final MethodResultStore results;
	private Map<String, Object> lookup;
	private CallbackHandler callbackHandler;
	private int runNumber = 0;
	private int rangeRound = 0;

	public BenchmarkMethodInstance(Method method) {
		if (!method.isAnnotationPresent(Benchmark.class)) {
			throw new BenchmarkException("Method does not have benchmark annotation");
		}
		results = new MethodResultStore(method);
		method.setAccessible(true);
		this.method = method;
		checkMaxRange();
	}

	public MethodResultStore executeMethodBenchmark(BenchmarkObjectInstance instance) {
		results.clear();
		for (int k = 0; k < largestRange; k++) {
			for (int run = 0; run < executions(); run++) {
				runNumber = run + 1;
				rangeRound = k;
				instance.setFields(runNumber);
				setMethodArguments();
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

	public void setMethodArguments() {
		if (method.getParameterTypes().length == 0) {
			arguments = new Object[]{};
			return;
		}

		arguments = new Object[method.getParameterTypes().length];
		int count = 0;
		for (Annotation[] array : method.getParameterAnnotations()) {
			if (array.length == 0) {
				throw new BenchmarkException("Annotation for parameter needs to be set: " + method.getName()
						+ " in "
						+ method.getDeclaringClass().getName());
			}
			Class<?> type = method.getParameterTypes()[count];
			Object obj = extractObject(array, type);
			if (obj == null) {
				throw new BenchmarkException("Argument for benchmark cannot be null: " + method.getName() + " in "
						+ method.getDeclaringClass().getName());
			}
			arguments[count] = obj;
			count++;
		}

	}

	// TODO
	// Create method to check for more than one benchmark annotaiton
	private Object extractObject(Annotation[] array, Class<?> type) {
		Object obj = null;
		for (Annotation a : array) {
			if (a.annotationType().equals(Fixed.class)) {
				if (obj != null) {
					throw new BenchmarkException("Can only set one annotation for this field/parameter");
				}
				Fixed fixed = (Fixed) a;
				String var = fixed.value();
				obj = BenchmarkUtil.createObjectFromString(var, type);
			} else if (a.annotationType().equals(Range.class)) {
				if (obj != null) {
					throw new BenchmarkException("Can only set one annotation for this field/parameter");
				}
				Range range = (Range) a;
				String[] rangeVals = range.value();
				int n = rangeRound % rangeVals.length;
				obj = BenchmarkUtil.createObjectFromString(rangeVals[n], type);
			} else if (a.annotationType().equals(Lookup.class)) {
				if (obj != null) {
					throw new BenchmarkException("Can only set one annotation for this field/parameter");
				}
				Lookup lookupAnnotation = (Lookup) a;
				String key = lookupAnnotation.value();
				obj = lookup.get(key);
			} else if (a.annotationType().equals(Callback.class)) {
				if (obj != null) {
					throw new BenchmarkException("Can only set one annotation for this field/parameter");
				}
				Callback callback = (Callback) a;
				String key = callback.value();
				obj = callbackHandler.call(key, method, runNumber);
			}
		}
		return obj;
	}

	private void checkMaxRange() {
		for (Annotation[] array : method.getParameterAnnotations()) {
			for (Annotation a : array) {
				if (a.annotationType().equals(Range.class)) {
					Range range = (Range) a;
					int len = range.value().length;
					if (len > largestRange) {
						largestRange = len;
					}
				}
			}
		}
	}

	public MethodResultStore getResults() {
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
