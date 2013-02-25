package net.cammann.results;

import java.io.File;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.Arguments;
import net.cammann.BenchmarkException;
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
	public List<Arguments> getMethodsTested() {
		List<Arguments> methods = new ArrayList<Arguments>();
		for (MethodRangeResult result : resultRange.values()) {
			methods.addAll(result.getArguments());
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

	public long getMethodAverageNano(Arguments args) {
		long average = 0;
		List<MethodResult> resultSet = resultRange.get(args.getMethod()).getResults(args);
		for (MethodResult i : resultSet) {
			average += i.getTime();
		}
		return average /= resultSet.size();
	}

	@Override
	public Map<Arguments, List<MethodResult>> getMethodResults() {
		Map<Arguments, List<MethodResult>> results = new HashMap<Arguments, List<MethodResult>>();
		for (MethodRangeResult m : resultRange.values()) {
			for (Arguments i : m.getArguments()) {
				results.put(i, m.getResults(i));
			}
		}
		return results;
	}

	@Override
	public void printResult() {
		for (MethodRangeResult range : resultRange.values()) {
			for (Arguments args : range.getArguments()) {
				System.out.println(getClassTested().getName() + "." + args.getMethod().getName() + " - averaged: "
						+ getAverageTime(args) + " over " + range.getResults(args).size() + " iterations");
			}
		}

	}

	private String getAverageTime(Arguments a) {
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
	public List<MethodResult> getMethodResults(Arguments a) {
		List<MethodResult> results = resultRange.get(a.getMethod()).getResults(a);
		if (results == null) {
			return Collections.emptyList();
		}
		return results;
	}

}
