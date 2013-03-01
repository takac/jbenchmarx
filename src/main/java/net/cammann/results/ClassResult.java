package net.cammann.results;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.ParameterisedMethod;

public class ClassResult extends SaveableResult {

	private final Map<Method, MethodResultStore> methodToResultMap = new HashMap<Method, MethodResultStore>();

	private Class<?> classTested;

	public ClassResult(Class<?> cls) {
		this.classTested = cls;
	}

	public ClassResult() {
	}

	public void add(MethodResultStore result) {
		if (result.getMethod() == null) {
			throw new NullPointerException("Method must be set");
		}
		methodToResultMap.put(result.getMethod(), result);
	}

	@Override
	public List<ParameterisedMethod> getParameterisedMethodsTested() {
		List<ParameterisedMethod> methods = new ArrayList<ParameterisedMethod>();
		for (MethodResultStore result : methodToResultMap.values()) {
			methods.addAll(result.getParameterisedMethodsTested());
		}
		return methods;
	}

	@Override
	public List<MethodResultStore> getMethodResults(Method m) {
		List<MethodResultStore> methods = new ArrayList<MethodResultStore>();
		for (MethodResultStore r : methodToResultMap.values()) {
			if (r.getMethod().equals(m)) {
				methods.add(r);
			}
		}
		return methods;
	}

	public long getMethodAverageNano(ParameterisedMethod args) {
		long average = 0;
		List<MethodResult> resultSet = methodToResultMap.get(args.getMethod()).getResults(args);
		for (MethodResult i : resultSet) {
			average += i.getRuntime();
		}
		return average /= resultSet.size();
	}

	@Override
	public Map<ParameterisedMethod, List<MethodResult>> getMethodResults() {
		Map<ParameterisedMethod, List<MethodResult>> results = new HashMap<ParameterisedMethod, List<MethodResult>>();
		for (MethodResultStore m : methodToResultMap.values()) {
			for (ParameterisedMethod i : m.getParameterisedMethodsTested()) {
				results.put(i, m.getResults(i));
			}
		}
		return results;
	}

	@Override
	public void printResult() {
		for (MethodResultStore range : methodToResultMap.values()) {
			for (ParameterisedMethod paramMethod : range.getParameterisedMethodsTested()) {
				String out = getParamMethodInfoString(paramMethod);
				System.out.println(out);
			}
		}
		for (MethodResultStore range : methodToResultMap.values()) {
			String out = getTotalMethodInfoString(range.getMethodAverage(), range.getMethod());
			System.out.println(out);
		}
	}

	private String getTotalMethodInfoString(AveragedResult result, Method method) {
		String avg = NumberFormat.getInstance().format(result.getAverageTime()) + " ns";
		return method.getDeclaringClass().getName()
				+ "."
				+ method.getName()
				+ " - average time: "
				+ avg
				+ "called with "
				+ result.getNumberParamCombinations()
				+ " different combination of arguments over "
				+ result.getNumIterations()
				+ " iterations";
	}

	private String getParamMethodInfoString(ParameterisedMethod paramMethod) {
		return getClassTested().getName()
				+ "."
				+ paramMethod.getMethod().getName()
				+ " args: "
				+ paramMethod
				+ " - averaged: "
				+ getAverageTime(paramMethod)
				+ " over "
				+ methodToResultMap.get(paramMethod.getMethod()).getResults(paramMethod).size()
				+ " iterations";
	}

	private String getAverageTime(ParameterisedMethod a) {
		long ns = getMethodAverageNano(a);
		return NumberFormat.getInstance().format(ns) + " ns";
	}

	public Class<?> getClassTested() {
		return classTested;
	}

	@Override
	public List<MethodResult> getMethodResults(ParameterisedMethod a) {
		List<MethodResult> results = methodToResultMap.get(a.getMethod()).getResults(a);
		if (results == null) {
			return Collections.emptyList();
		}
		return results;
	}

	public void setClassTested(Class<?> cls) {
		this.classTested = cls;
	}

}
