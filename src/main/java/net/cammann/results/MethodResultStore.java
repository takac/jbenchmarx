package net.cammann.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cammann.ParameterisedMethod;

/**
 * Stores all benchmarks for one method, includes all parameterised methods used
 * 
 */
public class MethodResultStore extends SaveableResult {

	private final Map<ParameterisedMethod, ParameterisedMethodResult> results;

	private Method method;

	public MethodResultStore(Method method) {
		this();
		this.method = method;
	}

	public MethodResultStore() {
		results = new HashMap<ParameterisedMethod, ParameterisedMethodResult>();
	}

	public Method getMethod() {
		return method;
	}

	public List<MethodResult> getMethodResults() {
		List<MethodResult> all = new ArrayList<MethodResult>();
		for (ParameterisedMethodResult i : results.values()) {
			all.addAll(i.getResults());
		}
		return all;
	}

	public AveragedResult getMethodAverage() {
		AveragedResult avg = new AveragedResult();
		List<MethodResult> all = getMethodResults();

		avg.setIterations(all.size());
		avg.setAverageTime(100);

		long total = 0;
		for (MethodResult m : all) {
			total += m.getRuntime();
		}
		avg.setAverageTime(total / all.size());
		avg.setParamCombinations(results.size());

		return avg;
	}


	public void clear() {
		results.clear();
	}


	public void recordResult(Object[] args, long startNanoSeconds, long endNanoSeconds, Object returned) {
		recordResult(new ParameterisedMethod(method, args), startNanoSeconds, endNanoSeconds, returned);
	}

	public void recordResult(Object[] args, long startNanoSeconds, long endNanoSeconds) {
		recordResult(new ParameterisedMethod(method, args), startNanoSeconds, endNanoSeconds);
	}

	public void recordResult(ParameterisedMethod args, long startNanoSecs, long endNanoSecs) {
		MethodResult r = new MethodResult(args, startNanoSecs, endNanoSecs);
		saveResult(r);
	}

	public void recordResult(ParameterisedMethod args, long startNanoSecs, long endNanoSeconds, Object returned) {
		MethodResult r = new MethodResult(args, startNanoSecs, endNanoSeconds, returned);
		saveResult(r);
	}

	private void saveResult(MethodResult r) {
		ParameterisedMethodResult resultList = results.get(r.getParameterisedMethod());
		if (resultList == null) {
			resultList = new ParameterisedMethodResult();
			results.put(r.getParameterisedMethod(), resultList);
		}
		resultList.addResult(r);
		System.out.println(r);
	}

	public List<MethodResult> getResults(ParameterisedMethod args) {
		ParameterisedMethodResult pmr = results.get(args);
		if (pmr == null) {
			return Collections.emptyList();
		}
		return pmr.getResults();
	}

	@Override
	public void printResult() {
		throw new IllegalStateException();
	}

	@Override
	public List<MethodResultStore> getMethodResult(Method m) {
		List<MethodResultStore> single = new ArrayList<MethodResultStore>();
		single.add(this);
		return single;
	}

	@Override
	public List<MethodResult> getMethodResults(ParameterisedMethod a) {
		ParameterisedMethodResult res = results.get(a);
		if (res == null) {
			return Collections.emptyList();
		}
		return res.getResults();
	}

	@Override
	public List<ParameterisedMethod> getParameterisedMethodsTested() {
		return new ArrayList<ParameterisedMethod>(results.keySet());
	}

	@Override
	public String toString() {
		return method.getDeclaringClass().getName() + "." + method.getName() + " avg: "
				+ getMethodAverage().getAverageTimeInNanoSeconds()
				+ "ns " + " over " + getMethodResults().size() + " iterations";
	}

	@Override
	public Set<Method> getMethodsTested() {
		Set<Method> methods = new HashSet<Method>();
		for (ParameterisedMethod m : results.keySet()) {
			methods.add(m.getMethod());
		}
		return methods;
	}

}
