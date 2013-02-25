package net.cammann;

public interface CallbackListener<T> {
	
	public T callback(CallbackEvent event);
	
}
