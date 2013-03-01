package net.cammann;

import net.cammann.annotations.Benchmark;
import net.cammann.results.Result;

import org.junit.Test;

public class BenchmarkSelectMethod {

	@Benchmark
	public int getNumber() {
		return 42;
	}

	@Test
	public void test() {
		Result r = Benchmarker.run(BenchmarkSelectMethod.class, "getNumber");
	}

}
