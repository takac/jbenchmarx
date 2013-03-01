package net.cammann.export;

import java.io.File;

import net.cammann.results.Result;

public interface Exporter {

	public void setResult(Result result);
	public Result getResult();
	public void save(File file);

}
