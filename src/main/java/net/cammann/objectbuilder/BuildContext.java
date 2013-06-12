package net.cammann.objectbuilder;


public interface BuildContext {
	int getRunNumber();
	int getRangeRound();
	Object callback(String key);
	Object lookup(String key);
}
