package com.mgr.training.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Splitter;
import com.google.common.net.InetAddresses;

public class IP {
	private static final Splitter ipSplitter = Splitter.on(",");
	private static final String[] HEADERS_TO_TRY = {
		"X-Forwarded-For",
	    "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR" 		
	};
	
	public static boolean isPrivate(String ip){
		ip = ip.trim();
		if(InetAddresses.isInetAddress(ip)){
			try {
				return InetAddress.getByName(ip).isSiteLocalAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static String get(HttpServletRequest request) {  
		String ip = null;
		for(String header: HEADERS_TO_TRY){
			ip = request.getHeader(header);
			if(!Utils.isNullOrEmpty(header) && !"unknown".equalsIgnoreCase(header)){
				for(String _ip: ipSplitter.split(ip)){
					if(!isPrivate(_ip)){
						return _ip;
					}
				}
			}
		}
		return request.getRemoteAddr();
	}
}
