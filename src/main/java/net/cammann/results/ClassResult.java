package net.cammann.results;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.BenchmarkException;
import net.cammann.export.CVSExport;
import net.cammann.export.Format;
import net.cammann.export.Saveable;

public class ClassResult implements Result, Saveable {

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

	public List<Method> getMethodsTested() {
		return new ArrayList<Method>(resultMap.keySet());
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

	public Map<Method, List<MethodResult>> getMethodResults() {
		Map<Method, List<MethodResult>> results = new HashMap<Method, List<MethodResult>>();
		for(Method m : getMethodsTested()) {
			results.put(m, getMethodResults(m));
		}
		return results;
	}

	public void printResult() {
		for (Method m : getMethodsTested()) {
			System.out.println(getClassTested().getName() + "." + m.getName() + " - averaged: "
					+ getMethodAverage(m) + "ns after " + getMethodResults(m).size()
					+ " iterations");
		}
	}

	public Class<?> getClassTested() {
		return classTested;
	}


	public void save(Format format, File file) {
		switch(format) {
			case CSV :
				CVSExport csv = new CVSExport(this);
				csv.save(file);
				break;
			default:
				throw new BenchmarkException("Not implemented");
		}
	}

	public void save(Format format, String file) {
		save(format, new File(file));
	}

}
