package net.cammann.callback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.cammann.BenchmarkException;

public class CallbackHandler {

	private final Map<String, CallbackListener<?>> map = new HashMap<String, CallbackListener<?>>();


	public void addCallbackListener(String key, CallbackListener<?> cbl) {
		map.put(key, cbl);
	}

	public Object call(String key, CallbackEvent event) {
		CallbackListener<?> listener = map.get(key);
		if (listener == null) {
			throw new BenchmarkException("No such call back for key: " + key);
		}
		return listener.callback(event);
	}

	public Object call(String key, Method method, int runNumber) {
		CallbackEvent event = new CallbackEvent();
		event.setMethodTesting(method);
		event.setRun(runNumber);
		return call(key, event);
	}

	public Object call(String key, Field field, int runNumber) {
		CallbackEvent event = new CallbackEvent();
		event.setField(field);
		event.setRun(runNumber);
		return call(key, event);
	}

}
