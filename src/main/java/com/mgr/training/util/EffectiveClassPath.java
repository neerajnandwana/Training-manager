package com.mgr.training.util;


import static java.lang.Thread.currentThread;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import com.google.common.base.Joiner;
import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.ImmutableSet;

/**
 * Provides a class path containing all of the jars present on the local machine
 * that are referenced by a given {@link ClassLoader}.
 */
public final class EffectiveClassPath {
	private static final String pathSeparator = StandardSystemProperty.PATH_SEPARATOR.value();
	
	private EffectiveClassPath() {
	}

	public static String getClassPath() {
		// Use the effective class path in case this is being invoked in an isolated class loader
		ClassLoader classLoader = currentThread().getContextClassLoader();
		return Joiner.on(pathSeparator).join(getClassPathFiles(classLoader));
	}

	private static ImmutableSet<File> getClassPathFiles(ClassLoader classLoader) {
		ImmutableSet.Builder<File> files = ImmutableSet.builder();
		ClassLoader parent = classLoader.getParent();
		if (parent != null) {
			files.addAll(getClassPathFiles(parent));
		}
		if (classLoader instanceof URLClassLoader) {
			URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
			for (URL url : urlClassLoader.getURLs()) {
				try {
					files.add(new File(url.toURI()));
				} catch (URISyntaxException e) {
					// skip it then
				} catch (IllegalArgumentException e) {
					// skip it then
				}
			}
		}
		return files.build();
	}
}
