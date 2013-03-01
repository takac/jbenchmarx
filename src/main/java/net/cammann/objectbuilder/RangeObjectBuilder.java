package net.cammann.objectbuilder;

import net.cammann.BenchmarkUtil;
import net.cammann.annotations.Range;

public class RangeObjectBuilder implements ObjectBuilder {


	private final Range range;
	private final int rangeRound;

	public RangeObjectBuilder(Range range, int rangeRound) {
		this.range = range;
		this.rangeRound = rangeRound;
	}

	@Override
	public Object get(Class<?> type) {
		String[] rangeVals = range.value();
		int n = rangeRound % rangeVals.length;
		Object obj = BenchmarkUtil.createObjectFromString(rangeVals[n], type);
		return obj;
	}

}
