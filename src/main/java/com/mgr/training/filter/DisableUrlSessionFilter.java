package com.mgr.training.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.google.inject.Singleton;

/**
 * Filter class to suppress sessions coming up in URLs as jsessionid parameter
 * 
 */
@Singleton
public class DisableUrlSessionFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Do not process non-http requests
		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Invalidate any sessions that are being backed by session Ids
		if (httpRequest.isRequestedSessionIdFromURL()) {
			HttpSession session = httpRequest.getSession();
			if (session != null){
				session.invalidate();
			}
		}

		// Disable URL encoding by wrapping our httpResponse object
		HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {
			public String encodeRedirectURL(String url) {
				return url;
			}

			public String encodeURL(String url) {
				return url;
			}
		};

		// Filter the request finally
		chain.doFilter(request, wrappedResponse);
	}

	public void init(FilterConfig config) throws ServletException {
	}

}
