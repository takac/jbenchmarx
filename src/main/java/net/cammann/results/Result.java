package net.cammann.results;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface Result {

	void printResult();
	List<Method> getMethodsTested();
	List<MethodResult> getMethodResults(Method m);
	Map<Method, List<MethodResult>> getMethodResults();

}
