package net.cammann.working;
import net.cammann.Benchmarker;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;

import org.junit.Test;

public class WorkingBaseCaseTest {

	// @Benchmark
	public int myMethod(@Fixed("1") int reps) {
		int total = 10;
		for (int i = 0; i < (reps * 100); i++) {
			total += i;
		}

		return total;
	}

	// @Benchmark(10)
	public int otherMethod(@Fixed("10") double reps) {
		int total = 0;
		for (int i = 0; i < (reps * 1); i++) {
			total += i;
		}
		return total;
	}

	@Fixed("helloString")
	private String myFavouriteString;

	@Benchmark(200)
	public double yourMethod(@Fixed("100") double mult) {
		double total = 0;
		for (int i = 0; i < 99; i++) {
			total += mult + i + myFavouriteString.length();
		}
		return total;
	}

	@Test
	public void testOne() {
		Benchmarker.run(WorkingBaseCaseTest.class);
	}

}
