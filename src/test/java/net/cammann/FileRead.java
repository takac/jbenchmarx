package net.cammann;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import net.cammann.annotations.BeforeBenchmark;
import net.cammann.annotations.Benchmark;

public class FileRead {

	private static final String filename = "/Users/tc191/bigfile.txt";
	private static final int BYTE_BUFFER_SIZE_SMALL = 32;
	private static final int BYTE_BUFFER_SIZE_MEDIUM = 256;
	private static final int BYTE_BUFFER_SIZE_LARGE = 1024;

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
	private int rawReadSmallBuffer() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
		byte[] buffer = new byte[BYTE_BUFFER_SIZE_SMALL];
		int length = 0;
		int read = 0;
		while ((read = bis.read(buffer)) != -1) {
			length += new String(buffer, 0, read).length();
		}
		bis.close();
		return length;
	}

	@Benchmark(10)
	private int rawReadMediumBuffer() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
		byte[] buffer = new byte[BYTE_BUFFER_SIZE_MEDIUM];
		int length = 0;
		int read = 0;
		while ((read = bis.read(buffer)) != -1) {
			length += new String(buffer, 0, read).length();
		}
		bis.close();
		return length;
	}

	@Benchmark(10)
	private int rawReadLargeBuffer() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
		byte[] buffer = new byte[BYTE_BUFFER_SIZE_LARGE];
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
