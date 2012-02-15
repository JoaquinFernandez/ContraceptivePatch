package com.jsolutionssp.patch.gui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.PatchLogics;
import com.jsolutionssp.patch.R;
import com.jsolutionssp.patch.service.SetAlarms;

public class CalendarCell extends Button {

	private SharedPreferences settings;

	private Context context;

	private Editor editor;

	private int representingDay;

	private int representingMonth;

	private int representingYear;

	private int representingDayofYear;

	private GestureDetector gestureDetector;

	private int actualDay;

	private int actualYear;

	public CalendarCell(Context context, int i, int firstRepresentingDay, int representingYear, int realRepresentingMonth, GestureDetector gestureDetector) {
		super(context);
		//Initialize variables
		settings = context.getSharedPreferences(PatchActivity.PREFS_NAME, 0);
		editor = settings.edit();
		this.context = context;
		this.representingYear = representingYear;
		this.gestureDetector = gestureDetector;
		int startCycleDayofYear = settings.getInt("startCycleDayofYear", -1);
		int startCycleYear = settings.getInt("startCycleYear", -1);
		//Store the actual day and year
		GregorianCalendar calendar = new GregorianCalendar();
		actualDay = calendar.get(Calendar.DAY_OF_YEAR);
		actualYear = calendar.get(Calendar.YEAR);
		//The day we're representing in Day of the year
		representingDayofYear = i + firstRepresentingDay;
		//we actualize our calendar
		calendar.set(Calendar.DAY_OF_YEAR, representingDayofYear);
		calendar.set(Calendar.YEAR, representingYear);
		representingDay = calendar.get(Calendar.DAY_OF_MONTH);
		representingMonth = calendar.get(Calendar.MONTH);
		//We set the label of the button
		String dayS = String.valueOf(representingDay);
		setSingleLine();
		setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		setText(dayS);
		//If there is no choose day
		if (startCycleDayofYear == -1 || startCycleYear == -1);
		//else
		else
			setBackgroundResource(setDays(actualDay));
		if (realRepresentingMonth != representingMonth)
			setTextColor(R.color.grey);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (settings.getBoolean("firstRunDayCell", true)) {
				editor.putInt("startCycleDayofYear", representingDayofYear);
				editor.putInt("startCycleYear", representingYear);
				editor.putBoolean("firstRunDayCell", false);
				editor.commit();
				CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
				cal.fillGrid();
				Toast t = Toast.makeText(context, R.string.alarm_info_toast, Toast.LENGTH_LONG);
				//Twice so it stays the double of time
				t.show();
				t.show();
				return true;
			}
			else {
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.day_touched_dialog);
				String text = representingDay + "/" + (representingMonth + 1) + "/" + representingYear;
				dialog.setTitle(text);

				Button buttonOK = (Button) dialog.findViewById(R.id.day_touched_button_ok);
				buttonOK.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						editor.putInt("startCycleDayofYear", representingDayofYear);
						editor.putInt("startCycleYear", representingYear);
						editor.commit();
						CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
						cal.fillGrid();
						dialog.dismiss();
						Intent i = new Intent(context, SetAlarms.class);
						i.setAction("com.jsolutionssp.patch.updateAlarm");
						context.sendBroadcast(i);
					}
				});
				Button buttonNO = (Button) dialog.findViewById(R.id.day_touched_button_no);
				buttonNO.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
				return true;
			}
		}
		return super.onTouchEvent(event);	
	}

	private int setDays(int actualDay) {

		boolean ringDay = PatchLogics.isRingDay(settings, representingDayofYear, representingYear);
		//We don't have to take care of if day number is negative or higher than 365/366 because calendar manages it automatically
		
		//The representingDayofYear relative to the actual day
		int relativeDayofYear = relativeDayofYear();
		
		boolean isPutRingDay = PatchLogics.isPutRingDay(settings, representingDayofYear, representingYear);
		boolean isRemoveRingDay = PatchLogics.isRemoveRingDay(settings, representingDayofYear, representingYear);
		
		if (relativeDayofYear < actualDay) { //We're representing a past day
			if (isPutRingDay)
				return R.drawable.patch_take_past;
			else if (isRemoveRingDay)
				return R.drawable.patch_remove_past;
			else if (ringDay)
				return R.drawable.patch_past;
			else
				return R.drawable.smiley_past;
		}
		else if (relativeDayofYear == actualDay) { //We're representing today
			if (isPutRingDay)
				return R.drawable.patch_take_today;
			else if (isRemoveRingDay)
				return R.drawable.patch_remove_today;
			if (ringDay)
				return R.drawable.patch_today;
			else
				return R.drawable.smiley_today;
		}
		else { //We're representing a future day
			if (isPutRingDay)
				return R.drawable.patch_take;
			else if (isRemoveRingDay)
				return R.drawable.patch_remove;
			if (ringDay)
				return R.drawable.patch;
			else
				return R.drawable.smiley;
		}
	}

	private int relativeDayofYear() {
		if (actualYear == representingYear)
			return representingDayofYear;
		else if (representingYear > actualYear)
			return (actualDay + 1);
		else 
			return (actualDay - 1);
	}

}

