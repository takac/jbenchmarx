package net.cammann.results;

import java.io.File;

import net.cammann.export.CSVExport;
import net.cammann.export.Exporter;
import net.cammann.export.Format;

public abstract class SaveableResult implements Result {

	@Override
	public File save(Format format, File file) {
		Exporter exporter = null;
		switch (format) {
			case CSV :
				exporter = new CSVExport(this);
				break;
			default :
				throw new RuntimeException("Not supported");
		}
		exporter.save(file);
		return file;
	}

	@Override
	public File save(Format format, String filename) {
		return save(format, new File(filename));
	}

	public File save(String format, String filename) {
		return save(Format.valueOf(format.toUpperCase()), filename);
	}
}
