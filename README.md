## JBenchmarx

### Benchmarking utility to help you get the most from your app!

###Usage:

Mark any method you would like to benchmark with a `@Benchmark` annotation,
then create a new class to execute your Benchmark, and call:

	Benchmarker.run(YourClassToBenchmark.class);

This will return you a simple output similar too:

	your.package.YourClass.yourMethod took: 4ns, with no args, returned: 45
	your.package.YourClass.yourMethod : averaged 4ns

You can run tell JBenchmarx how many times to run the method by just adding
a number inside the annotation `@Benchmark(5)`.

	your.package.YourClass.yourMethod took: 5,000ns, with no args, returned: 45
	your.package.YourClass.yourMethod took: 5,000ns, with no args, returned: 45
	your.package.YourClass.yourMethod took: 5,000ns, with no args, returned: 45
	your.package.YourClass.yourMethod took: 3,000ns, with no args, returned: 45
	your.package.YourClass.yourMethod took: 2,000ns, with no args, returned: 45
	yourMethod : averaged 4,000 ns

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


	your.package.YourClass.yourMethod took: 12ns, with arguments: [10.0], returned: 6930.0
	your.package.YourClass.yourMethod took: 6ns, with arguments: [10.0], returned: 6930.0
	your.package.YourClass.yourMethod took: 3ns, with arguments: [10.0], returned: 6930.0
	.
	.
	.
	your.package.YourClass.yourMethod took: 2ns, with arguments: [10.0], returned: 6930.0
	your.package.YourClass.yourMethod : averaged 3ns

The `@Range` annotation allows you to specify a range of values in your method parameters.

	@Benchmark
	public int method(@Range({"5", "6", "7"}) int x) {
		return x * 5;
	}

This will produce - 

	your.package.YourClass.method took: 2100ns, with arguments: { 5 }, returned: 25
	your.package.YourClass.method took: 1200ns, with arguments: { 6 }, returned: 30
	your.package.YourClass.method took: 1200ns, with arguments: { 7 }, returned: 35

The `@Lookup` annoation allows you to set the value of a field or parameter at run time.

	@Lookup("value_key")
	private int value;
	
	@Benchmark
	public int method(@Lookup("x_key") int x) {
		return x * 5;
	}
	
	public static void main(String[] args) {
		Map<String, Object> lookupTable = new HashMap<String, Object>();
		lookupTable.put("value_key", 3);
		lookupTable.put("x_key", 8);
		Benchmarker.run(YourClassToBenchmark.class, lookupTable);
	}

This will produce - 

	your.package.YourClass.method took: 1200ns, with arguments: { 8 }, returned: 24


You can also save the results of your benchmark -

	Benchmarker.run(YourClassToBenchmark.class).save(Format.CSV, "yourfile.csv");

Check out the test packages for more info on how to use JBenchmarx
