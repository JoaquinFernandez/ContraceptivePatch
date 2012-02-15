package com.jsolutionssp.patch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.jsolutionssp.patch.widget.WidgetProvider;

public class PatchActivity extends Activity {

	private SharedPreferences settings;

	private boolean notFirstTime = false;

	public static GoogleAnalyticsTracker tracker;

	public static String PREFS_NAME = "com.jsolutionssp.patch";

	private static String ANALYTICS_ID = "UA-26947875-6";

	public static final int five_week_calendar = 35;

	public static final int six_week_calendar = 42;

	public static final int months = 11; //From 0 to 11

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//For Google analytics
		tracker = GoogleAnalyticsTracker.getInstance();

		// Start the tracker in manual dispatch mode...
		tracker.startNewSession(ANALYTICS_ID, this);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//Theme settings
		settings = getSharedPreferences(PatchActivity.PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putInt("ringTakingDays", 21);
		editor.putInt("ringRestDays", 7);
		editor.commit();
		String[] themeNames = getResources().getStringArray(R.array.theme_names);
		ChangeTheme.iniTheme(settings, themeNames);
		setContentView(R.layout.main);
		setTabBackground();
		initialize();
		widgetUpdate();
	}

	private void widgetUpdate() {
		GregorianCalendar date = new GregorianCalendar();
		//Update widget info
		WidgetProvider.infoUpdate(PatchLogics.isRingDay(settings, 
				date.get(Calendar.DAY_OF_YEAR), date.get(Calendar.YEAR)));
		WidgetProvider.updateWidgetContent(this,
				AppWidgetManager.getInstance(this));

	}

	public void setTabBackground() {
		if (notFirstTime) {
			LinearLayout tab = (LinearLayout) findViewById(R.id.tab1);
			tab.setBackgroundResource(ChangeTheme.background);
		}
		notFirstTime = true;
	}

	private void initialize() {
		if (settings.getInt("startCycleDayofYear", -1) == -1) {
			Toast t = Toast.makeText(getApplicationContext(), R.string.set_taking_ringday_toast, Toast.LENGTH_LONG);
			//Twice so it stays the double of time
			t.show();
			t.show();
		}
		createTab();
	}

	private void createTab() {
		Resources res = getResources();

		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		tabs.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String label) {
				if (label.compareTo("Calendary") == 0)
					tracker.trackPageView("/Calendario");
				else
					tracker.trackPageView("/Alarma");


			}

		});
		tabs.setup();

		TabHost.TabSpec spec=tabs.newTabSpec("Calendary");
		spec.setContent(R.id.tab1);
		spec.setIndicator("",
				res.getDrawable(android.R.drawable.ic_menu_month));
		tabs.addTab(spec);

		spec=tabs.newTabSpec("Preferences");
		spec.setContent(R.id.tab2);
		spec.setIndicator("",
				res.getDrawable(android.R.drawable.ic_menu_manage));
		tabs.addTab(spec);

		tabs.setCurrentTab(0);
	}


	@Override
	public void onDestroy() {
		//send last info 
		tracker.dispatch();
		super.onDestroy();
		// Stop the tracker when it is no longer needed.
		tracker.stopSession();
	}

	@Override
	public void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MnuOpt1:
			PackageManager manager = getApplicationContext().getPackageManager();
			PackageInfo info = null;
			try {
				info = manager.getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
			}
			String version;
			if (info == null) {
				version = null;
			}
			else
				version = info.versionName;
			
			String myVersion = getResources().getString(R.string.app_version);
			//get the TextView from the custom_toast layout
			final Dialog dialog = new Dialog(this,R.style.NoTitleDialog);
			dialog.setContentView(R.layout.about_dialog);

			TextView text = (TextView) dialog.findViewById(R.id.about_dialog_version);
			text.setText(myVersion + " " + version);

			Button button = (Button) dialog.findViewById(R.id.about_dialog_rate);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW); 
						intent.setData(Uri.parse("market://details?id=com.jsolutionssp.patch")); 
						startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Intent intent = new Intent(Intent.ACTION_VIEW); 
						intent.setData(Uri.parse("http://market.android.com/details?id=com.jsolutionssp.patch")); 
						startActivity(intent);
					}
					dialog.dismiss();
					finish();
				}
			});
			Button button2 = (Button) dialog.findViewById(R.id.about_dialog_ok);
			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
			return true;
		case R.id.MnuOpt2:
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}