package com.mgr.training;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.google.inject.servlet.SessionScoped;
import com.mgr.training.data.User;

@SessionScoped
public class AppSession {

	private static final User LOGGED_OUT = User.anonymous();

	private User user = LOGGED_OUT;

	private final HttpSession httpSession;

	@Inject
	public AppSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public boolean isLoggedIn() {
		return getUser() != LOGGED_OUT;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user != null ? user : LOGGED_OUT;
		httpSession.setAttribute(AppSession.class.getName(), Boolean.valueOf(user != LOGGED_OUT));
	}
}
