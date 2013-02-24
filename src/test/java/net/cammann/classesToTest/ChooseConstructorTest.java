package net.cammann.classesToTest;

import net.cammann.annotations.BenchConstructor;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Fixed;

public class ChooseConstructorTest {

	private int field = 20;

	public ChooseConstructorTest() {
		field = 33;
	}

	@BenchConstructor
	public ChooseConstructorTest(@Fixed("19") int x) {
		field = x;
	}

	public ChooseConstructorTest(float x) {
		field = (int) x;
	}

	@Benchmark
	public int method() {
		return field;
	}
}
