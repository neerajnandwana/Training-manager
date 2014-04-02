package com.mgr.training.metrics;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.google.common.base.Throwables;
import com.google.inject.multibindings.Multibinder;
import com.mgr.training.metrics.healthcheck.HibernateSessionHealthCheck;
import com.palominolabs.metrics.guice.InstrumentationModule;

public class AppInstrumentationModule  extends InstrumentationModule {
	final MetricRegistry metricRegistry = AppMetricsServletContextListener.METRIC_REGISTRY;
	final HealthCheckRegistry healthCheckRegistry = AppHealthCheckServletContextListener.HEALTH_CHECK_REGISTRY;	
	
	@Override
	protected void configure() {
		super.configure();
		//register JVM registry
		metricRegistry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
		metricRegistry.register("jvm.gc", new GarbageCollectorMetricSet());
		metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
		metricRegistry.register("jvm.threads", new ThreadStatesGaugeSet());
		
		//register health check registry
		Multibinder<HealthCheck> healthChecksBinder = Multibinder.newSetBinder(binder(), HealthCheck.class);
		healthChecksBinder.addBinding().to(HibernateSessionHealthCheck.class);
		healthChecksBinder.addBinding().to(ThreadDeadlockHealthCheck.class);
				
		install(new AppInstrumentationServletModule());
		
		try {
			startReporter();
		} catch (IOException e) {		
			throw Throwables.propagate(e);
		}
	}
	
	@Override
	protected HealthCheckRegistry createHealthCheckRegistry() {
		return healthCheckRegistry;
	}
	
	@Override
	protected MetricRegistry createMetricRegistry() {
		return metricRegistry;
	}
	
	private void startReporter() throws IOException{
		final String dirPath = "trainingmgr-report"; 
		final String homeDir = System.getProperty("user.home");
		final File file = new File(homeDir, dirPath);
		
		FileUtils.forceMkdir(file);
		
		CsvReporter.forRegistry(metricRegistry)
                .formatFor(Locale.US)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(file)
            	.start(1, TimeUnit.MINUTES);
	}
}