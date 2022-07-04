package com.inventory.utils;

import java.util.Date;

public class DateUtils {
	public static long DAY_MILLIS = 86400000;
	public static Date nextDays(int d) {
		return new Date(System.currentTimeMillis() + (DAY_MILLIS * d));
	}
}
