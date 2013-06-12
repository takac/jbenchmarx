package net.cammann;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.cammann.classesToTest.ChooseConstructorTest;
import net.cammann.classesToTest.FailConstructorTest;
import net.cammann.results.MethodResult;

import org.junit.Test;

public class ChooserTest {

	@Test
	public void test() {
		List<MethodResult> results = Benchmarker.run(ChooseConstructorTest.class).getMethodResults();
		Object result = results.get(0).getReturned().get();
		System.out.println(result);
		assertEquals(19, result);
	}

	@Test(expected = BenchmarkException.class)
	public void testFailWithNoParameterSet() {
		Object result = Benchmarker.run(FailConstructorTest.class).getMethodResults().get(0);
		System.out.println(result);
		assertEquals(33, result);
	}

}
