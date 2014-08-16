package com.mgr.training.util;

import java.util.List;

final public class Utils {
	private Utils() {
	}

	public static boolean isNullOrEmpty(final String s) {
		if (s == null) {
			return true;
		}
		return s.trim().length() == 0;
	}

	public static boolean isNullOrEmpty(final List<?> list) {
		if(list == null){
			return true;
		}
		return list.isEmpty();
	}
}
