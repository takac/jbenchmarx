package net.cammann;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map.Entry;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;

import org.junit.Test;


public class WorkingBaseCaseTest {


	@Benchmark
	public int myMethod(@Fixed("1") int reps) {
		int total = 10;
		for (int i = 0; i < reps * 100; i++) {
			total += i;
		}
		return total;
	}

	@Benchmark(10)
	public int otherMethod(@Fixed("10") double reps) {
		int total = 0;
		for (int i = 0; i < reps * 1; i++) {
			total += i;
		}
		return total;
	}


	@Test
	public void testOne() {
		Benchmarker bm = new Benchmarker(WorkingBaseCaseTest.class);

		bm.execute();

		for (Entry<Method, List<Result>> e : bm.getResults().entrySet()) {
			long total = 0;
			for (Result r : e.getValue()) {
				total += r.getTime();
				System.out.println(r);
			}
			long average = total / e.getValue().size();
			System.out.println(e.getKey().getName() + " : averaged " + average + "ms");
		}
	}

}
