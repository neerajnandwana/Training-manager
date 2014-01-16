package com.mgr.training.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;

public class UrlEncodeUtils {
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";
	
	private UrlEncodeUtils() {}

	static String encode(final String value) {
		try {
			return URLEncoder.encode(value, Const.CHAR_SET.toString()).replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new WebApplicationException(e);
		}
	}

	static String urlEncode(final Map<String, String> asMap) {
		final StringBuilder urlParam = new StringBuilder();
		for (Entry<String, String> entry : asMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if(urlParam.length() > 0){
				urlParam.append(PARAMETER_SEPARATOR);
			}
			urlParam.append(encode(key)).append(NAME_VALUE_SEPARATOR).append(encode(value));
		}
		return urlParam.toString();
	}

}
