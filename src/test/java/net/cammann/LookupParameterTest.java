package net.cammann;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Lookup;
import net.cammann.results.ClassResult;
import net.cammann.results.MethodResult;

import org.junit.Test;

public class LookupParameterTest {

	@Benchmark(20)
	public int methodOne(@Lookup("key") int value) {
		return value;
	}

	@Benchmark(50)
	public String methodTwo(@Lookup("key2") String value) {
		return value;
	}


	@Test
	public void test() {
		Benchmarker.addLookup("key", 464);
		Benchmarker.addLookup("key2", "Villa");
		ClassResult pkg = Benchmarker.run(LookupParameterTest.class);


		ParameterisedMethod m1 = pkg.getParameterisedMethodsTested().get(0);
		ParameterisedMethod m2 = pkg.getParameterisedMethodsTested().get(1);
		List<MethodResult> resultsOne = pkg.getMethodResults(m1);
		List<MethodResult> resultsTwo = pkg.getMethodResults(m2);

		if (resultsOne.size() == 20) {
			for (MethodResult m : resultsOne) {
				assertEquals(464, m.getReturned().get());
			}
			for (MethodResult m : resultsTwo) {
				assertEquals("Villa", m.getReturned().get());
			}
		} else if (resultsOne.size() == 50 ) {
			for (MethodResult m : resultsOne) {
				assertEquals("Villa", m.getReturned().get());
			}
			for (MethodResult m : resultsTwo) {
				assertEquals(464, m.getReturned().get());
			}
		} else {
			fail("Not matching correct sizes");
		}
	}

}
