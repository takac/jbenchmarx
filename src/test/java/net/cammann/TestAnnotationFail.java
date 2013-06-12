package net.cammann;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Lookup;

import org.junit.Test;

public class TestAnnotationFail {

	@Lookup("alpha")
	private int alpha;

	@Benchmark
	public int benchMe() {
		return alpha;
	}


	@Test(expected = BenchmarkException.class)
	public void testFail() {
		Benchmarker.run(TestAnnotationFail.class, "benchMe");
	}

}
