package com.demo.restaurant.rest.utils;

import java.util.Calendar;
import java.util.Date;

import lombok.NonNull;

public class DateUtils {

	private static final int MILISECONDS_PER_DAY = 86400000;

	private DateUtils() {
	}

	public static Date calculateDayToServe(Date dayOrder) {
		Calendar now = Calendar.getInstance();
		if (dayOrder.after(endOfDay())) {
			return normalizeDate(dayOrder);
		}
		if (normalizeDate(dayOrder).equals(normalizeDate(now.getTime()))) {
			if (now.get(Calendar.HOUR_OF_DAY) > 11) {
				now.add(Calendar.DAY_OF_YEAR, 1);
				return normalizeDate(now.getTime());
			}
		}
		return normalizeDate(now.getTime());

	}

	public static Date normalizeDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static int daysBetweenDates(@NonNull Date initialDate, @NonNull Date finalDate) {
		return Math.abs((int) ((normalizeDate(initialDate).getTime() - normalizeDate(finalDate).getTime())
				/ MILISECONDS_PER_DAY));
	}

	private static Date endOfDay() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		today.set(Calendar.MILLISECOND, 999);
		return today.getTime();
	}
}
