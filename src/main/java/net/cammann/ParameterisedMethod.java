package net.cammann;

import java.lang.reflect.Method;

public class ParameterisedMethod {

	private Method method;
	private Object[] parameters;

	public ParameterisedMethod(Method method, Object[] parameters) {
		this.method = method;
		this.parameters = parameters;
	}

	public ParameterisedMethod() {
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public void setParameters(Object[] arguments) {
		this.parameters = arguments;
	}

	public Object[] getParameters() {
		return parameters;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass().equals(ParameterisedMethod.class)) {
			ParameterisedMethod args = (ParameterisedMethod) obj;
			if (args.method.equals(method)) {
				if((parameters == null && args.parameters == null)) {
					return true;
				}
				if(args.parameters.length == parameters.length) {
					for (int i = 0; i < parameters.length; i++) {
						if (!args.parameters[i].equals(parameters[i])) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int n = 12591222;
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				n += parameters[i].hashCode();
			}
		}
		n += method.hashCode();
		return n;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		if (parameters == null) {
			sb.append("}");
			return sb.toString();
		}
		for (Object i : parameters) {
			sb.append(i.toString()).append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" }");
		return sb.toString();
	}

}
