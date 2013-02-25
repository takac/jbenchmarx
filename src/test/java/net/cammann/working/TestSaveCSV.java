package net.cammann.working;

import static org.junit.Assert.assertEquals;

import java.io.File;

import net.cammann.Benchmarker;
import net.cammann.classesToTest.StringJoinExample;
import net.cammann.export.Format;

import org.junit.Test;

public class TestSaveCSV {
	@Test
	public void test() {
		String fileName = "stringJoin.csv";
		File file = new File(fileName);
		File f = Benchmarker.run(StringJoinExample.class).save(Format.CSV, "stringJoin.csv");
		assertEquals(file, f);
		File f2 = Benchmarker.run(StringJoinExample.class).save(Format.CSV, file);
		assertEquals(file, f2);
		f2.delete();
	}
}
