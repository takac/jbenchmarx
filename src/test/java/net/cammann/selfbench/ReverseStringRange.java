package net.cammann.selfbench;

import net.cammann.Benchmarker;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Range;

public class ReverseStringRange {

	@Benchmark(500)
	public int reverse_recursion(@Range({ "42480" }) int n) {
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
	public int reverse_builder(@Range({ "42480" }) int n) {
		String r = new StringBuilder(Integer.toString(n, 2)).reverse().toString();
		return Integer.parseInt(r, 2);
	}

	@Benchmark(500)
	public int reverse_array(@Range({ "42480" }) int n) {
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

	public void main(String[] args) {
		Benchmarker.run(ReverseStringRange.class);
	}
}
