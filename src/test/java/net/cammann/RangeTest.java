package net.cammann;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Range;
import net.cammann.results.MethodResult;
import net.cammann.results.PackageResult;

import org.junit.Test;

public class RangeTest {

	@Benchmark(10)
	private int method(@Range({"1", "5", "30"}) int x) {
		return x;
	}

	@Test
	public void test() {
		PackageResult pkg = Benchmarker.run(RangeTest.class);

		Map<ParameterisedMethod, List<MethodResult>> results = pkg.getMethodResults();
		for (List<MethodResult> methods : results.values()) {
			assertEquals(10, methods.size());
		}
		Iterator<ParameterisedMethod> args = results.keySet().iterator();
		int one = (Integer) args.next().getParameters()[0];
		int two = (Integer) args.next().getParameters()[0];
		int three = (Integer) args.next().getParameters()[0];
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
