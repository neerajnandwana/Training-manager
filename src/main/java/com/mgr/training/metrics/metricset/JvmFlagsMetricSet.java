package com.mgr.training.metrics.metricset;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mgr.training.util.EffectiveClassPath;
import com.mgr.training.util.JvmExecutableSupplier;
import com.mgr.training.util.RuntimeShutdownHookRegistrar;
import com.mgr.training.util.Transforms;

public class JvmFlagsMetricSet implements MetricSet {

	@Override
	public Map<String, Metric> getMetrics() {
		Map<String, String> flags = null;
		try {
			flags = Flags.getInstance().flags();
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return Maps.transformValues(flags, Transforms.VALUE_TO_METRIC);
	}
	
	
	private static final class Flags {
		private static volatile Flags INSTANCE;

		private final RuntimeShutdownHookRegistrar shutdownHookRegistrar;
		private final Supplier<File> jvmExecutable;

		private Flags() {
			shutdownHookRegistrar = RuntimeShutdownHookRegistrar.INSTANCE;
			jvmExecutable = Suppliers.memoize(new JvmExecutableSupplier());
		}

		public static Flags getInstance() {
			Flags inst = INSTANCE;
			if (inst == null) {
				synchronized (Flags.class) {
					inst = INSTANCE;
					if (inst == null) {
						INSTANCE = inst = new Flags();
					}
				}
			}
			return INSTANCE;
		}

		public synchronized Map<String, String> flags() throws IOException {
			Map<String, String> flagMap = Maps.newLinkedHashMap();
			Process p = startWorker();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			CharSequence line;
			Entry<String, String> flagEntry;
			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
				flagEntry = LineParser.parse(line);
				if (flagEntry != null) {
					flagMap.put(flagEntry.getKey(), flagEntry.getValue());
				}
			}
			return flagMap;
		}

		private Process startWorker() throws IOException {
			final Process delegate = buildProcess().start();
			final Thread shutdownHook = new Thread("worker-shutdown-hook") {
				@Override
				public void run() {
					delegate.destroy();
				}
			};
			shutdownHookRegistrar.addShutdownHook(shutdownHook);

			final Process worker = new Process() {
				@Override
				public OutputStream getOutputStream() {
					return delegate.getOutputStream();
				}

				@Override
				public InputStream getInputStream() {
					return delegate.getInputStream();
				}

				@Override
				public InputStream getErrorStream() {
					return delegate.getErrorStream();
				}

				@Override
				public int waitFor() throws InterruptedException {
					int waitFor = delegate.waitFor();
					shutdownHookRegistrar.removeShutdownHook(shutdownHook);
					return waitFor;
				}

				@Override
				public int exitValue() {
					int exitValue = delegate.exitValue();
					// if it hasn't thrown, the process is done
					shutdownHookRegistrar.removeShutdownHook(shutdownHook);
					return exitValue;
				}

				@Override
				public void destroy() {
					delegate.destroy();
					shutdownHookRegistrar.removeShutdownHook(shutdownHook);
				}
			};
			return worker;
		}

		private ProcessBuilder buildProcess() throws IOException {
			ProcessBuilder processBuilder = new ProcessBuilder().redirectErrorStream(false);
			List<String> args = processBuilder.command();

			args.addAll(getJvmArgs());
			args.add("-XX:+PrintFlagsFinal");
			args.add("-XX:+PrintCompilation");
			args.add("-XX:+PrintGC");

			return processBuilder;
		}

		private List<String> getJvmArgs() {
			List<String> args = Lists.newArrayList();
			String jdkPath = jvmExecutable.get().getAbsolutePath();
			args.add(jdkPath);

			String classPath = EffectiveClassPath.getClassPath();
			Collections.addAll(args, "-cp", classPath);
			return args;
		}

		private static final class LineParser {
			private static final Pattern VM_OPTION_PATTERN = Pattern.compile("\\s*(\\w+)\\s+(\\w+)\\s+:?=\\s+([^\\s]*)\\s+\\{([^}]*)\\}\\s*");

			public static Entry<String, String> parse(CharSequence line) {
				Matcher vmOptionMatcher = VM_OPTION_PATTERN.matcher(line);
				if (vmOptionMatcher.matches()) {
					return Maps.immutableEntry(vmOptionMatcher.group(2), vmOptionMatcher.group(3));
				}
				return null;
			}
		}
	}
}
