package net.cammann;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;

import org.junit.Test;


public class WithFieldsTest {

	@Fixed("99")
	public int field;

	@Fixed("golfing")
	public String string;
	
	@Benchmark(500)
	public int method() {
		int x = field;
		for (int i = 0; i < 99; i++) {
			x += string.length();
		}
		return x;
	}

	@Test
	public void test() {
		Benchmarker bm = new Benchmarker(WithFieldsTest.class);
		bm.execute();
		BenchmarkUtil.printResults(bm);
	}

}
