package com.jsolutionssp.patch.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.R;

public class WidgetProvider extends AppWidgetProvider {

	public static final String DEBUG_TAG = "TutWidgetProvider";
	private static boolean isRingDay;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		try {
			updateWidgetContent(context, appWidgetManager);
		} catch (Exception e) {
		}
	}

	public static void updateWidgetContent(Context context,
			AppWidgetManager appWidgetManager) {

		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_layout);

		Intent launchAppIntent = new Intent(context, PatchActivity.class);
		PendingIntent launchAppPendingIntent = PendingIntent.getActivity(context,
				0, launchAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteView.setOnClickPendingIntent(R.id.full_widget, launchAppPendingIntent);
		if (isRingDay) {
			remoteView.setImageViewResource(R.id.logo_widget, R.drawable.patch_icon);
			remoteView.setTextViewText(R.id.text_widget, context.getResources().getString(R.string.ring_name));
		}
		else {
			remoteView.setImageViewResource(R.id.logo_widget, R.drawable.smiley_icon);
			remoteView.setTextViewText(R.id.text_widget, context.getResources().getString(R.string.rest_name));
		}
		ComponentName listWidget = new ComponentName(context,
				WidgetProvider.class);
		appWidgetManager.updateAppWidget(listWidget, remoteView);
	}

	public static void infoUpdate(boolean bool) {
		isRingDay = bool;
	}
}
