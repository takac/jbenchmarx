package net.cammann.objectbuilder;

import net.cammann.annotations.Lookup;

public class LookupObjectBuilder implements ObjectBuilder {

	private final BuildContext ctx;
	private final Lookup lookup;

	public LookupObjectBuilder(Lookup lookup, BuildContext ctx) {
		this.ctx = ctx;
		this.lookup = lookup;
	}

	@Override
	public Object get(Class<?> type) {
		return ctx.lookup(lookup.value());
	}

}
