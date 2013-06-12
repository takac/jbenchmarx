package net.cammann;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.results.ClassResult;

public class ClassBenchmarker {

	private final Class<?> clazz;
	private final List<BenchmarkMethodInstance> bnchmarkMethodsInstances = new ArrayList<BenchmarkMethodInstance>();
	private final ClassResult results;
	private final BenchmarkClassInstance classInstance;
	private final ParameterResolver parameterResolver;
	private List<Method> methodsToTest = null;

	public ClassBenchmarker(Class<?> clazz, ParameterResolver parameterResolver) {
		this.clazz = clazz;
		results = new ClassResult(clazz);
		this.parameterResolver = parameterResolver;
		classInstance = new BenchmarkClassInstance(clazz, parameterResolver);
	}

	public void run() {
		if (methodsToTest == null) {
			findBenchmarkMethods();
		} else {
			bnchmarkMethodsInstances.clear();
			for (Method i : methodsToTest) {
				BenchmarkMethodInstance inst = new BenchmarkMethodInstance(i, parameterResolver);
				bnchmarkMethodsInstances.add(inst);
			}
		}
		if (bnchmarkMethodsInstances.size() == 0) {
			return;
		}
		classInstance.newInstance();
		runBenchmarkMethods();
	}

	private void findBenchmarkMethods() {
		bnchmarkMethodsInstances.clear();
		for (Method i : clazz.getDeclaredMethods()) {
			if (i.isAnnotationPresent(Benchmark.class)) {
				BenchmarkMethodInstance inst = new BenchmarkMethodInstance(i, parameterResolver);
				bnchmarkMethodsInstances.add(inst);
			}
		}
	}

	private void runBenchmarkMethods() {
		for (BenchmarkMethodInstance m : bnchmarkMethodsInstances) {
			m.executeMethodBenchmark(classInstance);
			results.add(m.getResults());
		}
	}

	public void overwriteMethodsToBenchmark(List<Method> listMethodsToTest) {
		this.methodsToTest = listMethodsToTest;
	}

	public ClassResult getResult() {
		return results;
	}

	public ParameterResolver getParameterResolver() {
		return parameterResolver;
	}
}
