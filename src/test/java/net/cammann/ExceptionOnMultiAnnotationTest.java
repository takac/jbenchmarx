package net.cammann;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.Lookup;

import org.junit.Test;

public class ExceptionOnMultiAnnotationTest {

	@Benchmark
	public int run(@Fixed("1") @Lookup("k") int x) {
		return 42;
	}

	@Test(expected = BenchmarkException.class)
	public void test() {
		Benchmarker.run(ExceptionOnMultiAnnotationTest.class);
	}

}
