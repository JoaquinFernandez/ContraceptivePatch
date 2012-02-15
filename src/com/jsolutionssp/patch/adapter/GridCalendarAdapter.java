package com.jsolutionssp.patch.adapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.R;
import com.jsolutionssp.patch.gui.CalendarCell;
import com.jsolutionssp.patch.gui.CalendarGestureDetector;

public class GridCalendarAdapter extends BaseAdapter {
	
	Context context;
	
	private int count;

	private SharedPreferences settings;

	private int firstRepresentingDay;

	private int year;

	private int month;

	private GestureDetector gestureDetector;

	public GridCalendarAdapter(Context context, LinearLayout calendarLayout, int month, int year)	{
		this.context = context;
		settings = context.getSharedPreferences(PatchActivity.PREFS_NAME, 0);
		this.year = year;
		this.month = month;

		firstRepresentingDay = getFirstRepresentingDay();
		
		ImageView nextMonth = (ImageView) calendarLayout.findViewById(R.id.nextMonth);
		ImageView prevMonth = (ImageView) calendarLayout.findViewById(R.id.prevMonth);
		gestureDetector = new GestureDetector(new CalendarGestureDetector(nextMonth, prevMonth));
	}

	private int getFirstRepresentingDay() { //Settings -> Select First Day Of Week
		int firstRepresentingDay;
		GregorianCalendar myCalendar = new GregorianCalendar(year, month, 1);
		
		int daysofMonth = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int firstDayMonth = myCalendar.get(Calendar.DAY_OF_WEEK); // First day of month (relative to the week)
		int globalFirstDayMonth = myCalendar.get(Calendar.DAY_OF_YEAR);
		
		if (settings.getBoolean("firstDayofWeek", true)) { //The default option is the week starting on monday
			firstDayMonth = firstDayMonth - 1;
			if (firstDayMonth == 0)
				firstDayMonth = 7;
			firstRepresentingDay = globalFirstDayMonth - (firstDayMonth - 1);
		}
		else { //else we start the week on Sunday
			firstRepresentingDay = globalFirstDayMonth - (firstDayMonth - 1);
		}
		if (firstDayMonth + daysofMonth < 37)
			count = PatchActivity.five_week_calendar;
		else
			count = PatchActivity.six_week_calendar;
		
		return firstRepresentingDay;
	}

	@Override
	public int getCount() {
		//return numbers of element u want on the grid
		return count;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Button button;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			button = new CalendarCell(context, position, firstRepresentingDay, year, month, gestureDetector);
		} else {
			button = (Button) convertView;
		}
		return button;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
