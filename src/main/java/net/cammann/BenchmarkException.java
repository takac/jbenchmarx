package net.cammann;

public class BenchmarkException extends RuntimeException {

	public BenchmarkException(Exception e) {
		super(e);
	}

	public BenchmarkException() {
	}

	public BenchmarkException(String message) {
		super(message);
	}

}
