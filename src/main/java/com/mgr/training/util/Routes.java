package com.mgr.training.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Preconditions;

public class Routes {
	private Routes() {
	}

	private static String path(HttpServletRequest req) {
		return req.getContextPath();
	}

	public static String login(HttpServletRequest req) {
		Preconditions.checkNotNull(req);
		return String.format("%s/index.html#/login", path(req));
	}

	public static String login(HttpServletRequest req, Map<String, String> params) {
		return String.format("%s?%s", login(req), UrlUtils.urlEncode(params));
	}

	public static String home(HttpServletRequest req) {
		return String.format("%s/sdt/home", path(req));
	}

	public static String logout(HttpServletRequest req) {
		return String.format("%s/logout", path(req));
	}

	public static String logout(HttpServletRequest req, Map<String, String> params) {
		return String.format("%s?%s", logout(req), UrlUtils.urlEncode(params));
	}
}
