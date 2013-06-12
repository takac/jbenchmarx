package net.cammann;
import net.cammann.annotations.Benchmark;

import org.junit.Test;

public class BenchmarkWithoutSetParamTest {

	@Benchmark(10)
	public int failingMethodBadParam(double reps) {
		int total = 0;
		for (int i = 0; i < (reps * 1); i++) {
			total += i;
		}
		return total;
	}

	@Test(expected = BenchmarkException.class)
	public void testUnsetParam() {
		Benchmarker.run(BenchmarkWithoutSetParamTest.class);
	}

}
