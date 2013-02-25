package net.cammann.results;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.Arguments;
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

	@Override
	public void printResult() {
		for (ClassResult cls : classResults.values()) {
			cls.printResult();
		}
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
	public List<Method> getMethodsTested() {
		List<Method> list = new ArrayList<Method>();
		for (ClassResult cls : getClassResults()) {
			list.addAll(cls.getMethodsTested());
		}
		return list;
	}

	@Override
	public Map<Arguments, List<MethodResult>> getMethodResults() {
		Map<Arguments, List<MethodResult>> results = new HashMap<Arguments, List<MethodResult>>();
		for (ClassResult cls : classResults.values()) {
			results.putAll(cls.getMethodResults());
		}
		return results;
	}

	@Override
	public List<MethodRangeResult> getMethodResults(Method m) {
		for (ClassResult cls : classResults.values()) {
			List<MethodResult> results = cls.getMethodResults(m);
			if (results != null) {
				return results;
			}
		}
		return null;
	}

}
