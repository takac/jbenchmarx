package net.cammann;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.NoReturn;
import net.cammann.annotations.Range;
import net.cammann.callback.CallbackHandler;
import net.cammann.objectbuilder.AnnotationObjectBuilderFactory;
import net.cammann.objectbuilder.BuildContextImpl;
import net.cammann.objectbuilder.ObjectBuilder;
import net.cammann.results.MethodResultStore;

public class BenchmarkMethodInstance {

	private final Method method;
	private Object[] arguments;
	private int largestRange = 1;
	private final MethodResultStore results;
	private final BuildContextImpl bctx = new BuildContextImpl();

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
				bctx.setRangeRound(k);
				bctx.setRunNumber(run + 1);
				instance.setFields(run + 1);
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

	private Object extractObject(Annotation[] array, Class<?> type) {
		Object obj = null;

		AnnotationObjectBuilderFactory factory = new AnnotationObjectBuilderFactory();
		for (Annotation a : array) {
			ObjectBuilder builder = factory.getBuilder(a, bctx);
			if (builder == null) {
				continue;
			}
			if (obj != null) {
				throw new BenchmarkException("Can only set one annotation for this field/parameter");
			}
			obj = builder.get(type);
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
		bctx.setLookupMap(lookup);
	}

	public Map<String, Object> getLookup() {
		return bctx.getLookupMap();
	}

	public CallbackHandler getCallbackHandler() {
		return bctx.getCallbackHandler();
	}

	public void setCallbackHandler(CallbackHandler callbackHandler) {
		bctx.setCallbackHandler(callbackHandler);
	}
}
