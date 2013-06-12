package net.cammann.objectbuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.cammann.ParameterResolver;
import net.cammann.callback.CallbackEvent;

public class BuildContextImpl implements BuildContext {

	private int runNumber;
	private int rangeRound;
	private Method method;
	private Field field;
	private final ParameterResolver resolver;

	public BuildContextImpl(ParameterResolver resolver) {
		this.resolver = resolver;
	}

	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}

	@Override
	public int getRunNumber() {
		return runNumber;
	}

	@Override
	public int getRangeRound() {
		return rangeRound;
	}

	public void setCaller(Method method) {
		field = null;
		this.method = method;
	}

	public Method getCallerMethod() {
		return method;
	}

	public void setCaller(Field field) {
		method = null;
		this.field = field;
	}

	public Field getCallerField() {
		return field;
	}

	public void setRangeRound(int rangeRound) {
		this.rangeRound = rangeRound;
	}

	private CallbackEvent createEvent() {
		CallbackEvent event = new CallbackEvent();
		event.setRun(runNumber);
		if (method == null) {
			event.setField(field);
		} else {
			event.setMethodTesting(method);
		}
		return event;
	}

	@Override
	public Object callback(String key) {
		return resolver.call(key, createEvent());
	}

	@Override
	public Object lookup(String key) {
		return resolver.lookup(key);
	}

}
