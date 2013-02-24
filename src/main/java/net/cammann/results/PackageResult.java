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

public class PackageResult implements Result, Saveable {

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

	public void printResult() {
		for (ClassResult cls : classResults.values()) {
			cls.printResult();
		}
	}

	public void save(Format format, File file) {
		switch (format) {
			case CSV :
				CVSExport csv = new CVSExport(this);
				csv.save(file);
				break;
			default :
				throw new BenchmarkException("Not implemented");
		}
	}

	public void save(Format format, String file) {
		save(format, new File(file));
	}

	public List<Method> getMethodsTested() {
		List<Method> list = new ArrayList<Method>();
		for (ClassResult cls : getClassResults()) {
			list.addAll(cls.getMethodsTested());
		}
		return list;
	}

	public Map<Method, List<MethodResult>> getMethodResults() {
		Map<Method, List<MethodResult>> results = new HashMap<Method, List<MethodResult>>();
		for (ClassResult cls : classResults.values()) {
			results.putAll(cls.getMethodResults());
		}
		return results;
	}

	public List<MethodResult> getMethodResults(Method m) {
		for (ClassResult cls : classResults.values()) {
			List<MethodResult> results = cls.getMethodResults(m);
			if (results != null) {
				return results;
			}
		}
		return null;
	}

}
