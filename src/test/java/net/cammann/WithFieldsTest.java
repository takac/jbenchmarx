package net.cammann;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import net.cammann.results.ClassResult;
import net.cammann.results.MethodResult;

import org.junit.Test;

public class WithFieldsTest {

	@Fixed("99")
	public int field;

	@Fixed("golfing")
	public String string;

	@Benchmark(500)
	public int intMethod() {
		return field;
	}

	@Benchmark(200)
	public String stringMethod() {
		return string;
	}

	@Test
	public void test() {
		ClassResult pkg = Benchmarker.run(WithFieldsTest.class);

		ParameterisedMethod m1 = pkg.getParameterisedMethodsTested().get(0);
		ParameterisedMethod m2 = pkg.getParameterisedMethodsTested().get(1);
		List<MethodResult> resultsOne = pkg.getMethodResults(m1);
		List<MethodResult> resultsTwo = pkg.getMethodResults(m2);

		if (resultsOne.size() == 200) {
			for (MethodResult m : resultsOne) {
				assertEquals("golfing", m.getReturned().get());
			}
			for (MethodResult m : resultsTwo) {
				assertEquals(99, m.getReturned().get());
			}
		} else if (resultsOne.size() == 500) {
			for (MethodResult m : resultsOne) {
				assertEquals(99, m.getReturned().get());
			}
			for (MethodResult m : resultsTwo) {
				assertEquals("golfing", m.getReturned().get());
			}
		} else {
			fail("Not matching correct sizes");
		}

	}

}
