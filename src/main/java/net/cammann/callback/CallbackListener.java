package net.cammann.callback;

public interface CallbackListener<T> {
	
	public T callback(CallbackEvent event);
	
}
