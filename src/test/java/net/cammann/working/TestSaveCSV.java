package net.cammann.working;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import net.cammann.Benchmarker;
import net.cammann.classesToTest.StringJoinExample;
import net.cammann.export.CSVExport;
import net.cammann.export.Format;
import net.cammann.results.Result;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSaveCSV {

	private File tmpFile;
	private final String fileName = "stringJoin.csv";
	private CSVExport export;
	private Result result;

	@Before
	public void setup() {
		tmpFile = new File(fileName);
		tmpFile.delete();
		assertFalse(tmpFile.exists());
		result = Benchmarker.run(StringJoinExample.class);
		export = new CSVExport();
		export.setResult(result);
	}

	@After
	public void cleanup() {
		tmpFile.delete();
		assertFalse(tmpFile.exists());
	}

	@Test
	public void testSetResult() {
		assertEquals(result, export.getResult());
	}

	@Test
	public void testPackage() {
		export.save(tmpFile);
		assertTrue(tmpFile.exists());
	}

	@Test
	public void testClass() {
		export.save(tmpFile);
		assertTrue(tmpFile.exists());
	}

	@Test(expected = NullPointerException.class)
	public void testNullFile() {
		export.save(null);
	}

	@Test
	public void testSaveableResult() {
		File f = Benchmarker.run(StringJoinExample.class).save(Format.CSV, fileName);
		assertEquals(tmpFile, f);
	}

	@Test
	public void testResultConstructor() {
		CSVExport csv = new CSVExport(result);
		assertEquals(result, csv.getResult());
	}
}
