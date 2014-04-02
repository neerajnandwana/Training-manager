package com.mgr.training.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import com.google.common.base.Preconditions;

public final class UrlUtils {
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";

	private UrlUtils() {
	}

	public static String encode(final String value) {
		String encodedValue = "";
		try {
			if (!Utils.isNullOrEmpty(value)) {
				encodedValue = URLEncoder.encode(value, Const.CHAR_SET.toString()).replace("+", "%20");
			}
		} catch (UnsupportedEncodingException e) {
			throw new WebApplicationException(e);
		}
		return encodedValue;
	}

	public static String urlEncode(final Map<String, String> asMap) {
		Preconditions.checkNotNull(asMap);
		final StringBuilder urlParam = new StringBuilder();
		for (Entry<String, String> entry : asMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (urlParam.length() > 0) {
				urlParam.append(PARAMETER_SEPARATOR);
			}
			urlParam.append(encode(key)).append(NAME_VALUE_SEPARATOR).append(encode(value));
		}
		return urlParam.toString();
	}

	public static String getFullUrl(HttpServletRequest request) {
		Preconditions.checkNotNull(request);
		final StringBuilder url = new StringBuilder(100).append(request.getRequestURI());
		if (request.getQueryString() != null) {
			url.append('?').append(request.getQueryString());
		}
		return url.toString();
	}

}
