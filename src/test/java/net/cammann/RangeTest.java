package net.cammann;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Range;

import org.junit.Test;

public class RangeTest {

	@Benchmark(10)
	private int method(@Range({"1", "5", "30"}) int x) {
		return x;
	}

	@Test
	public void test() {
		Benchmarker.run(Range.class);
	}

}
