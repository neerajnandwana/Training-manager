package com.mgr.training.rest;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.mgr.training.rest.internal.AppResponse;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class RestModule extends JerseyServletModule {

	@Override
	protected void configureServlets() {
		/* bind wrapper response */
		bind(AppResponse.class);
		
		/* bind resources*/
		bind(UserResource.class);
		bind(EmployeeResource.class);
		bind(TrainingResource.class);
		
		/* bind jackson converters for JAXB/JSON serialization */		
		bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);
		bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
		bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

		serve("/r/*").with(GuiceContainer.class);
		

        /* create wrapper for all rest service response */
        MethodInterceptor interceptor = new RestResponseInterceptor();
        requestInjection(interceptor);
        bindInterceptor(any(), annotatedWith(GET.class), interceptor);
        bindInterceptor(any(), annotatedWith(POST.class), interceptor);
        bindInterceptor(any(), annotatedWith(PUT.class), interceptor);
        bindInterceptor(any(), annotatedWith(DELETE.class), interceptor);
	}
	
	class RestResponseInterceptor implements MethodInterceptor {
		@Inject private Provider<AppResponse> responseProvider;

		public Object invoke(MethodInvocation methodInvocation) throws Throwable {
			AppResponse response = responseProvider.get();
			Object result = null;
			try{
				result = methodInvocation.proceed();
				response.setResult(result);
			} catch(Throwable e){
				response.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
			}
			return response;
		}
	}
}
