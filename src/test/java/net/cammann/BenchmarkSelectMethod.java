package net.cammann;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;

import org.junit.Test;

public class BenchmarkSelectMethod {

	@Benchmark
	public int getNumber() {
		return 42;
	}

	public int getNoBenchmarkAnnotation() {
		return 42;
	}

	@Benchmark
	public int getOverload() {
		return 42;
	}
	
	@Benchmark
	public int getOverload(@Fixed("2") int x) {
		return 42;
	}
	
	@Test
	public void test() {
		Benchmarker.run(BenchmarkSelectMethod.class, "getNumber");
	}

	@Test(expected = BenchmarkException.class)
	public void testNoSuchMethod() {
		Benchmarker.run(BenchmarkSelectMethod.class, "boobies");
	}

	@Test(expected = BenchmarkException.class)
	public void testNoBenchmarkAnnotation() {
		Benchmarker.run(BenchmarkSelectMethod.class, "getNoBenchmarkAnnotation");
	}

	@Test(expected = BenchmarkException.class)
	public void testAmbigiousMethodName() {
		Benchmarker.run(BenchmarkSelectMethod.class, "getOverload");
	}

}
