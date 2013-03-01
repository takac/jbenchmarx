package net.cammann;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Lookup;
import net.cammann.results.MethodResult;
import net.cammann.results.Result;

import org.junit.Test;

public class LookupFieldTest {

	@Lookup("k1")
	private float valueOne;

	@Lookup("k2")
	private String valueTwo;

	@Benchmark(3)
	private String methodOne() {
		return valueTwo;
	}

	@Benchmark(8)
	private float methodTwo() {
		return valueOne;
	}

	@Test
	public void testSetField() {
		Benchmarker.addLookup("k1", 91.2f);
		Benchmarker.addLookup("k2", "bobsled");
		Result pkg = Benchmarker.run(LookupFieldTest.class);

		ParameterisedMethod m1 = pkg.getParameterisedMethodsTested().get(0);
		ParameterisedMethod m2 = pkg.getParameterisedMethodsTested().get(1);
		List<MethodResult> resultsOne = pkg.getMethodResults(m1);
		List<MethodResult> resultsTwo = pkg.getMethodResults(m2);

		if (resultsOne.size() == 3) {
			for (MethodResult m : resultsOne) {
				assertEquals("bobsled", m.getReturned().get());
			}
			for (MethodResult m : resultsTwo) {
				assertEquals(91.2f, m.getReturned().get());
			}
		} else if (resultsOne.size() == 8) {
			for (MethodResult m : resultsOne) {
				assertEquals(91.2f, m.getReturned().get());
			}
			for (MethodResult m : resultsTwo) {
				assertEquals("bobsled", m.getReturned().get());
			}
		} else {
			fail("Not matching correct sizes");
		}
	}

	@Test(expected = NullPointerException.class)
	public void testSetNullLookup() {
		Benchmarker.addLookup("key", null);
	}

	@Test(expected = NullPointerException.class)
	public void testSetNullKeyLookup() {
		Benchmarker.addLookup(null, "value");
	}

}
