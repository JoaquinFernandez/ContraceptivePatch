package com.jsolutionssp.patch.gui;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageView;

public class CalendarGestureDetector extends SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 80;
	private ImageView nextMonth;
	private ImageView prevMonth;
    
    public CalendarGestureDetector(ImageView nextMonth, ImageView prevMonth) {
		this.nextMonth = nextMonth;
		this.prevMonth = prevMonth;
	}

	@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            // right to left
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
            	nextMonth.performClick();
            	return true;
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
            	prevMonth.performClick();
            	return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}