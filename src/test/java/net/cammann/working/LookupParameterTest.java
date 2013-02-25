package net.cammann.working;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cammann.Benchmarker;
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", 464);
		map.put("key2", "Villa");
		ClassResult pkg = Benchmarker.run(LookupParameterTest.class, map);

		List<List<MethodResult>> results = new ArrayList<List<MethodResult>>(pkg.getMethodResults().values());
		List<MethodResult> resultsOne = results.get(0);
		List<MethodResult> resultsTwo = results.get(1);

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
