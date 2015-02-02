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
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mgr.training.metrics.healthcheck.DiskSpaceHealthCheck;
import com.mgr.training.metrics.healthcheck.HibernateSessionHealthCheck;
import com.mgr.training.metrics.metricset.HibernateStatisticsMetricSet;
import com.mgr.training.metrics.metricset.JvmFlagsMetricSet;
import com.mgr.training.metrics.metricset.VmSpecsMetricSet;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;

public class AppInstrumentationModule extends AbstractModule {
	private static final MetricRegistry metricRegistry = AppMetricsServletContextListener.METRIC_REGISTRY;
	private static final HealthCheckRegistry healthCheckRegistry = AppHealthCheckServletContextListener.HEALTH_CHECK_REGISTRY;
	
	@Override
	protected void configure() {		
		//binding metric registry
		Multibinder<MetricSet> metricSetBinder = Multibinder.newSetBinder(binder(), MetricSet.class);
		metricSetBinder.addBinding().toInstance(new VmSpecsMetricSet());	
		metricSetBinder.addBinding().toInstance(new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
		metricSetBinder.addBinding().toInstance(new GarbageCollectorMetricSet());
		metricSetBinder.addBinding().toInstance(new MemoryUsageGaugeSet());
		metricSetBinder.addBinding().toInstance(new ThreadStatesGaugeSet());
		metricSetBinder.addBinding().toInstance(new JvmFlagsMetricSet());	
		metricSetBinder.addBinding().to(HibernateStatisticsMetricSet.class);	
		
		// binding health check registry
		Multibinder<HealthCheck> healthChecksBinder = Multibinder.newSetBinder(binder(), HealthCheck.class);
		healthChecksBinder.addBinding().toInstance(new DiskSpaceHealthCheck());
		healthChecksBinder.addBinding().toInstance(new ThreadDeadlockHealthCheck());
		healthChecksBinder.addBinding().to(HibernateSessionHealthCheck.class);

		install(new MetricsInstrumentationModule(metricRegistry));
		install(new AppInstrumentationServletModule(healthCheckRegistry));

		bind(MetricRegistry.class).toInstance(metricRegistry);
		bind(HealthCheckRegistry.class).toInstance(healthCheckRegistry);
		
		//start metric report
		bind(MetricReport.class).asEagerSingleton();
	}
}