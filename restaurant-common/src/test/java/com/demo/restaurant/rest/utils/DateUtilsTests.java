package com.demo.restaurant.rest.utils;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTests {

	private static final SimpleDateFormat ONLY_DATE = new SimpleDateFormat("dd/MM/yyyy"); 
	
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int ELEVEN = 11;

	@Test
	public void should_return_correct_day() {
		// ARRANGE
		Calendar now = Calendar.getInstance();
		Calendar checkToday =Calendar.getInstance();
		checkToday.set(Calendar.HOUR_OF_DAY,ZERO);
		checkToday.set(Calendar.MINUTE,ZERO);
		checkToday.set(Calendar.SECOND,ZERO);
		checkToday.set(Calendar.MILLISECOND,ZERO);
		Calendar checkTomorrow =Calendar.getInstance();
		checkTomorrow.add(Calendar.DAY_OF_YEAR, ONE);
		checkTomorrow.set(Calendar.HOUR_OF_DAY,ZERO);
		checkTomorrow.set(Calendar.MINUTE,ZERO);
		checkTomorrow.set(Calendar.SECOND,ZERO);
		checkTomorrow.set(Calendar.MILLISECOND,ZERO);
		// ACT
		Date dayToServe = DateUtils.calculateDayToServe(now.getTime());
		// ASSERT
		if(now.get(Calendar.HOUR_OF_DAY) >= ELEVEN) {
			assertEquals(checkTomorrow.getTime(),dayToServe );
		} else {
			assertEquals(checkToday.getTime(),dayToServe );
		}
		
	}
	
	@Test
	public void should_return_correct_difference_of_days() {
		// ARRANGE
		Calendar now = Calendar.getInstance();
		Calendar future = Calendar.getInstance();
		future.add(Calendar.DAY_OF_YEAR, ELEVEN);
		// ACT
		int daysDiferrence = DateUtils.daysBetweenDates(now.getTime(), future.getTime());
		// ASSERT
		assertEquals(ELEVEN, daysDiferrence);
	}
	
	@Test
	public void should_return_start_of_day() throws ParseException {
		// ARRANGE
		Calendar today = Calendar.getInstance();
		Date startDay = ONLY_DATE.parse(ONLY_DATE.format(today.getTime()));
		// ACT
		Date normalized = DateUtils.normalizeDate(today.getTime());
		// ASSERT
		assertEquals(startDay, normalized);
	}
}
