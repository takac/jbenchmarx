package net.cammann;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import net.cammann.results.ClassResult;
import net.cammann.results.MethodResultStore;

import org.junit.Before;
import org.junit.Test;

public class ClassResultTest {

	private ClassResult classResult;

	@Before
	public void setup() {
		classResult = new ClassResult();
	}

	@Test
	public void testConstructorSetClass() {
		ClassResult cr = new ClassResult(ClassResultTest.class);
		assertEquals(ClassResultTest.class, cr.getClassTested());
	}

	@Test(expected = NullPointerException.class)
	public void testAddMethodResultRangeNullMethod() {
		MethodResultStore methodRangeResultOne = new MethodResultStore();
		classResult.add(methodRangeResultOne);
	}

	@Test(expected = NullPointerException.class)
	public void testAddNullMethodResultRange() {
		MethodResultStore methodRangeResultOne = null;
		classResult.add(methodRangeResultOne);
	}

	//add(MethodRangeResult result)

	@Test
	public void testAddMethodResultRange() throws SecurityException, NoSuchMethodException {

		Method testMethodOne = ClassResultTest.class.getDeclaredMethod("testAddMethodResultRange", new Class[]{});
		Method testMethodTwo = ClassResultTest.class.getDeclaredMethod("testConstructorSetClass", new Class[]{});

		MethodResultStore methodRangeResultOne = new MethodResultStore(testMethodOne);
		MethodResultStore methodRangeResultTwo = new MethodResultStore(testMethodTwo);

		methodRangeResultOne.recordResult(new Object[]{}, 10, 100);
		methodRangeResultTwo.recordResult(new Object[]{methodRangeResultOne}, 90, 200);

		classResult.add(methodRangeResultOne);
		classResult.add(methodRangeResultTwo);

		assertEquals(1, classResult.getMethodResult(testMethodOne).size());
		assertEquals(1, classResult.getMethodResult(testMethodTwo).size());
		assertEquals(2, classResult.getParameterisedMethodsTested().size());
		MethodResultStore returned = classResult.getMethodResult(testMethodOne).get(0);
		assertEquals(methodRangeResultOne, returned);
		assertEquals(1, returned.getMethodResults().size());
		assertEquals(90, returned.getMethodResults().get(0).getRuntime());
	}

	@Test
	public void testGetAverageTime() throws SecurityException, NoSuchMethodException {
		Method testMethod = ClassResultTest.class.getDeclaredMethod("testGetAverageTime", new Class[] {});
		MethodResultStore methodRangeResult = new MethodResultStore(testMethod);
		classResult.add(methodRangeResult);
		long total = 0;
		ParameterisedMethod pm = new ParameterisedMethod(testMethod, new Object[]{});
		for (int i = 1; i < 11; i++) {
			long start = i * 10;
			long end = i * 30;
			long diff = end - start;
			total += diff;
			methodRangeResult.recordResult(pm, start, end);
		}
		total /= 10;
		assertEquals(total, classResult.getMethodAverageNano(pm));
	}

	// +getMethodResults()
	@Test
	public void testGetAllMethodResults() throws SecurityException, NoSuchMethodException {
		assertEquals(0, classResult.getParameterisedMethodsTested().size());
		assertEquals(0, classResult.getMethodResults().size());

		Method testMethod = ClassResultTest.class.getDeclaredMethod("testGetAllMethodResults", new Class[]{});
		MethodResultStore methodResult = new MethodResultStore(testMethod);
		ParameterisedMethod pm = new ParameterisedMethod(testMethod, new Object[]{});
		methodResult.recordResult(pm, 99, 104);
		classResult.add(methodResult);
		assertEquals(1, classResult.getParameterisedMethodsTested().size());
		assertEquals(1, classResult.getMethodResults().size());
		assertEquals(5, classResult.getMethodResults().get(0).getRuntime());
	}

	// +getMethodResults(ParameterisedMethod a)
	@Test
	public void testGetMethodResultsFromParamMethod() throws SecurityException, NoSuchMethodException {
		Method testMethod = ClassResultTest.class.getDeclaredMethod("testGetMethodResultsFromParamMethod",
				new Class[]{});
		MethodResultStore methodResult = new MethodResultStore(testMethod);
		ParameterisedMethod pm = new ParameterisedMethod(testMethod, new Object[]{});
		methodResult.recordResult(pm, 10, 77);
		classResult.add(methodResult);
		assertEquals(67, classResult.getMethodResults(pm).iterator().next().getRuntime());
	}

	// +getMethodsTested()
	@Test
	public void testGetTestedMethods() {

	}

	// +save(Format format, File file)
	// +save(Format format, String filepath)
	@Test
	public void testSaveCSV() {

	}

	// -printParamMethodInfo(ParameterisedMethod paramMethod)
	// +printResult()

}
