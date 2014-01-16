package com.mgr.training.util;


final public class Utils {
	private Utils(){}
	
	public static boolean isNullOrEmpty(final String s){
		if(s == null){
			return true;
		}
		return s.trim().length() == 0;
	}
}
