package net.cammann;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Result {

	private Method method;
	private Object[] args;
	private Long time;
	private Object returned;

	public Result(Method method, Object[] args, long time, Object returned) {
		this.method = method;
		this.args = args;
		this.time = time;
		this.returned = returned;
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
		this.returned = returned;
	}

	@Override
	public String toString() {
		if(args == null) {
			return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + time
					+ "ms, with no args, returned: " + returned;
		}
		return method.getDeclaringClass().getName() + "." + method.getName() + " took: " + time
				+ "ms, with arguments: " + Arrays.asList(args) + ", returned: " + returned;
	}
}
