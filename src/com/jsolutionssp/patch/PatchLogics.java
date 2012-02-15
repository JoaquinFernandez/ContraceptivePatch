package com.jsolutionssp.patch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.SharedPreferences;

public class PatchLogics {

	private static GregorianCalendar calendar;
	private static SharedPreferences settings;

	public static boolean isRingDay(SharedPreferences sett, int repDay, int repYear) {
		calendar = new GregorianCalendar();
		settings = sett;
		int dayNumber = getDayNumber(repDay, repYear);
		return isRingDay(dayNumber);
	}

	public static long cycleAlarm(SharedPreferences sett) {
		settings = sett;
		calendar = new GregorianCalendar();
		return nextCycleDay();
	}
	
	public static boolean isPutRingDay(SharedPreferences sett, int repDay, int repYear) {
		calendar = new GregorianCalendar();
		settings = sett;
		
		int day = getDayNumber(repDay, repYear);
		
		if (isRingDay(day) && !isRingDay(day - 1))
			//if yesterday was not ring day, today is the put ring day
				return true;
		if (isRingDay(day) && !isRingDay(day - 8) && isRingDay(day - 7))
			return true;
		if (isRingDay(day) && !isRingDay(day - 15) && isRingDay(day - 14))
			return true;
		return false;
	}
	
	public static boolean isRemoveRingDay(SharedPreferences sett, int repDay, int repYear) {
		calendar = new GregorianCalendar();
		settings = sett;
		
		int day = getDayNumber(repDay, repYear);
		
		if (!isRingDay(day) && isRingDay(day - 1))
			//if yesterday was ring day, and today it's not, today is the remove ring day
				return true;
		return false;
	}

	public static long putRingAlarm(SharedPreferences sett) {
		calendar = new GregorianCalendar();
		settings = sett;

		int actualDay = calendar.get(Calendar.DAY_OF_YEAR);
		int actualYear = calendar.get(Calendar.YEAR);
		int settingHour = settings.getInt("diaryHourNotification", -1);
		int settingMinute = settings.getInt("diaryMinuteNotification", -1);
		int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
		int actualMinute = calendar.get(Calendar.MINUTE);
		int putRingDay = 0;

		//check if it has already passed the day
		if (settingHour < actualHour || (settingHour == actualHour && settingMinute <= actualMinute))
			actualDay++;
		int day = getDayNumber(actualDay, actualYear);

		//if today is ring day
		if (isRingDay(day)) {
			//if yesterday was not ring day, today is the put ring day
			if (!isRingDay(day - 1)) {
				putRingDay = actualDay;
			}
			else {
				int leftDays = 0;
				while (isRingDay(day++)) {
					actualDay++;//I increment days until it's not ring day
					leftDays++;
				}
				if (leftDays < 7)
					//i add the rest days, so i get the first ring day (put ring day)
					putRingDay += settings.getInt("ringRestDays", -1);
				else if (leftDays >= 7 && leftDays < 14)
					putRingDay -= 7;
				else
					putRingDay -= 14;
				putRingDay += actualDay;
			}
		}
		//if today is not ring day
		else {
			while (!isRingDay(day++)) {
				actualDay++;//I increment days until it's ring day
			}
			putRingDay = actualDay;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, putRingDay);
		cal.set(Calendar.HOUR_OF_DAY, settingHour);
		cal.set(Calendar.MINUTE, settingMinute);
		long date = cal.getTimeInMillis();
		return date;
	}

	public static long removeRingAlarm(SharedPreferences sett) {
		calendar = new GregorianCalendar();
		settings = sett;

		int actualDay = calendar.get(Calendar.DAY_OF_YEAR);
		int actualYear = calendar.get(Calendar.YEAR);
		int settingHour = settings.getInt("diaryHourNotification", -1);
		int settingMinute = settings.getInt("diaryMinuteNotification", -1);
		int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
		int actualMinute = calendar.get(Calendar.MINUTE);
		int removeRingDay = 0;

		//check if it has already passed the day
		if (settingHour < actualHour || (settingHour == actualHour && settingMinute <= actualMinute))
			actualDay++;
		int day = getDayNumber(actualDay, actualYear);

		//if today is ring day
		if (isRingDay(day)) {
			while (isRingDay(day++)) {
				actualDay++;//I increment days until it's not ring day
			}
			//as it's the first not ring day
			removeRingDay = actualDay;
		}
		//if today is not ring day
		else {
			if (isRingDay(day - 1)) {
				removeRingDay = actualDay;
			}
			else {
				while (!isRingDay(day++)) {
					actualDay++;//I increment days until it's ring day
				}
				removeRingDay = settings.getInt("ringTakingDays", -1);
				removeRingDay += actualDay; // i add all the ring days to get to the first not ring Day
			}
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, removeRingDay);
		cal.set(Calendar.HOUR_OF_DAY, settingHour);
		cal.set(Calendar.MINUTE, settingMinute);
		long date = cal.getTimeInMillis();
		return date;
	}

