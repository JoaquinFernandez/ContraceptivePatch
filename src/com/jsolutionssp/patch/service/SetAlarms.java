package com.jsolutionssp.patch.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.PatchLogics;
import com.jsolutionssp.patch.widget.WidgetProvider;

public class SetAlarms extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences settings = context.getSharedPreferences(PatchActivity.PREFS_NAME, 0);
		int diary = settings.getInt("diaryAlarm", 0);
		int cycle = settings.getInt("cycleAlarm", 0);
		int startCycleDayofYear = settings.getInt("startCycleDayofYear", -1);
		int startCycleYear = settings.getInt("startCycleYear", -1);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (diary == 1 && startCycleDayofYear != -1 && startCycleYear != -1) {
			long putRingAlarm = PatchLogics.putRingAlarm(settings);
			if (putRingAlarm != 0) {
				Intent alarmIntent = new Intent(context, PutPatchAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, putRingAlarm,pendingIntent);
			}
			long removeRingAlarm = PatchLogics.removeRingAlarm(settings);
			if (removeRingAlarm != 0) {
				Intent alarmIntent = new Intent(context, RemovePatchAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, removeRingAlarm,pendingIntent);
			}
		}
		if (cycle == 1 && startCycleDayofYear != -1 && startCycleYear != -1) {
			long cycleAlarm = PatchLogics.cycleAlarm(settings);
			if (cycleAlarm != 0) {
				Intent alarmIntent = new Intent(context, CycleAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, cycleAlarm,pendingIntent);
			}
		}
		GregorianCalendar date = new GregorianCalendar();
		//Update widget info
		WidgetProvider.infoUpdate(PatchLogics.isRingDay(settings, 
				date.get(Calendar.DAY_OF_YEAR), date.get(Calendar.YEAR)));
		WidgetProvider.updateWidgetContent(context,
			    AppWidgetManager.getInstance(context));
		//Set next alarm if not changed before
		date.set(Calendar.DAY_OF_YEAR, (date.get(Calendar.DAY_OF_YEAR) + 1));
		long alarm = date.getTimeInMillis();
		Intent alarmIntent = new Intent(context, SetAlarms.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarm,pendingIntent);
	}
}