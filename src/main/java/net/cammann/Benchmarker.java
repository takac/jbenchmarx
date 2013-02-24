package net.cammann;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.cammann.results.PackageResult;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class Benchmarker {

	public static PackageResult run(Class<?>... classes) {
		PackageResult pkg = new PackageResult();
		for (Class<?> cls : classes) {
			ClassBenchmarker bm = new ClassBenchmarker(cls);
			bm.execute();
			pkg.add(bm.getResult());
		}
		pkg.printResult();
		return pkg;
	}

	public static PackageResult run(Package pkg) {
		PackageResult packResult = new PackageResult();
		System.out.println(pkg.getName());

		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		ConfigurationBuilder configBuilder = new ConfigurationBuilder();
		configBuilder.setScanners(new SubTypesScanner(false), new ResourcesScanner());
		configBuilder.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])));
		configBuilder.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg.getName())));

		Reflections reflections = new Reflections(configBuilder);
					
		Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
		for (Class<? extends Object> cls : allClasses) {
			System.out.println(cls.getName());
			ClassBenchmarker bm = new ClassBenchmarker(cls);
			bm.execute();
			packResult.add(bm.getResult());
		}
		packResult.printResult();
		return packResult;

	}
	
}
