package results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ClassResult implements Result {

	Map<Method, List<MethodResult>> resultMap = new HashMap<Method, List<MethodResult>>();

	public final Class<?> classTested;

	public ClassResult(Class<?> cls) {
		this.classTested = cls;
	}

	public void add(MethodResult result) {
		List<MethodResult> resultList = resultMap.get(result.getMethod());
		if(resultList == null) {
			resultList = new ArrayList<MethodResult>();
			resultMap.put(result.getMethod(), resultList);
		}
		resultList.add(result);

	}

	public Set<Method> getMethodsTested() {
		return resultMap.keySet();
	}

	public List<MethodResult> getMethodResults(Method m) {
		return resultMap.get(m);
	}

	public Long getMethodAverage(Method m) {
		long average = 0;
		for(MethodResult i : resultMap.get(m)) {
			average += i.getTime();
		}
		return average /= resultMap.get(m).size();
	}

	public List<List<MethodResult>> getMethodResults() {
		return new ArrayList<List<MethodResult>>(resultMap.values());
	}

	public void printResult() {
		for (Method m : getMethodsTested()) {
			System.out.println(getClassTested().getName() + "." + m.getName() + " - averaged: "
					+ getMethodAverage(m) + "ms after " + getMethodResults(m).size()
					+ " iterations");
		}
	}

	public Class<?> getClassTested() {
		return classTested;
	}

}
