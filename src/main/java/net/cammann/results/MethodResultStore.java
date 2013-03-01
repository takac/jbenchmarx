package net.cammann.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cammann.ParameterisedMethod;

public class MethodResultStore {

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

	public void setMethod(Method method) {
		this.method = method;
	}

	public Set<ParameterisedMethod> getParameterisedMethodsTested() {
		return results.keySet();
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

	public List<MethodResult> getAllMethodResults() {
		List<MethodResult> all = new ArrayList<MethodResult>();
		for (ParameterisedMethodResult i : results.values()) {
			all.addAll(i.getResults());
		}
		return all;
	}

	public AveragedResult getMethodAverage() {
		AveragedResult avg = new AveragedResult();
		List<MethodResult> all = getAllMethodResults();

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

	public void clear() {
		results.clear();
	}

	public List<MethodResult> getResults(ParameterisedMethod args) {
		ParameterisedMethodResult pmr = results.get(args);
		if (pmr == null) {
			return Collections.emptyList();
		}
		return pmr.getResults();
	}

}
