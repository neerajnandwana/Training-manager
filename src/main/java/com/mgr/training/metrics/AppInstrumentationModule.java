package com.mgr.training.metrics;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.servlets.AdminServlet;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.codahale.metrics.servlets.ThreadDumpServlet;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import com.palominolabs.metrics.guice.InstrumentationModule;
import com.palominolabs.metrics.guice.servlet.AdminServletModule;

public class AppInstrumentationModule  extends AbstractModule {	
	private static final String METRICS_ROOT_URI = AdminServlet.DEFAULT_METRICS_URI;
	private static final String METRICS_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_METRICS_URI;
	private static final String PING_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_PING_URI;
	private static final String THREADS_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_THREADS_URI;
	private static final String HEALTHCHECK_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_HEALTHCHECK_URI;
	
	@Override
	protected void configure() {
		install(initInstrumentationModule());
		install(initServletModule());
		
		registerHealthChecks();
		startReporter();
	}
	
	private Module initInstrumentationModule(){
		final Module instrumentationModule = new InstrumentationModule(){			
			@Override
			protected HealthCheckRegistry createHealthCheckRegistry() {
				return AppAdminServletContextListener.HEALTH_CHECK_REGISTRY;
			}
			@Override
			protected MetricRegistry createMetricRegistry() {
				return AppAdminServletContextListener.METRIC_REGISTRY;
			}			
		};
		return instrumentationModule;		
	}

	private Module initServletModule() {
		final Module applicationServletModule = new ServletModule() {
			@Override
			protected void configureServlets() {				
				install(new AdminServletModule());
				
				bind(MetricsServlet.class).asEagerSingleton();
				bind(PingServlet.class).asEagerSingleton();
				bind(ThreadDumpServlet.class).asEagerSingleton();
				bind(HealthCheckServlet.class).asEagerSingleton();
				
				serve(METRICS_URI).with(MetricsServlet.class);
				serve(PING_URI).with(PingServlet.class);
				serve(THREADS_URI).with(ThreadDumpServlet.class);
				serve(HEALTHCHECK_URI).with(HealthCheckServlet.class);
			}
		};
		return applicationServletModule;
	}
	
	private void registerHealthChecks(){
		final HealthCheckRegistry healthCheckRegistry = AppAdminServletContextListener.HEALTH_CHECK_REGISTRY;		
		healthCheckRegistry.register("DB Health", new DBHealthCheck());
		healthCheckRegistry.register("ThreadDeadLock Health", new ThreadDeadlockHealthCheck());		
	}
	
	private void startReporter(){
		new File("D:\\trainingmgr\\report").mkdir();
		final CsvReporter reporter = CsvReporter.forRegistry(AppAdminServletContextListener.METRIC_REGISTRY)
								                .formatFor(Locale.US)
								                .convertRatesTo(TimeUnit.SECONDS)
								                .convertDurationsTo(TimeUnit.MILLISECONDS)
								                .build(new File("c:\\trainingmgr\\report"));
		reporter.start(1, TimeUnit.MINUTES);
	}
}