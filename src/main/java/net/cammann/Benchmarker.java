package net.cammann;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.cammann.annotations.Benchmark;
import net.cammann.callback.CallbackListener;
import net.cammann.results.PackageResult;
import net.cammann.results.SaveableResult;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class Benchmarker {

	private static final ParameterResolver parameterResolver = new ParameterResolver();

	public static void addCallback(String key, CallbackListener<?> listener) {
		parameterResolver.addCallback(key, listener);
	}

	public static void addLookup(String key, Object value) {
		parameterResolver.addLookup(key, value);
	}

	public static SaveableResult run(Class<?>... classes) {

		if (classes.length == 0) {
			try {
				StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
				Class<?> cls = Class.forName(stackTrace[2].getClassName());
				classes = new Class[] { cls };
			} catch (ClassNotFoundException e) {
				throw new BenchmarkException("Failed to find class", e);
			}

		}

		PackageResult pkg = new PackageResult();
		for (Class<?> cls : classes) {
			ClassBenchmarker bm = new ClassBenchmarker(cls, parameterResolver);
			bm.run();
			pkg.add(bm.getResult());
		}
		pkg.printResult();
		return pkg;
	}

	private static Method lookupMethod(Class<?> cls, String methodName) {
		Method method = null;
		for (Method m : cls.getDeclaredMethods()) {
			if (m.getName().equals(methodName)) {
				if (method == null) {
					method = m;
				} else {
					throw new BenchmarkException("Ambigious method name, multiple methods named: " + methodName);
				}
			}
		}
		if (method == null) {
			throw new BenchmarkException("No method name: " + methodName);
		}
		if (method.getAnnotation(Benchmark.class) == null) {
			throw new BenchmarkException("Method does not have benchmark annotation");
		}
		return method;
	}

	public static SaveableResult run(Class<?> cls, String... methodNames) {
		ClassBenchmarker bm = new ClassBenchmarker(cls, parameterResolver);
		List<Method> realMethods = new ArrayList<Method>();
		for (String name : methodNames) {
			realMethods.add(lookupMethod(cls, name));
		}
		bm.overwriteMethodsToBenchmark(realMethods);
		bm.run();
		return bm.getResult();
	}

	public static SaveableResult run(Package pkg) {
		PackageResult packResult = new PackageResult();
		System.out.println(pkg.getName());

		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		ConfigurationBuilder configBuilder = new ConfigurationBuilder();
		configBuilder.setScanners(new SubTypesScanner(false), new ResourcesScanner());
		configBuilder.setUrls(ClasspathHelper.
				forClassLoader(classLoadersList.toArray(new ClassLoader[classLoadersList.size()])));
		configBuilder.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg.getName())));

		Reflections reflections = new Reflections(configBuilder);

		Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
		for (Class<? extends Object> cls : allClasses) {
			System.out.println(cls.getName());
			ClassBenchmarker bm = new ClassBenchmarker(cls, parameterResolver);
			bm.run();
			packResult.add(bm.getResult());
		}
		packResult.printResult();
		return packResult;

	}

	// TODO run all annotations

}
