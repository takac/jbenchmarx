package net.cammann.results;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import net.cammann.ParameterisedMethod;
import net.cammann.export.Saveable;

public interface Result extends Saveable {

	void printResult();
	List<ParameterisedMethod> getParameterisedMethodsTested();
	List<MethodResultStore> getMethodResult(Method m);
	List<MethodResult> getMethodResults(ParameterisedMethod a);

	// List<MethodResult> getMethodResults();
	Set<Method> getMethodsTested();

}
