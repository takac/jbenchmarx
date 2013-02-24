package net.cammann.export;

import java.io.File;

public interface Saveable {

	File save(Format format, File file);
	File save(Format format, String file);
}
