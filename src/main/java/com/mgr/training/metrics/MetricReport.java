package com.mgr.training.metrics;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.mgr.training.metrics.metricset.HibernateStatisticsMetricSet;
import com.mgr.training.metrics.metricset.JvmFlagsMetricSet;
import com.mgr.training.metrics.metricset.VmSpecsMetricSet;


public class MetricReport {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricReport.class);
	private static final Map<Class<? extends MetricSet>, String> customMetricName = ImmutableMap.<Class<? extends MetricSet>, String>builder()
						.put(VmSpecsMetricSet.class, 				"vm.specs")
						.put(BufferPoolMetricSet.class, 			"jvm.buffers")
						.put(GarbageCollectorMetricSet.class, 		"jvm.gc")
						.put(MemoryUsageGaugeSet.class, 			"jvm.memory")
						.put(ThreadStatesGaugeSet.class, 			"jvm.threads")
						.put(JvmFlagsMetricSet.class, 				"jvm.flags")
						.put(HibernateStatisticsMetricSet.class, 	"db.hibernate")
						.build();
	
	@Inject
	public MetricReport(Set<MetricSet> metricSets, MetricRegistry metricRegistry) {		
		for (MetricSet metricSet : metricSets) {
			metricRegistry.register(name(metricSet.getClass()), metricSet);
		}
		
		final String dirPath = "trainingmgr-report";
		final String homeDir = StandardSystemProperty.USER_HOME.value();
		final File file = new File(homeDir, dirPath);

		try {
			FileUtils.forceMkdir(file);

			CsvReporter.forRegistry(metricRegistry)
						.formatFor(Locale.US)
						.convertRatesTo(TimeUnit.SECONDS)
						.convertDurationsTo(TimeUnit.MILLISECONDS)
						.build(file)
						.start(1, TimeUnit.MINUTES);
		} catch (IOException e) {
			LOGGER.error("Error while starting MetricReport: {}", e.getMessage());
			throw Throwables.propagate(e);
		}
	}
	
	private String name(final Class<?> klass){
		final String customName = customMetricName.get(klass);
		return customName == null ? klass.getSimpleName(): customName;
	}
	
}
