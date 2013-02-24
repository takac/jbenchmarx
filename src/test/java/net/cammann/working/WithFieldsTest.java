package net.cammann.working;

import net.cammann.ClassBenchmarker;
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
		ClassBenchmarker bm = new ClassBenchmarker(WithFieldsTest.class);
		bm.execute();
		bm.getResult().printResult();
	}

}
