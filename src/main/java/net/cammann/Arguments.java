package net.cammann;

import java.lang.reflect.Method;

public class Arguments {

	private Method method;
	private Object[] arguments;

	public Arguments(Method method, Object[] arguments) {
		this.method = method;
		this.arguments = arguments;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Object[] getArguments() {
		return arguments;
	}

}
