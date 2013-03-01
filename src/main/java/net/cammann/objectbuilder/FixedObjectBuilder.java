package net.cammann.objectbuilder;

import net.cammann.BenchmarkUtil;
import net.cammann.annotations.Fixed;

public class FixedObjectBuilder implements ObjectBuilder {

	private final Fixed fixed;

	public FixedObjectBuilder(Fixed fixed) {
		this.fixed = fixed;
	}

	@Override
	public Object get(Class<?> type) {
		String var = fixed.value();
		Object obj = BenchmarkUtil.createObjectFromString(var, type);
		return obj;
	}

}
