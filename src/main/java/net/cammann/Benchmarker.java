package net.cammann;

import results.PackageResult;

public class Benchmarker {

	public static void run(Class<?>... classes) {
		PackageResult pkg = new PackageResult();
		for (Class<?> cls : classes) {
			ClassBenchmarker bm = new ClassBenchmarker(cls);
			bm.execute();
			pkg.add(bm.getResult());
		}
		pkg.printResult();
	}
	
}
