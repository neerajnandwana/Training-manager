package com.mgr.training.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mgr.training.AppSession;
import com.mgr.training.util.Routes;
import com.mgr.training.util.UrlUtils;

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
		Map<String, String> params = Maps.newHashMap();
		if(!session.get().isLoggedIn()){
			params.put("curl", UrlUtils.getFullUrl(req));
			resp.sendRedirect(Routes.logout(req, params));
			return;
		}
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}