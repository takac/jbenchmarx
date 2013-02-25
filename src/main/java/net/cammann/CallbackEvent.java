package net.cammann;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CallbackEvent {

	private int run;
	private Method method;
	private Field field;

	public CallbackEvent() {
	}

	public int getRun() {
		return run;
	}

	public void setRun(int run) {
		this.run = run;
	}

	public Method getMethodTesting() {
		return method;
	}

	public void setMethodTesting(Method method) {
		this.method = method;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
