package net.cammann;

public class LookupException extends BenchmarkException {
	public LookupException() {
	}
	public LookupException(String message) {
		super(message);
	}
	public LookupException(Throwable t) {
		super(t);
	}
}
