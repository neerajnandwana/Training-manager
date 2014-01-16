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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mgr.training.AppSession;
import com.mgr.training.util.Routes;

@Singleton
public class SecurityFilter implements Filter {
	private final Provider<AppSession> session;
	
	@Inject
	public SecurityFilter(Provider<AppSession> session){
		this.session = session;
	}
	
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if(!session.get().isLoggedIn()){
			resp.sendRedirect(Routes.logout(req));
			return;
		}
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}