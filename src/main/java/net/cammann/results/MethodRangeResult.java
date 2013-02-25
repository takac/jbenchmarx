package net.cammann.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cammann.ParameterisedMethod;

public class MethodRangeResult {

	private final Map<ParameterisedMethod, List<MethodResult>> results = new HashMap<ParameterisedMethod, List<MethodResult>>();
	private final Method method;

	public MethodRangeResult(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public Set<ParameterisedMethod> getParameterisedMethodsTested() {
		return results.keySet();
	}

	private void saveResult(MethodResult r) {
		List<MethodResult> resultList = results.get(r.getParameterisedMethod());
		if (resultList == null) {
			resultList = new ArrayList<MethodResult>();
			results.put(r.getParameterisedMethod(), resultList);
		}
		resultList.add(r);

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
		System.out.println(r);
	}


	public void recordResult(ParameterisedMethod args, long startNanoSecs, long endNanoSeconds, Object returned) {
		MethodResult r = new MethodResult(args, startNanoSecs, endNanoSeconds, returned);
		saveResult(r);
		System.out.println(r);
	}

	public void clear() {
		results.clear();
	}

	public List<MethodResult> getResults(ParameterisedMethod args) {
		return results.get(args);
	}

}
