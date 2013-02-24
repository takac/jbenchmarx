package net.cammann.working;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.cammann.BenchmarkException;
import net.cammann.Benchmarker;
import net.cammann.classesToTest.ChooseConstructorTest;
import net.cammann.classesToTest.FailConstructorTest;
import net.cammann.results.MethodResult;

import org.junit.Test;

public class ChooserTest{

	@Test
	public void test() {
		Map<Method, List<MethodResult>> results = Benchmarker.run(ChooseConstructorTest.class).getMethodResults();
		Object result = results.values().iterator().next().get(0).getReturned().get();
		System.out.println(result);
		assertEquals(19, result);
	}

	@Test(expected = BenchmarkException.class)
	public void testFailWithNoParameterSet() {
		Object result = Benchmarker.run(FailConstructorTest.class).getMethodResults().get(0).get(0).getReturned();
		System.out.println(result);
		assertEquals(33, result);
	}


}
