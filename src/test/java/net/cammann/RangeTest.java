package net.cammann;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Range;
import net.cammann.results.MethodResult;
import net.cammann.results.Result;

import org.junit.Test;

public class RangeTest {

	@Benchmark(10)
	private int method(@Range({"1", "5", "30"}) int x) {
		return x;
	}

	@Test
	public void test() {
		Result pkg = Benchmarker.run(RangeTest.class);

		List<ParameterisedMethod> tested = pkg.getParameterisedMethodsTested();

		for (int i = 0; i < tested.size(); i++) {
			List<MethodResult> methods = pkg.getMethodResults(tested.get(i));
			assertEquals(10, methods.size());
		}

		int one = (Integer) tested.get(0).getParameters()[0];
		int two = (Integer) tested.get(1).getParameters()[0];
		int three = (Integer) tested.get(2).getParameters()[0];
		if(one == 1) {
			if(two == 5) {
				assertEquals(30, three);
			} else {
				assertEquals(30, two);
				assertEquals(5, three);
			}
		} else if (one == 5) {
			if (two == 30) {
				assertEquals(1, three);
			} else {
				assertEquals(30, three);
				assertEquals(1, two);
			}
		} else if (one == 30) {
			if (two == 5) {
				assertEquals(1, three);
			} else {
				assertEquals(5, three);
				assertEquals(1, two);
			}
		} else {
			fail("not correct argumetns");
		}
	}

}
