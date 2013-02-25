package net.cammann.results;

import java.io.File;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.BenchmarkException;
import net.cammann.ParameterisedMethod;
import net.cammann.export.CVSExport;
import net.cammann.export.Format;
import net.cammann.export.Saveable;

public class ClassResult implements Result, Saveable {

	private final Map<Method, MethodRangeResult> resultRange = new HashMap<Method, MethodRangeResult>();

	private final Class<?> classTested;

	public ClassResult(Class<?> cls) {
		this.classTested = cls;
	}

	public void add(MethodRangeResult result) {
		resultRange.put(result.getMethod(), result);
	}

	@Override
	public List<ParameterisedMethod> getMethodsTested() {
		List<ParameterisedMethod> methods = new ArrayList<ParameterisedMethod>();
		for (MethodRangeResult result : resultRange.values()) {
			methods.addAll(result.getParameterisedMethodsTested());
		}
		return methods;
	}

	@Override
	public List<MethodRangeResult> getMethodResults(Method m) {
		List<MethodRangeResult> methods = new ArrayList<MethodRangeResult>();
		for (MethodRangeResult r : resultRange.values()) {
			if (r.getMethod().equals(m)) {
				methods.add(r);
			}
		}
		return methods;
	}

	public long getMethodAverageNano(ParameterisedMethod args) {
		long average = 0;
		List<MethodResult> resultSet = resultRange.get(args.getMethod()).getResults(args);
		for (MethodResult i : resultSet) {
			average += i.getRuntime();
		}
		return average /= resultSet.size();
	}

	@Override
	public Map<ParameterisedMethod, List<MethodResult>> getMethodResults() {
		Map<ParameterisedMethod, List<MethodResult>> results = new HashMap<ParameterisedMethod, List<MethodResult>>();
		for (MethodRangeResult m : resultRange.values()) {
			for (ParameterisedMethod i : m.getParameterisedMethodsTested()) {
				results.put(i, m.getResults(i));
			}
		}
		return results;
	}

	@Override
	public void printResult() {
		for (MethodRangeResult range : resultRange.values()) {
			for (ParameterisedMethod paramMethod : range.getParameterisedMethodsTested()) {
				System.out.println(getClassTested().getName() 
						+ "." 
						+ paramMethod.getMethod().getName() 
						+ " args: "
						+ paramMethod
						+ " - averaged: "
						+ getAverageTime(paramMethod) 
						+ " over " 
						+ range.getResults(paramMethod).size() 
						+ " iterations");
			}
		}

	}

	private String getAverageTime(ParameterisedMethod a) {
		long ns = getMethodAverageNano(a);
		return NumberFormat.getInstance().format(ns) + " ns";
	}

	public Class<?> getClassTested() {
		return classTested;
	}

	@Override
	public File save(Format format, File file) {
		switch (format) {
			case CSV :
				CVSExport csv = new CVSExport(this);
				csv.save(file);
				break;
			default :
				throw new BenchmarkException("Not implemented");
		}
		return file;
	}

	@Override
	public File save(Format format, String filepath) {
		File file = new File(filepath);
		save(format, file);
		return file;
	}

	@Override
	public List<MethodResult> getMethodResults(ParameterisedMethod a) {
		List<MethodResult> results = resultRange.get(a.getMethod()).getResults(a);
		if (results == null) {
			return Collections.emptyList();
		}
		return results;
	}

}
