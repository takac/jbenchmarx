package net.cammann.selfbench;

import net.cammann.Benchmarker;
import net.cammann.annotations.Benchmark;
import net.cammann.callback.CallbackEvent;
import net.cammann.callback.CallbackListener;

public class Caller {
	public static void main(String[] args) {
		Benchmarker.addLookup("n", 42480);
		Benchmarker.addCallback("n", new CallbackListener<Integer>() {

			@Override
			public Integer callback(CallbackEvent event) {
				return 42480;
			}
		});

		Benchmarker.run(Caller.class);
	}

	@Benchmark(100)
	public void range() {
		Benchmarker.run(ReverseStringRange.class);
	}

	@Benchmark(100)
	public void fixed() {
		Benchmarker.run(ReverseStringFixed.class);
	}

	@Benchmark(100)
	public void lookup() {
		Benchmarker.run(ReverseStringLookup.class);
	}

	@Benchmark(100)
	public void callback() {
		Benchmarker.run(ReverseStringCallback.class);
	}
}
