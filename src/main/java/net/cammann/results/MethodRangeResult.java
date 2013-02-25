package net.cammann.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cammann.Arguments;

public class MethodRangeResult {

	private final Map<Arguments, List<MethodResult>> results = new HashMap<Arguments, List<MethodResult>>();
	private final Method method;

	public MethodRangeResult(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public Set<Arguments> getArguments() {
		return results.keySet();
	}

	private void saveResult(MethodResult r) {
		List<MethodResult> resultList = results.get(r.getArguments());
		if (resultList == null) {
			resultList = new ArrayList<MethodResult>();
			results.put(r.getArguments(), resultList);
		}
		resultList.add(r);

	}

	public void recordResult(Object[] args, long runtime, Object returned) {
		recordResult(new Arguments(method, args), runtime, returned);
	}

	public void recordResult(Arguments args, long runtime) {
		MethodResult r = new MethodResult(method, args, runtime);
		saveResult(r);
		System.out.println(r);
	}

	public void recordResult(Object[] args, long runtime) {
		recordResult(new Arguments(method, args), runtime);
	}

	public void recordResult(Arguments args, long runtime, Object returned) {
		MethodResult r = new MethodResult(method, args, runtime, returned);
		saveResult(r);
		System.out.println(r);
	}

	public void clear() {
		results.clear();
	}

	public List<MethodResult> getResults(Arguments args) {
		return results.get(args);
	}

}
