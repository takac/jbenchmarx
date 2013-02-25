package net.cammann.results;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.cammann.Arguments;

public interface Result {

	void printResult();
	List<Arguments> getMethodsTested();
	List<MethodRangeResult> getMethodResults(Method m);
	List<MethodResult> getMethodResults(Arguments a);
	Map<Arguments, List<MethodResult>> getMethodResults();

}
