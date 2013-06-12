package net.cammann;

public class BenchmarkException extends RuntimeException {

	public BenchmarkException(Throwable t) {
		super(t);
	}

	public BenchmarkException(String msg, Throwable t) {
		super(msg, t);
	}

	public BenchmarkException() {
	}

	public BenchmarkException(String message) {
		super(message);
	}

}
