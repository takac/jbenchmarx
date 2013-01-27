## JBenchmarx

### Benchmarking utility to help you get the most from your app!

###Usage:

	Mark any method you would like to benchmark with a `@Benchmark` annotation,
	then create a new class to execute your Benchmark, and call:

		Benchmarker.run(YourClassToBenchmark.class);

	This will return you a simple output similar too:

		your.package.YourClass.yourMethod took: 4ms, with no args, returned: 45
		your.package.YourClass.yourMethod : averaged 4ms

	You can run tell JBenchmarx how many times to run the method by just adding
	a number inside the annotation `@Benchmark(5)`.

		your.package.YourClass.yourMethod took: 4ms, with no args, returned: 45
		your.package.YourClass.yourMethod took: 4ms, with no args, returned: 45
		your.package.YourClass.yourMethod took: 3ms, with no args, returned: 45
		your.package.YourClass.yourMethod took: 3ms, with no args, returned: 45
		yourMethod : averaged 14ms

	You can specify parameters and fields for your methods:

		@Fixed("helloString")
		private String myFavouriteString;

		@Benchmark(10)
		public double yourMethod(@Fixed("10") double mult) {
			double total = 0;
			for (int i = 0; i < 99; i++) {
				total += mult + i + myFavouriteString.length();
			}
			return total;
		}

	The `@Fixed` annotation uses the string constructor of the method, so can be
	used for many classes.


		your.package.YourClass.yourMethod took: 12ms, with arguments: [10.0], returned: 6930.0
		your.package.YourClass.yourMethod took: 6ms, with arguments: [10.0], returned: 6930.0
		your.package.YourClass.yourMethod took: 3ms, with arguments: [10.0], returned: 6930.0
		.
		.
		.
		your.package.YourClass.yourMethod took: 2ms, with arguments: [10.0], returned: 6930.0
		your.package.YourClass.yourMethod : averaged 3ms

	Check out the test packages for more info on how to use JBenchmarx
