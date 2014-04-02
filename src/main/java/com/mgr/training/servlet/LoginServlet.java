package com.mgr.training.servlet;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.mgr.training.AppSession;
import com.mgr.training.data.User;
import com.mgr.training.store.UserStore;
import com.mgr.training.util.Const;
import com.mgr.training.util.Routes;
import com.mgr.training.util.Utils;

@Singleton
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final UserStore userStore;
	private Provider<AppSession> sessionProvider;

	@Inject
	public LoginServlet(UserStore userStore, Provider<AppSession> sessionProvider) {
		this.userStore = userStore;
		this.sessionProvider = sessionProvider;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	};

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> params = Maps.newHashMap();
		String userId = req.getParameter(Const.userId);
		String userPass = req.getParameter(Const.userPass);
		String curl = req.getParameter("curl");

		if (!Utils.isNullOrEmpty(userId) && !Utils.isNullOrEmpty(userPass)) {
			userId = userId.trim();
			userPass = userPass.trim();
			AppSession session = sessionProvider.get();
			User user = session.getUser();
			if (!session.isLoggedIn()) {
				try {
					user = userStore.getByCredential(userId, userPass);
				} catch (Exception e) {
					Throwables.propagate(e);
				}
				if (user != null) {
					session.setUser(user);
				}
			}

			if (user != null) {
				if (Utils.isNullOrEmpty(curl)) {
					resp.sendRedirect(Routes.home(req));
				} else {
					curl = curl.trim();
					resp.sendRedirect(curl);
				}
				return;
			} else {
				params.put("err", "Invalid user/password. Try again!");
			}
		}

		params.put("curl", curl);
		resp.sendRedirect(Routes.login(req, params));
	}
}
