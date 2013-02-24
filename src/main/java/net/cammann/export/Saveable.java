package net.cammann.export;

import java.io.File;

public interface Saveable {

	void save(Format format, File file);
	void save(Format format, String file);
}
