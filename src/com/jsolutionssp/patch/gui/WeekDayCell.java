package com.jsolutionssp.patch.gui;

import com.jsolutionssp.patch.ChangeTheme;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class WeekDayCell extends TextView {

	public WeekDayCell(Context context, String text) {
		super(context);
		setBackgroundResource(ChangeTheme.weekDayBG);
		setTextColor(Color.BLACK);
		setText(text);
		setSingleLine();
		setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	}
}

