package net.cammann.selfbench;

import java.lang.reflect.Method;

import net.cammann.Benchmarker;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import net.cammann.results.PackageResult;

public class ReverseStringFixed {

	@Benchmark(500)
	public int reverse_recursion(@Fixed("42480") int n) {
		String out = reverse(Integer.toString(n, 2), "");
		return Integer.parseInt(out, 2);
	}

	private static String reverse(String in, String out) {
		  return (in.isEmpty()) ? out :
	            (in.charAt(0) == ' ')
	            ? out + ' ' + reverse(in.substring(1), "")
	            : reverse(in.substring(1), in.charAt(0) + out);
	}

	@Benchmark(500)
	public int reverse_builder(@Fixed("42480") int n) {
		String r = new StringBuilder(Integer.toString(n, 2)).reverse().toString();
		return Integer.parseInt(r, 2);
	}

	@Benchmark(500)
	public int reverse_array(@Fixed("42480") int n) {
		char[] chs = Integer.toString(n, 2).toCharArray();
		int i = 0, j = chs.length - 1;
		while (i < j) {
			char t = chs[i];
			chs[i] = chs[j];
			chs[j] = t;
			i++;
			j--;
		}
		return Integer.parseInt(new String(chs), 2);
		

	}

	public static void main(String[] args) {
		PackageResult run = Benchmarker.run();
		Method method = run.getMethodsTested().iterator().next();
		System.out.println(run.getMethodResult(method).get(0));
	}
}
