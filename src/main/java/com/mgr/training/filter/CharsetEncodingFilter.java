package com.mgr.training.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.inject.Singleton;
import com.mgr.training.util.Const;

/**
 * Utility class for Charset encoding Servlet Filter. This filter would be used to
 * consistently set the character encoding as Application Charset.
 * 
 */
@Singleton
public class CharsetEncodingFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(Const.CHAR_SET.toString());
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
