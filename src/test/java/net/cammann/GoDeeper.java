package net.cammann;
import net.cammann.annotations.Benchmark;
import net.cammann.classesToTest.StringJoinExample;

import org.junit.Test;


public class GoDeeper {

	@Benchmark(2)
	public void benchmarker() {
		Benchmarker.run(StringJoinExample.class);
	}

	// Test for no strange failures.
	@Test
	public void test() {
		Benchmarker.run(GoDeeper.class);
	}

}
