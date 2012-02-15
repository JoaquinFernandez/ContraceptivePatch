package com.jsolutionssp.patch;

import android.content.SharedPreferences;

public class ChangeTheme {

	public static int background;

	public static int calendarBar;

	public static int leftArrow;

	public static int rightArrow;

	public static int weekDayBG;

	private static int[] pinkImageId = {
		R.drawable.bg_pink, R.drawable.calendar_bar_pink, R.drawable.arrows_left_pink,
		R.drawable.arrows_right_pink, R.drawable.week_day_bg_pink,};

	private static int[] greenImageId = {
		R.drawable.bg_green, R.drawable.calendar_bar_green, R.drawable.arrows_left_green, 
		R.drawable.arrows_right_green, R.drawable.week_day_bg_green,};

	private static int[] blueImageId = {
		R.drawable.bg_blue, R.drawable.calendar_bar_blue, R.drawable.arrows_left_blue,
		R.drawable.arrows_right_blue, R.drawable.week_day_bg_blue,};

	public static void changeTheme(int theme) {
		if (theme == 0) {
			background = pinkImageId[0];
			calendarBar = pinkImageId[1];
			leftArrow = pinkImageId[2];
			rightArrow = pinkImageId[3];
			weekDayBG = pinkImageId[4];
		}
		if (theme == 1) {
			background = greenImageId[0];
			calendarBar = greenImageId[1];
			leftArrow = greenImageId[2];
			rightArrow = greenImageId[3];
			weekDayBG = greenImageId[4];
		}
		if (theme == 2) {
			background = blueImageId[0];
			calendarBar = blueImageId[1];
			leftArrow = blueImageId[2];
			rightArrow = blueImageId[3];
			weekDayBG = blueImageId[4];
		}
	}
	public static void iniTheme(SharedPreferences settings, String[] themeNames) {
		String theme = settings.getString("actualTheme", themeNames[0]);
		if (theme.compareTo(themeNames[1]) == 0)
			ChangeTheme.changeTheme(1);
		else if (theme.compareTo(themeNames[2]) == 0)
			ChangeTheme.changeTheme(2);
		else
			ChangeTheme.changeTheme(0);

	}

}
