package net.cammann.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.ParameterisedMethod;

public class PackageResult extends SaveableResult {

	Map<Class<?>, ClassResult> classResults = new HashMap<Class<?>, ClassResult>();

	public void add(ClassResult result) {
		classResults.put(result.getClassTested(), result);
	}

	public ClassResult getClassResult(Class<?> cls) {
		return classResults.get(cls);
	}

	public List<ClassResult> getClassResults() {
		return new ArrayList<ClassResult>(classResults.values());
	}

	@Override
	public void printResult() {
		for (ClassResult cls : classResults.values()) {
			cls.printResult();
		}
	}

	@Override
	public List<ParameterisedMethod> getParameterisedMethodsTested() {
		List<ParameterisedMethod> list = new ArrayList<ParameterisedMethod>();
		for (ClassResult cls : getClassResults()) {
			list.addAll(cls.getParameterisedMethodsTested());
		}
		return list;
	}

	@Override
	public Map<ParameterisedMethod, List<MethodResult>> getMethodResults() {
		Map<ParameterisedMethod, List<MethodResult>> results = new HashMap<ParameterisedMethod, List<MethodResult>>();
		for (ClassResult cls : classResults.values()) {
			results.putAll(cls.getMethodResults());
		}
		return results;
	}

	@Override
	public List<MethodResultStore> getMethodResults(Method m) {
		for (ClassResult cls : classResults.values()) {
			List<MethodResultStore> results = cls.getMethodResults(m);
			return results;
		}
		return Collections.emptyList();
	}

	@Override
	public List<MethodResult> getMethodResults(ParameterisedMethod a) {
		List<MethodResult> list = new ArrayList<MethodResult>();
		for (ClassResult cr : classResults.values()) {
			list.addAll(cr.getMethodResults(a));
		}
		return list;
	}


}
