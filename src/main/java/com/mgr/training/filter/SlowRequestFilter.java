package com.mgr.training.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mgr.training.util.UrlUtils;

/**
 * A servlet filter which logs the methods and URIs of requests which take
 * longer than a given duration of time to complete.
 */
@Singleton
public class SlowRequestFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SlowRequestFilter.class);
	private final long threshold;

	@Inject
	public SlowRequestFilter(@Named("SlowRequestThreshold") long threshold) {
		this.threshold = threshold;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final Stopwatch stopwatch = Stopwatch.createStarted();
		try {
			chain.doFilter(request, response);
		} finally {
			final long elapsedMS = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
			if (elapsedMS >= threshold) {
				LOGGER.warn("Slow request: {} {} ({}ms)", req.getMethod(), UrlUtils.getFullUrl(req), elapsedMS);
			}
		}
	}
}
