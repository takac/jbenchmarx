package net.cammann;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map.Entry;

public class BenchmarkUtil {

	public static void printResults(Benchmarker bm) {
		for (Entry<Method, List<Result>> e : bm.getResults().entrySet()) {
			long total = 0;
			for (Result r : e.getValue()) {
				total += r.getTime();
				System.out.println(r);
			}
			long average = total / e.getValue().size();
			System.out.println(e.getKey().getName() + " : averaged " + average + "ms");
		}

	}

}
