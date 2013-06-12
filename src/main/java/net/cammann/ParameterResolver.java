package net.cammann;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.cammann.callback.CallbackEvent;
import net.cammann.callback.CallbackHandler;
import net.cammann.callback.CallbackListener;

public class ParameterResolver {

	public ParameterResolver() {

	}

	private final CallbackHandler callbackHandler = new CallbackHandler();
	private final Map<String, Object> lookupTable = new HashMap<String, Object>();

	public void addCallback(String key, CallbackListener<?> listener) {
		callbackHandler.addCallbackListener(key, listener);
	}

	public void addLookup(String key, Object value) {
		if (key == null) {
			throw new NullPointerException("Cannot set null key");
		}
		lookupTable.put(key, value);
	}

	public Object call(String key, CallbackEvent event) {
		return callbackHandler.call(key, event);
	}

	public Object call(String key, Method method, int runNumber) {
		return callbackHandler.call(key, method, runNumber);
	}

	public Object call(String key, Field field, int runNumber) {
		return callbackHandler.call(key, field, runNumber);
	}

	public Object lookup(String key) {
		return lookupTable.get(key);
	}
}
