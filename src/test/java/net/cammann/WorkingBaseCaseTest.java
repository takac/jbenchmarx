package net.cammann;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.Range;
import net.cammann.classesToTest.StringJoinExample;

import org.junit.Test;

public class WorkingBaseCaseTest {

	@Benchmark
	public int myMethod(@Fixed("1") int reps) {
		int total = 10;
		for (int i = 0; i < (reps * 100); i++) {
			total += i;
		}

		return total;
	}

	@Benchmark(10)
	public int otherMethod(@Range({"10", "2", "99.992"}) double reps) {
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

	// Test normal functionality
	@Test
	public void testOne() {
		Benchmarker.run(WorkingBaseCaseTest.class);
		Benchmarker.run(StringJoinExample.class);
	}

}
