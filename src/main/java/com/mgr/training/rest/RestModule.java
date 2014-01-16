package com.mgr.training.rest;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Scopes;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class RestModule extends JerseyServletModule {

	@Override
	protected void configureServlets() {
		bind(UserResource.class);
		bind(EmployeeResource.class);
		bind(TrainingResource.class);
		
		/* bind jackson converters for JAXB/JSON serialization */		
		bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);
		bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
		bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

		serve("/r/*").with(GuiceContainer.class);
	}
}
