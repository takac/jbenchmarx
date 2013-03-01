package net.cammann.results;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.cammann.Optional;
import net.cammann.ParameterisedMethod;

public class MethodResult extends SaveableResult {

	private ParameterisedMethod parameterisedMethod;
	private final long startTime;
	private final long endTime;
	private final long runtime;
	private final Optional<Object> returned = new Optional<Object>();

	public MethodResult(ParameterisedMethod args, long startNanoSeconds, long endNanoSeconds, Object returned) {
		this(args, startNanoSeconds, endNanoSeconds);
		this.returned.set(returned);
	}

	public MethodResult(ParameterisedMethod args, long startNanoSeconds, long endNanoSeconds) {
		this.parameterisedMethod = args;
		this.startTime = startNanoSeconds;
		this.endTime = endNanoSeconds;
		this.runtime = endTime - startTime;
	}

	public MethodResult(Method method, Object[] args, long startNanoSeconds, long endNanoSeconds, Object returned) {
		this(new ParameterisedMethod(method, args), startNanoSeconds, endNanoSeconds, returned);
	}

	public MethodResult(Method method, Object[] args, long startNanoSeconds, long endNanoSeconds) {
		this(new ParameterisedMethod(method, args), startNanoSeconds, endNanoSeconds);
	}

	public void setParameterisedMethod(Method method, Object[] args) {
		this.parameterisedMethod = new ParameterisedMethod(method, args);
	}

	public void setParameterisedMethod(ParameterisedMethod pm) {
		this.parameterisedMethod = pm;
	}

	public ParameterisedMethod getParameterisedMethod() {
		return parameterisedMethod;
	}

	public long getRuntime() {
		return runtime;
	}

	public void setMethod(Method method) {
		ParameterisedMethod newArguments = new ParameterisedMethod(method, parameterisedMethod.getParameters());
		this.parameterisedMethod = newArguments;
	}

	public Method getMethod() {
		return parameterisedMethod.getMethod();
	}

	public Optional<Object> getReturned() {
		return returned;
	}

	public void setReturned(Object returned) {
		this.returned.set(returned);
	}

	@Override
	public String toString() {
		String timeTaken = NumberFormat.getInstance().format(runtime) + " ns";
		String fullQualifieClassName = parameterisedMethod.getMethod().getDeclaringClass().getName();
		String methodName = parameterisedMethod.getMethod().getName();
		String arguments = parameterisedMethod.toString();

		String returnedString = null;

		if(returned.isAbsent()) {
			returnedString = "";
		} else {
			returnedString = ", Returned: " + (returned.get() == null ? "null" : returned.get().toString());
		}
		
		return fullQualifieClassName + "." + methodName + " Took: " + timeTaken + ", Arguments: " + arguments
				+ returnedString;

	}

	@Override
	public void printResult() {
		System.out.println(toString());
	}

	@Override
	public List<ParameterisedMethod> getParameterisedMethodsTested() {
		List<ParameterisedMethod> params = new ArrayList<ParameterisedMethod>();
		params.add(parameterisedMethod);
		return params;
	}

	@Override
	public List<MethodResultStore> getMethodResults(Method m) {
		if (m.equals(parameterisedMethod.getMethod())) {
			MethodResultStore rr = new MethodResultStore(m);
			if (returned.isPresent()) {
				rr.recordResult(parameterisedMethod, startTime, endTime, returned.get());
			} else {
				rr.recordResult(parameterisedMethod, startTime, endTime);
			}
			List<MethodResultStore> result = new ArrayList<MethodResultStore>();
			result.add(rr);
			return result;
		}
		return Collections.emptyList();
	}

	@Override
	public List<MethodResult> getMethodResults(ParameterisedMethod a) {
		if (a.equals(parameterisedMethod)) {
			List<MethodResult> list = new ArrayList<MethodResult>();
			list.add(this);
			return list;
		}
		return Collections.emptyList();
	}

	public long getEndTime() {
		return endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	@Override
	public List<MethodResult> getMethodResults() {
		// TODO Auto-generated method stub
		return null;
	}
}
