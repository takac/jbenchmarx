package net.cammann;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.cammann.annotations.Benchmark;
import net.cammann.callback.CallbackHandler;
import net.cammann.results.ClassResult;

public class ClassBenchmarker {

	private final Class<?> clazz;
	private final List<BenchmarkMethodInstance> methods = new ArrayList<BenchmarkMethodInstance>();
	private final ClassResult results;
	private final BenchmarkObjectInstance instance;
	private List<Method> methodsToTest = null;
	private Map<String, Object> lookup;
	private CallbackHandler callbackHandler;

	public ClassBenchmarker(Class<?> clazz) {
		this.clazz = clazz;
		results = new ClassResult(clazz);
		instance = new BenchmarkObjectInstance(clazz);
	}

	public void execute() {
		instance.setLookup(lookup);
		instance.setHandler(callbackHandler);
		if (methodsToTest == null) {
			findBenchmarkMethods();
		} else {
			methods.clear();
			for (Method i : methodsToTest) {
				BenchmarkMethodInstance inst = new BenchmarkMethodInstance(i);
				inst.setLookup(lookup);
				inst.setCallbackHandler(callbackHandler);
				methods.add(inst);
			}
		}
		if (methods.size() == 0) {
			return;
		}
		instance.newInstance();
		runBenchmarkMethods();
	}

	private void findBenchmarkMethods() {
		methods.clear();
		for (Method i : clazz.getDeclaredMethods()) {
			if (i.isAnnotationPresent(Benchmark.class)) {
				BenchmarkMethodInstance inst = new BenchmarkMethodInstance(i);
				inst.setLookup(lookup);
				inst.setCallbackHandler(callbackHandler);
				methods.add(inst);
			}
		}
	}

	private void runBenchmarkMethods() {
		for (BenchmarkMethodInstance m : methods) {
			m.executeMethodBenchmark(instance);
			results.add(m.getResults());
		}
	}

	public void overwriteMethodsToBenchmark(List<Method> listMethodsToTest) {
		this.methodsToTest = listMethodsToTest;
	}

	public ClassResult getResult() {
		return results;
	}

	public void setLookupTable(Map<String, Object> lookup) {
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
