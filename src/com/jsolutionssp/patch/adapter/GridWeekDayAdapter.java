package com.jsolutionssp.patch.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.R;
import com.jsolutionssp.patch.gui.WeekDayCell;

public class GridWeekDayAdapter extends BaseAdapter {
	
	Context context;

	private SharedPreferences settings;

	private String[] weekDays;
	
	//There are ALWAYS seven days in a week
	private int weekDaysNumber = 7;

	public GridWeekDayAdapter(Context context)	{
		this.context = context;
		settings = context.getSharedPreferences(PatchActivity.PREFS_NAME, 0);
		weekDays = context.getResources().getStringArray(R.array.weekdays_short);
		if (settings.getBoolean("firstDayofWeek", true)) {//By default Monday
			
		}
		else {//If the first day is sunday
			String firstDay = weekDays[weekDaysNumber - 1];
			//For all the days
			for (int i = weekDaysNumber - 1; i >= 0; i--) {
				//if its the first day (monday) change for the last (sunday) 
				if (i == 0)
					weekDays[i] = firstDay;
				//else, each day has to pass to the next one
				else
					weekDays[i] = weekDays[i - 1];

			}
		}
	}

	@Override
	public int getCount() {
		return weekDaysNumber;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView button;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			button = new WeekDayCell(context, weekDays[position]);
		} else {
			button = (TextView) convertView;
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
