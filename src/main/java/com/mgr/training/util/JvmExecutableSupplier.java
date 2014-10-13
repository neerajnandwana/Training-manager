package com.mgr.training.util;
import java.io.File;

import com.google.common.base.Preconditions;
import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Supplier;

public class JvmExecutableSupplier implements Supplier<File> {
	private final String javaHome = StandardSystemProperty.JAVA_HOME.value();

	@Override
	public synchronized File get() {
		// TODO: support other platforms. This currently supports
		// finding the java executable on
		// standard configurations of unix systems and windows.
		File bin = new File(javaHome, "bin");
		Preconditions.checkState(bin.exists() && bin.isDirectory(), "Could not find %s under java home %s", bin, javaHome);
		File jvm = new File(bin, "java");
		if (!jvm.exists() || jvm.isDirectory()) {
			jvm = new File(bin, "java.exe");
			if (!jvm.exists() || jvm.isDirectory()) {
				throw new IllegalStateException(String.format("Cannot find java binary in %s, looked for java and java.exe", bin));
			}
		}
		return jvm;
	}
}
