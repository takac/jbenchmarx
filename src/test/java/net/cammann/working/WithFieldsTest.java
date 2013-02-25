package net.cammann.working;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.cammann.Benchmarker;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import net.cammann.results.MethodResult;
import net.cammann.results.PackageResult;

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
		PackageResult pkg = Benchmarker.run(WithFieldsTest.class);
		List<List<MethodResult>> results = new ArrayList<List<MethodResult>>(pkg.getMethodResults().values());
		List<MethodResult> resultsOne = results.get(0);
		List<MethodResult> resultsTwo = results.get(1);

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
