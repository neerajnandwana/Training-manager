package com.mgr.training.metrics;

import java.lang.management.ManagementFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.google.inject.multibindings.Multibinder;
import com.mgr.training.metrics.healthcheck.HibernateSessionHealthCheck;
import com.palominolabs.metrics.guice.InstrumentationModule;

public class AppInstrumentationModule extends InstrumentationModule {
	final MetricRegistry metricRegistry = AppMetricsServletContextListener.METRIC_REGISTRY;
	final HealthCheckRegistry healthCheckRegistry = AppHealthCheckServletContextListener.HEALTH_CHECK_REGISTRY;

	@Override
	protected void configure() {
		super.configure();		
		
		//binding metric registry
		Multibinder<MetricSet> metricSetBinder = Multibinder.newSetBinder(binder(), MetricSet.class);
		metricSetBinder.addBinding().toInstance(new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
		metricSetBinder.addBinding().toInstance(new GarbageCollectorMetricSet());
		metricSetBinder.addBinding().toInstance(new MemoryUsageGaugeSet());
		metricSetBinder.addBinding().toInstance(new ThreadStatesGaugeSet());
		metricSetBinder.addBinding().to(HibernateStatisticsMetricSet.class);		
		
		// binding health check registry
		Multibinder<HealthCheck> healthChecksBinder = Multibinder.newSetBinder(binder(), HealthCheck.class);
		healthChecksBinder.addBinding().to(HibernateSessionHealthCheck.class);
		healthChecksBinder.addBinding().to(ThreadDeadlockHealthCheck.class);

		install(new AppInstrumentationServletModule());

		//start metric report
		bind(MetricReport.class).asEagerSingleton();
	}

	@Override
	protected HealthCheckRegistry createHealthCheckRegistry() {
		return healthCheckRegistry;
	}

	@Override
	protected MetricRegistry createMetricRegistry() {
		return metricRegistry;
	}
}