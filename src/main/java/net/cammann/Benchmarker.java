package net.cammann;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cammann.callback.CallbackHandler;
import net.cammann.callback.CallbackListener;
import net.cammann.results.ClassResult;
import net.cammann.results.PackageResult;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class Benchmarker {

	private static CallbackHandler callbackHandler = new CallbackHandler();
	private static Map<String, Object> lookupTable = new HashMap<String, Object>();

	public static void addCallback(String key, CallbackListener<?> listener) {
		callbackHandler.addCallbackListener(key, listener);
	}

	public static void addLookup(String key, Object value) {
		if(value == null || key == null) {
			throw new NullPointerException("Cannot set null key/value lookup");
		}
		lookupTable.put(key, value);
	}

	public static PackageResult run(Class<?>... classes) {
		PackageResult pkg = new PackageResult();
		for (Class<?> cls : classes) {
			ClassBenchmarker bm = new ClassBenchmarker(cls);
			bm.setLookupTable(lookupTable);
			bm.setCallbackHandler(callbackHandler);
			bm.execute();
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
					throw new BenchmarkException("Multiple methods named: " + methodName);
				}
			}
		}
		if (method == null) {
			throw new BenchmarkException("No method name: " + methodName);
		}

		return method;
	}

	public static ClassResult run(Class<?> cls, String... methodNames) {
		ClassBenchmarker bm = new ClassBenchmarker(cls);
		bm.setLookupTable(lookupTable);
		bm.setCallbackHandler(callbackHandler);
		List<Method> realMethods = new ArrayList<Method>();
		for (String name : methodNames) {
			realMethods.add(lookupMethod(cls, name));
		}
		bm.overwriteMethodsToBenchmark(realMethods);
		bm.execute();
		return bm.getResult();
	}

	public static ClassResult run(Class<?> cls) {
		ClassBenchmarker bm = new ClassBenchmarker(cls);
		bm.setLookupTable(lookupTable);
		bm.setCallbackHandler(callbackHandler);
		bm.execute();
		PackageResult pkg = new PackageResult();
		pkg.add(bm.getResult());
		pkg.printResult();
		return bm.getResult();
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
			bm.setCallbackHandler(callbackHandler);
			bm.execute();
			packResult.add(bm.getResult());
		}
		packResult.printResult();
		return packResult;

	}

}
