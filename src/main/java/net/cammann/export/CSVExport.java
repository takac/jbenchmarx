package net.cammann.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import net.cammann.BenchmarkException;
import net.cammann.ParameterisedMethod;
import net.cammann.results.MethodResult;
import net.cammann.results.Result;

import org.apache.commons.io.IOUtils;

public class CSVExport implements Exporter {

	private Result result;
	private static final Charset charset = Charset.defaultCharset();

	public CSVExport(Result result) {
		this.result = result;
	}

	public CSVExport() {
	}

	private void writeHeader(OutputStream os) throws IOException {
		for (ParameterisedMethod method : result.getParameterisedMethodsTested()) {
			os.write((method.getMethod().getName() + ", ").getBytes(charset));
		}
	}

	@Override
	public void save(File file) {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			writeHeader(fout);

			fout.write("\n".getBytes(charset));
			for (int i = 0;; i++) {
				int check = 0;
				for (ParameterisedMethod a : result.getParameterisedMethodsTested()) {
					List<MethodResult> methodResults = result.getMethodResults(a);
					if (methodResults.size() > i) {
						fout.write(String.valueOf(methodResults.get(i).getRuntime()).getBytes(charset));
						if (methodResults.size() == (i + 1)) {
							check++;
						}
					} else {
						check++;
					}
					fout.write(", ".getBytes(charset));
				}
				fout.write("\n".getBytes(charset));
				if (check == result.getParameterisedMethodsTested().size()) {
					break;
				}
			}
		} catch (IOException e) {
			throw new BenchmarkException(e);
		} finally {
			IOUtils.closeQuietly(fout);
		}
	}

	@Override
	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public Result getResult() {
		return result;
	}

}
