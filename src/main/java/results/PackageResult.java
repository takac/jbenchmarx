package results;

import java.util.HashMap;
import java.util.Map;

public class PackageResult implements Result {

	Map<Class<?>, ClassResult> classResults = new HashMap<Class<?>, ClassResult>();

	public void add(ClassResult result) {
		classResults.put(result.getClassTested(), result);
	}

	public void printResult() {
		for (ClassResult cls : classResults.values()) {
			cls.printResult();
		}
	}

}
