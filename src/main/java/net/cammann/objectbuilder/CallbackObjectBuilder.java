package net.cammann.objectbuilder;

import net.cammann.annotations.Callback;

public class CallbackObjectBuilder implements ObjectBuilder {

	private final Callback callback;
	private final BuildContext buildContext;

	public CallbackObjectBuilder(Callback callback, BuildContext buildContext) {
		this.callback = callback;
		this.buildContext = buildContext;
	}

	@Override
	public Object get(Class<?> type) {
		String key = callback.value();
		Object obj = buildContext.callback(key);
		return obj;
	}

}
