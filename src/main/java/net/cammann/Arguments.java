package net.cammann;

import java.lang.reflect.Method;

public class Arguments {

	private Method method;
	private Object[] arguments;

	public Arguments(Method method, Object[] arguments) {
		this.method = method;
		this.arguments = arguments;
	}

	public Arguments() {
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass().equals(Arguments.class)) {
			Arguments args = (Arguments) obj;
			if (args.method.equals(method)) {
				if((arguments == null && args.arguments == null)) {
					return true;
				}
				if(args.arguments.length == arguments.length) {
					for (int i = 0; i < arguments.length; i++) {
						if (!args.arguments[i].equals(arguments[i])) {
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
		if (arguments != null) {
			for (int i = 0; i < arguments.length; i++) {
				n += arguments[i].hashCode();
			}
		}
		n += method.hashCode();
		return n;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		if (arguments == null) {
			sb.append("}");
			return sb.toString();
		}
		for (Object i : arguments) {
			sb.append(i.toString()).append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" }");
		return sb.toString();
	}

}
