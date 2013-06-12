package net.cammann;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import net.cammann.classesToTest.ChooseConstructorTest;
import net.cammann.classesToTest.FailConstructorTest;
import net.cammann.results.SaveableResult;

import org.junit.Test;

public class ChooserTest {

	@Test
	public void test() {
		SaveableResult results = Benchmarker.run(ChooseConstructorTest.class);
		Method method = results.getMethodsTested().iterator().next();
		Object result = results.getMethodResult(method).get(0).getMethodResults().get(0).getReturned().get();
		System.out.println(result);
		assertEquals(19, result);
	}

	@Test(expected = BenchmarkException.class)
	public void testFailWithNoParameterSet() {
		SaveableResult results = Benchmarker.run(FailConstructorTest.class);
		Method method = results.getMethodsTested().iterator().next();
		Object result = results.getMethodResult(method).get(0).getMethodResults().get(0).getReturned();
		System.out.println(result);
		assertEquals(33, result);
	}

}
