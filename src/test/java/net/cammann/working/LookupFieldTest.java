package net.cammann.working;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.cammann.Benchmarker;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Lookup;
import net.cammann.results.ClassResult;
import net.cammann.results.MethodResult;

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
		ClassResult pkg = Benchmarker.run(LookupFieldTest.class);

		List<List<MethodResult>> results = new ArrayList<List<MethodResult>>(pkg.getMethodResults().values());
		List<MethodResult> resultsOne = results.get(0);
		List<MethodResult> resultsTwo = results.get(1);

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

}
