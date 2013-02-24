package net.cammann;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.NoReturn;
import net.cammann.results.ClassResult;
import net.cammann.results.MethodResult;


public class ClassBenchmarker {

	public static final int NUM_RUNS = 10;

	private final Class<?> clazz;
	private final List<BenchmarkMethodInstance> methods = new ArrayList<BenchmarkMethodInstance>();
	private final ClassResult results;
	private Object arguments[];
	private Object returned;
	private final BenchmarkObjectInstance instance;

	public ClassBenchmarker(Class<?> clazz) {
		this.clazz = clazz;
		results = new ClassResult(clazz);
		instance = new BenchmarkObjectInstance(clazz);
	}

	public void execute() {
		findBenchmarkMethods();
		if (methods.size() == 0) {
			return;
		}
		instance.newInstance();
		runBenchmarkMethods();
	}

	private void findBenchmarkMethods() {
		for (Method i : clazz.getMethods()) {
			if (i.isAnnotationPresent(Benchmark.class)) {
				methods.add(new BenchmarkMethodInstance(i));
			}
		}
	}

	private void runBenchmarkMethods() {
		for (BenchmarkMethodInstance m : methods) {
			instance.setFields();
			m.setMethodArguments();
			for (int run = 0; run < m.executions(); run++) {
				for (int i = 0; i < NUM_RUNS; i++) {
					returned = m.invokeMethod(instance.getInstance());
				}
				recordResult(m);
			}
		}
	}

	private void recordResult(BenchmarkMethodInstance method) {
		MethodResult r;
		if (method.hasAnnotation(NoReturn.class)) {
			r = new MethodResult(method.getMethod(), arguments, method.getMethodRuntime());
		} else {
			r = new MethodResult(method.getMethod(), arguments, method.getMethodRuntime(), returned);
		}
		System.out.println(r);
		results.add(r);
	}

	public ClassResult getResult() {
		return results;
	}

}
