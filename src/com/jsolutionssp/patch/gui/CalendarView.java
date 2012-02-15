
package com.jsolutionssp.patch.gui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsolutionssp.patch.ChangeTheme;
import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.R;
import com.jsolutionssp.patch.adapter.GridCalendarAdapter;
import com.jsolutionssp.patch.adapter.GridWeekDayAdapter;

public class CalendarView extends LinearLayout {

	private Context context;

	private final String[] months;

	private LinearLayout calendarLayout;

	private int month;
	
	private int year;
	
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		calendarLayout = (LinearLayout) layoutInflater.inflate(R.layout.calendar, this);
		this.context = context;
		months = getResources().getStringArray(R.array.months);
		GregorianCalendar calendar = new GregorianCalendar();
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
		fillGrid();
		setMonth();
		setListeners();
	}
	private void setListeners() {
		final ImageView nextMonth = (ImageView) calendarLayout.findViewById(R.id.nextMonth);
		final ImageView prevMonth = (ImageView) calendarLayout.findViewById(R.id.prevMonth);
		nextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextMonth.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_arrow));
				if (month == 11) {
					month = 0;
					year++;
				}
				else
					month++;
				fillGrid();
				setMonth();				
			}

		});
		prevMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prevMonth.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_arrow));
				if (month == 0) {
					month = PatchActivity.months;
					year--;
				}
				else
					month--;
				fillGrid();
				setMonth();				
			}
		});
	}

	private void setMonth() {
		String monthString = months[month];
		Button currentMonth = (Button) calendarLayout.findViewById(R.id.currentMonth);
		currentMonth.setText(monthString + " " + year);
	}

	public void fillGrid() {
		setTheme();
		GridView calendarGrid = (GridView) calendarLayout.findViewById(R.id.grid_calendar);
		calendarGrid.setAdapter(new GridCalendarAdapter(context, calendarLayout, month, year));
		GridView calendarWeekDayGrid = (GridView) calendarLayout.findViewById(R.id.grid_days);
		calendarWeekDayGrid.setAdapter(new GridWeekDayAdapter(context));
	}
	
	private void setTheme() {
		Button button = ((Button) findViewById(R.id.currentMonth));
		button.setBackgroundResource(ChangeTheme.calendarBar);
		ImageView img = ((ImageView) findViewById(R.id.prevMonth));
		img.setImageResource(ChangeTheme.leftArrow);
		img = ((ImageView) findViewById(R.id.nextMonth));
		img.setImageResource(ChangeTheme.rightArrow);
		PatchActivity act = ((PatchActivity) context);
		act.setTabBackground();
		//The week header will be considered on GridAdapter
	}
	@Override
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			((Activity) context).finish();
			return true;
		}
		return false;
	}
}
