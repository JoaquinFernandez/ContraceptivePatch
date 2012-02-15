package com.jsolutionssp.patch.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.R;

public class RemovePatchAlarmTriggered extends BroadcastReceiver {
    private Context context;
	private SharedPreferences settings;

	@Override
    public void onReceive(Context context, Intent intent) {
    	this.context = context;
		settings = context.getSharedPreferences(PatchActivity.PREFS_NAME, 0);
		int diary = settings.getInt("diaryAlarm", 0);
		if (diary == 1) {
				String tickerText = context.getResources().getText(R.string.notification_bar_remove_ring_text).toString() ;
				boolean sound = false;
				if (settings.getInt("diaryAlarmSound", 0) != 0)
					sound = true;
				boolean vibrate = false;
				if (settings.getInt("diaryAlarmVibrate", 0) != 0)
					vibrate = true;

				notificate(tickerText, sound, vibrate);
		}
    }
    
    private void notificate(String title, boolean sound, boolean vibrate) {

    	String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		int icon = R.drawable.patch_icon;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, title, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent notificationIntent = new Intent(context, PatchActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, title, "", contentIntent);

		if (sound) {
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.flags |= Notification.FLAG_INSISTENT;
		}
		if (vibrate) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.vibrate = new long[] {0, 500, 300, 500, 300, 500, 300};
		}
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 600;
		notification.ledOffMS = 900;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		final int HELLO_ID = R.id.preferences_diary_alarm;
		mNotificationManager.notify(HELLO_ID, notification);
	}
}