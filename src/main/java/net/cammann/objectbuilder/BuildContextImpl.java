package net.cammann.objectbuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import net.cammann.callback.CallbackEvent;
import net.cammann.callback.CallbackHandler;

public class BuildContextImpl implements BuildContext {

	private int runNumber;
	private int rangeRound;
	private CallbackHandler callbackHandler;
	private Map<String, Object> lookupMap;
	private Method method;
	private Field field;

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

	public void setLookupMap(Map<String, Object> lookupMap) {
		this.lookupMap = lookupMap;
	}

	public Map<String, Object> getLookupMap() {
		return lookupMap;
	}

	public void setCallbackHandler(CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}

	public CallbackHandler getCallbackHandler() {
		return callbackHandler;
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
		return callbackHandler.call(key, createEvent());
	}

	@Override
	public Object lookup(String key) {
		return lookupMap.get(key);
	}

}
