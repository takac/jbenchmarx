package net.cammann.classesToTest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

import net.cammann.Benchmarker;
import net.cammann.annotations.BeforeBenchmark;
import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Range;

public class FileRead {

	private static final String filename = "/Users/tc191/bigfile.txt";

	@BeforeBenchmark
	public void before() {

	}

	@Benchmark(10)
	private int bufferedReader() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = "";
		int length = 0;
		while ((line = br.readLine()) != null) {
			length += line.length();
		}
		br.close();
		return length;
	}
	
	@Benchmark(10)
	private int bufferedCharReader(@Range({ "256", "1024"}) int capacity) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		CharBuffer cb = CharBuffer.allocate(capacity);
		int length = 0;
		while (br.read(cb) != -1) {
			cb.flip();
			length += cb.toString().length();
		}
		br.close();
		return length;
	}

	@Benchmark(10)
	private int rawReadBuffer(@Range({"256", "1024"}) int buffersize) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
		byte[] buffer = new byte[buffersize];
		int length = 0;
		int read = 0;
		while ((read = bis.read(buffer)) != -1) {
			length += new String(buffer, 0, read).length();
		}
		bis.close();
		return length;
	}


	public static void main(String[] args) {
		Benchmarker.run(FileRead.class);
	}

}
