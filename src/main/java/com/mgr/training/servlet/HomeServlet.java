package com.mgr.training.servlet;

import java.io.IOException;

import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mgr.training.AppSession;
import com.mgr.training.data.User;
import com.mgr.training.servlet.view.Home;
import com.mgr.training.view.ViewRenderer;

@Singleton
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Provider<AppSession> sessionProvider;
	private final ObjectMapper mapper;
	private final ViewRenderer renderer;

	@Inject
	public HomeServlet(Provider<AppSession> sessionProvider, ObjectMapper mapper, ViewRenderer renderer) {
		this.sessionProvider = sessionProvider;
		this.mapper = mapper;
		this.renderer = renderer;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final User user = sessionProvider.get().getUser();
		renderer.render(new Home(user, mapper), resp.getLocale(), resp.getOutputStream());
	}

}
