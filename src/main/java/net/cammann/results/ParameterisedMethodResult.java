package net.cammann.results;

import java.util.ArrayList;
import java.util.List;

import net.cammann.ParameterisedMethod;

public class ParameterisedMethodResult {

	private ParameterisedMethod method;
	private final List<MethodResult> results = new ArrayList<MethodResult>();

	public ParameterisedMethodResult() {

	}

	public ParameterisedMethodResult(ParameterisedMethod method) {
		this.method = method;
	}


	public void recordResult(long startNanoSecs, long endNanoSecs, Object returned) {
		MethodResult r = new MethodResult(method, startNanoSecs, endNanoSecs, returned);
		results.add(r);
	}

	public void recordResult(long startNanoSecs, long endNanoSecs) {
		MethodResult r = new MethodResult(method, startNanoSecs, endNanoSecs);
		results.add(r);
	}

	public List<MethodResult> getResults() {
		return new ArrayList<MethodResult>(results);
	}

	public void addResult(MethodResult r) {
		results.add(r);
	}

}
