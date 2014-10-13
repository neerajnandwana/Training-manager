package com.mgr.training.metrics.metricset;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.mgr.training.util.Const;
import com.mgr.training.util.EffectiveClassPath;
import com.sun.management.OperatingSystemMXBean;

public class VmSpecsMetricSet implements MetricSet {
	private final Stopwatch appStarted = Stopwatch.createStarted();
	private final StandardSystemProperty javaVersion = StandardSystemProperty.JAVA_VERSION;
	private final StandardSystemProperty osName = StandardSystemProperty.OS_NAME;
	private final StandardSystemProperty osVersion = StandardSystemProperty.OS_VERSION;
	private final StandardSystemProperty osArch = StandardSystemProperty.OS_ARCH;

	@Override
	public Map<String, Metric> getMetrics() {
		final Map<String, Metric> gauges = Maps.newLinkedHashMap();
		final OperatingSystemMXBean osBean = Const.OS_BEAN;
		final String version = extractJavaVersion();
		
		gauges.put(javaVersion.key(), new Gauge<String>() {
			@Override
			public String getValue() {
				return version;
			}
		});
		gauges.put("java.effectiveClasspath", new Gauge<String[]>() {
			@Override
			public String[] getValue() {
				return EffectiveClassPath.getClassPath().split(";");
			}
		});
		gauges.put(osName.key(), new Gauge<String>() {
			@Override
			public String getValue() {
				return osName.value();
			}
		});
		gauges.put(osVersion.key(), new Gauge<String>() {
			@Override
			public String getValue() {
				return osVersion.value();
			}
		});
		gauges.put(osArch.key(), new Gauge<String>() {
			@Override
			public String getValue() {
				return osArch.value();
			}
		});
		gauges.put("host.instance.uptime", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return appStarted.elapsed(TimeUnit.MILLISECONDS);
			}
		});
		gauges.put("host.jvm.uptime", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return ManagementFactory.getRuntimeMXBean().getUptime();
			}
		});
		gauges.put("host.cpu.cores", new Gauge<Integer>() {
			@Override
			public Integer getValue() {
				return osBean.getAvailableProcessors();
			}
		});
		gauges.put("host.memory.physical.free", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return osBean.getFreePhysicalMemorySize();
			}
		});
		gauges.put("host.memory.physical.total", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return osBean.getTotalPhysicalMemorySize();
			}
		});
		gauges.put("host.memory.swap.free", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return osBean.getFreeSwapSpaceSize();
			}
		});
		gauges.put("host.memory.swap.total", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return osBean.getTotalSwapSpaceSize();
			}
		});
		gauges.put("host.system.loadAverage", new Gauge<Double>() {
			@Override
			public Double getValue() {
				return osBean.getSystemLoadAverage();
			}
		});
		gauges.put("host.system.cpuLoad", new Gauge<Double>() {
			@Override
			public Double getValue() {
				return osBean.getSystemCpuLoad();
			}
		});
		gauges.put("host.process.cpuLoad", new Gauge<Double>() {
			@Override
			public Double getValue() {
				return osBean.getProcessCpuLoad();
			}
		});
		gauges.put("host.process.cpuTime", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return osBean.getProcessCpuTime();
			}
		});
		return gauges;
	}

	private String extractJavaVersion() {
		String actualVersion = javaVersion.value();
		String alternateVersion = System.getProperty("java.runtime.version");
		if (alternateVersion != null && alternateVersion.length() > actualVersion.length()) {
			actualVersion = alternateVersion;
		}
		return actualVersion;
	}
}
