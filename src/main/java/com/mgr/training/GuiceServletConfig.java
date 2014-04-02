package com.mgr.training;

import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.mgr.training.auth.SecurityModule;
import com.mgr.training.data.PersistenceModule;
import com.mgr.training.data.SeedDummyData;
import com.mgr.training.filter.CharsetEncodingFilter;
import com.mgr.training.filter.DisableUrlSessionFilter;
import com.mgr.training.filter.SecurityFilter;
import com.mgr.training.filter.ThreadNameFilter;
import com.mgr.training.metrics.AppInstrumentationModule;
import com.mgr.training.rest.RestModule;
import com.mgr.training.servlet.HomeServlet;
import com.mgr.training.servlet.InsertDummyData;
import com.mgr.training.servlet.LoginServlet;
import com.mgr.training.servlet.LogoutServlet;
import com.mgr.training.util.Const;
import com.mgr.training.util.Prop;
import com.mgr.training.util.Tasks;
import com.mgr.training.view.ViewRenderer;
import com.mgr.training.view.freemarker.FreemarkerViewRenderer;

public class GuiceServletConfig extends GuiceServletContextListener {
	private static final int THREAD_POOL_SIZE = Prop.applicationConfig.getInt(Const.THREAD_POOL_SIZE_KEY);

	private static final Log LOG = LogFactory.getLog(GuiceServletConfig.class);
	private Injector injector;

	@Override
	protected Injector getInjector() {
		if (injector == null) {
			final Module applicationModule = initApplicationModules();
			final Module servletModule = initApplicationServletModule();
			final Module instrumentationModule = new AppInstrumentationModule();
			injector = Guice.createInjector(applicationModule, servletModule, instrumentationModule);
		}
		return injector;
	}

	private Module initApplicationModules() {
		final ListeningExecutorService executerService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_POOL_SIZE));
		// bind all of our service dependencies
		final Module applicationModule = new AbstractModule() {
			@Override
			protected void configure() {
				install(new SecurityModule());
				install(new PersistenceModule());
				install(new RestModule());

				bind(SeedDummyData.class);
				bind(Tasks.class);
				bind(ListeningExecutorService.class).toInstance(executerService);
				LOG.info("application modules initialized.");
			}
		};
		return applicationModule;
	}

	private Module initApplicationServletModule() {
		final Module applicationServletModule = new ServletModule() {
			@Override
			protected void configureServlets() {
				filter("/s/*").through(SecurityFilter.class);
				filter("/*").through(ThreadNameFilter.class);
				filter("/*").through(CharsetEncodingFilter.class);
				filter("/*").through(DisableUrlSessionFilter.class);
				filter("/*").through(PersistFilter.class);

				serve("/", "/login").with(LoginServlet.class);
				serve("/logout").with(LogoutServlet.class);
				serve("/s/home").with(HomeServlet.class);

				// bootstrap the dummy data for testing
				serve("/dummydata").with(InsertDummyData.class);

				bind(ViewRenderer.class).to(FreemarkerViewRenderer.class).asEagerSingleton();
				LOG.info("application servlet module initialized.");
			}
		};
		return applicationServletModule;
	}

	@Provides
	@Singleton
	public ObjectMapper provideJacksonMapper() {
		ObjectMapper mapper = new ObjectMapper();

		// adding modules
		mapper.registerModule(new GuavaModule());
		mapper.registerModule(new JodaModule());
		// AfterburnerModule uses bytecode generation to further speed up data
		// binding
		// (+30-40% throughput for serialization, deserialization)
		mapper.registerModule(new AfterburnerModule());

		// configuring mapper
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		return mapper;
	}
}