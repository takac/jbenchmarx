package net.cammann.working;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.cammann.Benchmarker;
import net.cammann.annotations.BenchConstructor;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.NoReturn;
import net.cammann.export.Format;

public class StringJoinExample {

	private final List<String> stringList;
	private final Set<String> stringSet;
	private static int NUM_STRINGS;


	public StringJoinExample() {
		stringList = new ArrayList<String>();
		stringSet = new HashSet<String>();
		for (int i = 0; i < NUM_STRINGS; i++) {
			stringList.add(String.valueOf(i));
			stringSet.add(String.valueOf(i));
		}
	}

	@BenchConstructor
	public StringJoinExample(@Fixed("10000000") int num) {
		stringList = new ArrayList<String>();
		stringSet = new HashSet<String>();
		for (int i = 0; i < NUM_STRINGS; i++) {
			stringList.add(String.valueOf(i));
			stringSet.add(String.valueOf(i));
		}
	}

	@Benchmark(10)
	@NoReturn
	public String concatList() {
		String out = "";
		for (String i : stringList) {
			out += i;
		}
		return out;
	}

	@Benchmark(25) @NoReturn
	public String stringBuildJoinList() {
		StringBuilder sb = new StringBuilder();
		for (String i : stringList) {
			sb.append(i);
		}
		return sb.toString();
	}

	@Benchmark(30)
	@NoReturn
	public String stringBufferJoinList() {
		StringBuffer sb = new StringBuffer();
		for (String i : stringList) {
			sb.append(i);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		Benchmarker.run(StringJoinExample.class).save(Format.CSV, "stirngJoin.csv");
//		Benchmarker.run(StringJoinExample.class.getPackage()).save(Format.CSV, "stringJoin.csv");
	}
}
