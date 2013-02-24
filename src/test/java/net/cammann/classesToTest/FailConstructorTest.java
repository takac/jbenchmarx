package net.cammann.classesToTest;

import net.cammann.annotations.BenchConstructor;
import net.cammann.annotations.Benchmark;

public class FailConstructorTest {

	private int field = 20;

	public FailConstructorTest() {
		field = 33;
	}

	@BenchConstructor
	public FailConstructorTest(int x) {
		field = x;
	}

	@Benchmark
	public int method() {
		return field;
	}
}
