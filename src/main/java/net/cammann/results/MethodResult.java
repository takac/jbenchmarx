package net.cammann.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.Optional;

public class MethodResult implements Result {

	private Method method;
	private Object[] args;
	private Long time;
	private final Optional<Object> returned = new Optional<Object>();

	public MethodResult(Method method, Object[] args, long time, Object returned) {
		this.method = method;
		this.args = args;
		this.time = time;
		this.returned.set(returned);
	}

	public MethodResult(Method method, Object[] args, long time) {
		this.method = method;
		this.args = args;
		this.time = time;
	}

	public void setArguments(Object[] args) {
		this.args = args;
	}

	public Object[] getArguments() {
		return args;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public Object getReturned() {
		return returned;
	}

	public void setReturned(Object returned) {
		this.returned.set(returned);
	}

	@Override
	public String toString() {
		if (args == null && returned.isAbsent()) {
			return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + time
					+ "ns, with no args";
		} else if (args == null) {
			return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + time
					+ "ns, with no args, returned: " + returned.get();
		} else if (returned.isAbsent()) {
			return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + time
					+ "ns, with arguments: " + Arrays.asList(args);
		}
		return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + time
				+ "ns, with arguments: " + Arrays.asList(args) + ", returned: " + returned.get();
	}

	public void printResult() {
		System.out.println(toString());
	}

	public List<Method> getMethodsTested() {
		return new ArrayList<Method>() {{ add(method); }};
	}

	public Map<Method, List<MethodResult>> getMethodResults() {
		final List<MethodResult> result = new ArrayList<MethodResult>();
		return new HashMap<Method, List<MethodResult>>() {{ put(method, result); }};
	}

	public List<MethodResult> getMethodResults(Method m) {
		if(m.equals(method)) {
			return new ArrayList<MethodResult>() {{ add(MethodResult.this); }};
		} else {
			return null;
		}
	}
}
