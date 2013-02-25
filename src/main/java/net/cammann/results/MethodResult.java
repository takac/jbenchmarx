package net.cammann.results;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.Arguments;
import net.cammann.Optional;

public class MethodResult implements Result {

	private Method method;
	private Arguments args;
	private Long time;
	private final Optional<Object> returned = new Optional<Object>();

	public MethodResult(Method method, Arguments args, long time, Object returned) {
		this(method, args, time);
		this.returned.set(returned);
	}

	public MethodResult(Method method, Arguments args, long time) {
		this.method = method;
		this.args = args;
		this.time = time;
	}

	public MethodResult(Method method, Object[] args, long time, Object returned) {
		this(method, args, time);
		this.returned.set(returned);
	}

	public MethodResult(Method method, Object[] args, long time) {
		this.method = method;
		this.args = new Arguments(method, args);
		this.time = time;
	}

	public void setArguments(Object[] args) {
		this.args = new Arguments(method, args);
	}

	public void setArguments(Arguments arguments) {
		this.args = arguments;
	}

	public Arguments getArguments() {
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

	public Optional<Object> getReturned() {
		return returned;
	}

	public void setReturned(Object returned) {
		this.returned.set(returned);
	}

	@Override
	public String toString() {
		String timeTaken = NumberFormat.getInstance().format(time) + " ns";
		if ((args == null) && returned.isAbsent()) {
			return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + timeTaken
					+ ", with no args";
		} else if (args == null) {
			return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + timeTaken
					+ ", with no args, returned: " + returned.get();
		} else if (returned.isAbsent()) {
			return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + timeTaken
					+ ", with arguments: " + Arrays.asList(args);
		}
		return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + timeTaken
				+ ", with arguments: " + Arrays.asList(args) + ", returned: " + returned.get();
	}

	@Override
	public void printResult() {
		System.out.println(toString());
	}

	@Override
	public List<Method> getMethodsTested() {
		return new ArrayList<Method>() {
			{
				add(method);
			}
		};
	}

	@Override
	public Map<Arguments, List<MethodResult>> getMethodResults() {
		final List<MethodResult> result = new ArrayList<MethodResult>();
		return new HashMap<Arguments, List<MethodResult>>() {
			{
				put(args, result);
			}
		};
	}

	@Override
	public List<MethodRangeResult> getMethodResults(Method m) {
		if (m.equals(method)) {
			MethodRangeResult rr = new MethodRangeResult(m);
			if (returned.isPresent()) {
				rr.recordResult(args, time, returned.get());
			} else {
				rr.recordResult(args, time);
			}
			List<MethodRangeResult> result = new ArrayList<MethodRangeResult>();
			result.add(rr);
			return result;
		}
		return null;
	}
}