	//Number of days since (or from when) i (will) took the ring
	private static int getDayNumber(int representingDayofYear, int representingYear) {
		//Keep in mind that days are negative if: myDay < day i took the ring
		int dayofrepresent = representingDayofYear;
		int startCycleDayofYear = settings.getInt("startCycleDayofYear", -1);
		int startCycleYear = settings.getInt("startCycleYear", -1);
		calendar.set(Calendar.YEAR, startCycleYear);
		calendar.set(Calendar.DAY_OF_YEAR, startCycleDayofYear);

		while (representingYear > startCycleYear) {
			int daysSince = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
			startCycleYear++;
			calendar.set(Calendar.YEAR, startCycleYear); //I already incremented the year
			//I don't substract the startCycleDayofYear cause it will do it anyway at the end
			dayofrepresent += daysSince;
		}
		while (representingYear < startCycleYear) {
			//As they're future days, they're negatives
			startCycleYear--;
			calendar.set(Calendar.YEAR, startCycleYear);
			//I update the year before getting the days, because i have no interest in the days of that year
			int daysSince = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
			if (startCycleYear == representingYear)
				daysSince += (2*startCycleDayofYear); //I do it twice because after it will substract once
			dayofrepresent -= daysSince;
		}
		if (startCycleYear == representingYear)
			dayofrepresent -= startCycleDayofYear;
		calendar = new GregorianCalendar();
		return dayofrepresent;
	}
	private static boolean isRingDay(int dayNumber) {
		int ringTakingDays = settings.getInt("ringTakingDays", -1);
		int ringRestDays = settings.getInt("ringRestDays", -1);
		if (dayNumber <= 0) {
			int multi = (int) (dayNumber/(ringTakingDays + ringRestDays));//We get a multiplicator to normalize the dayNumber value
			dayNumber = (dayNumber - (multi*(ringTakingDays + ringRestDays))); //We get the day in between [-27, 0]

			//if we're not in the rest days previous to take the ring is ring time
			if ((dayNumber < (-ringRestDays)) || (dayNumber == 0))
				return true;
			//if we're is rest time
			return false;
		}
		else {
			int multi = (int) (dayNumber/(ringTakingDays + ringRestDays));//We get a multiplicator to normalize the dayNumber value
			dayNumber = (dayNumber - (multi*(ringTakingDays + ringRestDays))); //We get the day in between (0, 27)
			//if we're in the next [0,20] days from taking the ring, is ring time
			if (dayNumber < ringTakingDays)
				return true;
			//else, is rest time
			return false;
		}
	}

	private static long nextCycleDay() {
		int startCycleDay;
		int actualDay = calendar.get(Calendar.DAY_OF_YEAR);
		int actualYear = calendar.get(Calendar.YEAR);

		//Now we get the hour of the alarm
		int settingHour = settings.getInt("cycleHourNotification", 0);
		int settingMinute = settings.getInt("cycleMinuteNotification", 0);
		int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
		int actualMinute = calendar.get(Calendar.MINUTE);
		//check if it has already passed the day
		if (settingHour < actualHour || (settingHour == actualHour && settingMinute <= actualMinute))
			actualDay++;
		int day = getDayNumber(actualDay, actualYear);

		if (isRingDay(day)) {
			if (!isRingDay(day - 1)) {
				startCycleDay = actualDay;
			}
			else {
				while (isRingDay(day++)) {
					actualDay++;//I increment days until it's not ring day
				}
				startCycleDay = settings.getInt("ringRestDays", -1);
				startCycleDay += actualDay;
			}
		}
		else {
			while (!isRingDay(day++)) {
				actualDay++;//I increment days until it's not ring day
			}
			startCycleDay = actualDay;
		}
		//we get the offset from today in wich it will be the start cycle day
		int prevAlarmDays = settings.getInt("prevAlarmDays", 0);
		startCycleDay -= prevAlarmDays;
		//If that day has passed, then we set, next Cycle's alarm
		if (startCycleDay < calendar.get(Calendar.DAY_OF_YEAR)) {
			startCycleDay += settings.getInt("ringRestDays", -1);
			startCycleDay += settings.getInt("ringTakingDays", -1);
		}
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, startCycleDay);
		cal.set(Calendar.HOUR_OF_DAY, settingHour);
		cal.set(Calendar.MINUTE, settingMinute);
		long date = cal.getTimeInMillis();
		return date;
	}
}
