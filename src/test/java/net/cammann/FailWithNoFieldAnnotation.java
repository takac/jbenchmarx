package net.cammann;

import net.cammann.annotations.Benchmark;

import org.junit.Test;


public class FailWithNoFieldAnnotation {

	private int total;

	@Benchmark(10)
	public int failingMethodBadParam(double reps) {
		for (int i = 0; i < reps * 1; i++) {
			total += i;
		}
		return total;
	}

	@Test
	// (expected = BenchmarkException.class)
	public void test() {
		Benchmarker bm = new Benchmarker(WithFieldsTest.class);
		bm.execute();
		BenchmarkUtil.printResults(bm);
	}

}
