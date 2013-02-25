package net.cammann.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.cammann.Arguments;
import net.cammann.BenchmarkException;
import net.cammann.results.MethodResult;
import net.cammann.results.Result;

public class CVSExport {

	private Result result;
	private FileOutputStream fout;

	public CVSExport(Result result) {
		this.result = result;
	}

	private void writeHeader() throws IOException {
		for (Arguments method : result.getMethodsTested()) {
			fout.write((method + ", ").getBytes());
		}
	}

	public void save(File file) {

		try {
			fout = new FileOutputStream(file);
			writeHeader();

			fout.write("\n".getBytes());
			for (int i = 0;; i++) {
				int check = 0;
				for (Arguments a : result.getMethodsTested()) {
					// System.out.println(m.getName());
					List<MethodResult> methodResults = result.getMethodResults(a);
					if (methodResults.size() > i) {
						fout.write(String.valueOf(methodResults.get(i).getTime()).getBytes());
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

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

}
