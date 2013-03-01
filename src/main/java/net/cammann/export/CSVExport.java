package net.cammann.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.cammann.BenchmarkException;
import net.cammann.ParameterisedMethod;
import net.cammann.results.MethodResult;
import net.cammann.results.Result;

public class CSVExport implements Exporter {

	private Result result;
	private FileOutputStream fout;

	public CSVExport(Result result) {
		this.result = result;
	}

	public CSVExport() {
	}

	private void writeHeader() throws IOException {
		for (ParameterisedMethod method : result.getParameterisedMethodsTested()) {
			fout.write((method + ", ").getBytes());
		}
	}

	@Override
	public void save(File file) {

		try {
			fout = new FileOutputStream(file);
			writeHeader();

			fout.write("\n".getBytes());
			for (int i = 0;; i++) {
				int check = 0;
				for (ParameterisedMethod a : result.getParameterisedMethodsTested()) {
					// System.out.println(m.getName());
					List<MethodResult> methodResults = result.getMethodResults(a);
					if (methodResults.size() > i) {
						fout.write(String.valueOf(methodResults.get(i).getRuntime()).getBytes());
						if (methodResults.size() == (i + 1)) {
							check++;
						}
					} else {
						check++;
					}
					fout.write(", ".getBytes());
				}
				fout.write("\n".getBytes());
				if (check == result.getMethodResults().size()) {
					break;
				}
			}
			fout.close();
		} catch (IOException e) {
			throw new BenchmarkException(e);
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
