## JBenchmarx

### Benchmarking utility to help you get the most from your app!

###Usage:

Mark any method you would like to benchmark with a `@Benchmark` annotation,
then create a new class to execute your Benchmark, and call:

	Benchmarker.run(YourClassToBenchmark.class);

This will return you a simple output similar too:

	your.package.YourClass.yourMethod took: 4,400 ns, Arguments: { }, returned: 45
	your.package.YourClass.yourMethod : averaged 4,400 ns

This shows us that the time taken to execute this method was 4400 nanoseconds,
there were no parameters injected into the method also the method retured the 45.

The times are done in nanoseconds, if your system does not support this timing
accuracy times will still be reported in nanoseconds however the results
obtained can only be accurate to the nearest milisecond.

You can run tell JBenchmarx how many times to run the method by just adding
a number inside the annotation `@Benchmark(5)`.

	your.package.YourClass.yourMethod took: 5,000 ns, Arguments: { }, returned: 45
	your.package.YourClass.yourMethod took: 5,000 ns, Arguments: { }, returned: 45
	your.package.YourClass.yourMethod took: 5,000 ns, Arguments: { }, returned: 45
	your.package.YourClass.yourMethod took: 3,000 ns, Arguments: { }, returned: 45
	your.package.YourClass.yourMethod took: 2,000 ns, Arguments: { }, returned: 45
	yourMethod : averaged 4,000 ns

You can specify parameters and fields to inject into your methods and classes
for benchmarking:

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


	your.package.YourClass.yourMethod took: 12,000 ns, Arguments: { 10.0 }, returned: 6930.0
	your.package.YourClass.yourMethod took: 6,000 ns, Arguments: { 10.0 }, returned: 6930.0
	your.package.YourClass.yourMethod took: 3,000 ns, Arguments: { 10.0 }, returned: 6930.0
	.
	.
	.
	your.package.YourClass.yourMethod took: 2,000 ns, Arguments: { 10.0 }, returned: 6930.0
	your.package.YourClass.yourMethod : averaged 3,000 ns

The `@Range` annotation allows you to specify a range of values to inject into
method parameters. *Currently fields are range injectable.*

	@Benchmark
	public int method(@Range({"5", "6", "7"}) int x) {
		return x * 5;
	}

This will produce - 

	your.package.YourClass.method took: 2,100 ns, Arguments: { 5 }, returned: 25
	your.package.YourClass.method took: 1,200 ns, Arguments: { 6 }, returned: 30
	your.package.YourClass.method took: 1,200 ns, Arguments: { 7 }, returned: 35

The `@Lookup` annoation allows you to set the value of a field or parameter at run time.

	@Lookup("value_key")
	private int value;
	
	@Benchmark
	public int method(@Lookup("x_key") int x) {
		return x * 5;
	}
	
	public static void main(String[] args) {
		Benchmarker.addLookup("value_key", 3);
		Benchmarker.addLookup("x_key", 8);
		Benchmarker.run(YourClassToBenchmark.class, lookupTable);
	}

This will produce - 

	your.package.YourClass.method took: 1,200 ns, Arguments: { 8 }, returned: 24

A new feature in JBenchmarx is the `@Callback` annotation, which allows you to
programmatically set your parameters and fields. To do this you need to create a
new `CallbackListener` and add it to the Benchmarker. The `CallbackListener` has
one method to implement which is called when the value is required by
JBenchmarx. This method is provided with `CallbackEvent` which contains some
details of the context that inject was called.

	public class Example {

		@Benchmark(15)
		public int exampleMethod(@Callback("callback_key") int x) {
			return x;
		}

		public static void main(String[] args) {
			Benchmarker.addCallback("callback_key", new CallbackListener<Integer>() {
				@Override
				public Integer callback(CallbackEvent event) {
					return event.getRun() * 5;
				}
			});

			Benchmarker.run(Example.class);
		}
	}

This will programmatically set the parameter `x` of the method `exampleMethod()`
to **5 * the execution number of the run**. During this example the parameter
will be set to 5 for for the first execution, 10 for the second, all the way up to 
`15 * 5` which is the number of runs we have told JBenchmarx to execute.

Example output:-

	Example.exampleMethod Took: 2,000 ns, Arguments: { 5 }, Returned: 5
	Example.exampleMethod Took: 2,000 ns, Arguments: { 10 }, Returned: 10
	Example.exampleMethod Took: 2,000 ns, Arguments: { 15 }, Returned: 15
	.
	.
	.
	Example.exampleMethod Took: 2,000 ns, Arguments: { 75 }, Returned: 75

You can also save the results of your benchmark -

	Benchmarker.run(YourClassToBenchmark.class).save(Format.CSV, "yourfile.csv");

Check out the test packages for more info on how to use JBenchmarx
