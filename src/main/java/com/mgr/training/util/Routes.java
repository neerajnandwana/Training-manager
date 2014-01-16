package com.mgr.training.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Routes {
	private Routes() {
	}
	
	public static String login(HttpServletRequest req){		
		return req.getContextPath() + "/index.html#/login";
	}
	
	public static String login(HttpServletRequest req, Map<String, String> params){	
		return login(req) + "?" + UrlEncodeUtils.urlEncode(params);
	}
	
	public static String logout(HttpServletRequest req){
		return req.getContextPath() + "/logout";
	}
	
	public static String home(HttpServletRequest req){
		return req.getContextPath() + "/s/index.html";
	}
}
