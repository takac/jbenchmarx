package net.cammann.results;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.cammann.ParameterisedMethod;

public interface Result {

	void printResult();
	List<ParameterisedMethod> getMethodsTested();
	List<MethodRangeResult> getMethodResults(Method m);
	List<MethodResult> getMethodResults(ParameterisedMethod a);
	Map<ParameterisedMethod, List<MethodResult>> getMethodResults();

}
