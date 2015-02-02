package com.mgr.training.metrics;

import java.util.Set;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.codahale.metrics.servlets.ThreadDumpServlet;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.palominolabs.metrics.guice.servlet.AdminServletModule;

public class AppInstrumentationServletModule extends ServletModule {
	private static final String METRICS_ROOT_URI = AdminServlet.DEFAULT_METRICS_URI;
	private static final String METRICS_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_METRICS_URI;
	private static final String PING_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_PING_URI;
	private static final String THREADS_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_THREADS_URI;
	private static final String HEALTHCHECK_URI = METRICS_ROOT_URI + AdminServlet.DEFAULT_HEALTHCHECK_URI;

	final HealthCheckRegistry healthCheckRegistry;

	public AppInstrumentationServletModule(HealthCheckRegistry healthCheckRegistry){
		this.healthCheckRegistry = healthCheckRegistry;
	}
	
	@Override
	protected void configureServlets() {
		install(new AdminServletModule());

		bind(MetricsServlet.class).asEagerSingleton();
		bind(PingServlet.class).asEagerSingleton();
		bind(ThreadDumpServlet.class).asEagerSingleton();

		serve(METRICS_URI).with(MetricsServlet.class);
		serve(PING_URI).with(PingServlet.class);
		serve(THREADS_URI).with(ThreadDumpServlet.class);
		serve(HEALTHCHECK_URI).with(HealthCheckServlet.class);
	}

	@Provides
	@Singleton
	@Inject
	public HealthCheckServlet buildHealthCheckServlet(Set<HealthCheck> healthChecks) {
		for (HealthCheck healthCheck : healthChecks) {
			healthCheckRegistry.register(healthCheck.getClass().getSimpleName(), healthCheck);
		}
		return new HealthCheckServlet();
	}

}
