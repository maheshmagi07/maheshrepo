package com.alcon.extraction.service.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Helper {

	public static Boolean testContains(String testString, String letterToBeTested) {

		if (testString.toUpperCase().contains(letterToBeTested.toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean testContains(List<String> testString, String letterToBeTested) {

		if (testString.contains(letterToBeTested.toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean testContainsInLower(List<String> testString, String letterToBeTested) {

		if (testString.contains(letterToBeTested.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String subString(String mainString, int startIndex, int endIndex) {

		if(startIndex <= mainString.length() && mainString.length() >= endIndex) {
			return mainString.substring(startIndex,endIndex).trim();
		}else {
			throw new IllegalStateException("Sub String isn't possible");
		}
		
		
	}

	public static boolean checkDOBPattern(String dateStr) {
		 DateFormat sdf = new SimpleDateFormat(Constants.DATE_MM_DD_YYYY);
	        sdf.setLenient(false);
	        try {
	            sdf.parse(dateStr);
	        } catch (ParseException e) {
	            return false;
	        }
	        return true;
	}
	

	public static boolean checkTimePattern(String dateStr) {
		 DateFormat sdf = new SimpleDateFormat(Constants.DATE_HH_MM_SS_A);
	        sdf.setLenient(false);
	        try {
	            sdf.parse(dateStr);
	        } catch (ParseException e) {
	            return false;
	        }
	        return true;
	}

	public static boolean testStartsWith(String testString, String letterToBeTested) {
		if (testString.toLowerCase().startsWith(letterToBeTested.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
}
