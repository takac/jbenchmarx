package net.cammann;

public class Optional<T> {
	
	private T object;
	private boolean set = false;

	public Optional(T object) {
		this.object = object;
		set = true;
	}

	public Optional() {
		
	}
	
	public boolean isPresent() {
		return set;
	}

	public boolean isAbsent() {
		return !set;
	}

	public T get() {
		if (!set) {
			throw new BenchmarkException("Object has not been set");
		}
		return object;
	}

	public void set(T obj) {
		this.object = obj;
		set = true;
	}

}
