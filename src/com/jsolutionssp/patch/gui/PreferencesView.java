package com.jsolutionssp.patch.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jsolutionssp.patch.ChangeTheme;
import com.jsolutionssp.patch.PatchActivity;
import com.jsolutionssp.patch.R;
import com.jsolutionssp.patch.adapter.CycleAlarmAdapter;
import com.jsolutionssp.patch.adapter.CycleAlarmSoundAdapter;
import com.jsolutionssp.patch.adapter.CycleAlarmVibrateAdapter;
import com.jsolutionssp.patch.adapter.DiaryAlarmAdapter;
import com.jsolutionssp.patch.adapter.DiaryAlarmSoundAdapter;
import com.jsolutionssp.patch.adapter.DiaryAlarmVibrateAdapter;
import com.jsolutionssp.patch.adapter.GalleryImageAdapter;
import com.jsolutionssp.patch.adapter.PrevAlarmDaysAdapter;
import com.jsolutionssp.patch.adapter.WeekDayAdapter;
import com.jsolutionssp.patch.service.SetAlarms;

public class PreferencesView extends LinearLayout {

	private Context context;

	private LinearLayout preferencesLayout;

	String[] textIni;

	private SharedPreferences settings;

	private Editor editor;

	public PreferencesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout firstLayout = (LinearLayout) layoutInflater.inflate(R.layout.preferences, this);
		ScrollView scroll = (ScrollView) firstLayout.getChildAt(0);
		preferencesLayout = (LinearLayout) scroll.getChildAt(0);
		this.context = context;
		settings = context.getSharedPreferences(PatchActivity.PREFS_NAME, 0);
		editor = settings.edit();
		textIni = getResources().getStringArray(R.array.preferences_categories);
		paint();
	}


	private void paint() {
		setSeparators();
		setWeekStartDay();
		setCalendarTheme();
		setDiaryAlarm();
		setDiaryAlarmHour();
		setDiaryAlarmSound();
		setDiaryAlarmVibrate();
		setCycleAlarm();
		setCycleAlarmPrevDays();
		setCycleAlarmHour();
		setCycleAlarmSound();
		setCycleAlarmVibrate();
	}


	private void setSeparators() {
		TextView textView1 = (TextView) preferencesLayout.findViewById(R.id.preferences_calendar_separator);
		textView1.setText(R.string.preferences_calendar_separator);
		textView1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.list_separator, 0, R.drawable.list_separator);
		TextView textView2 = (TextView) preferencesLayout.findViewById(R.id.preferences_alarms_separator);
		textView2.setText(R.string.preferences_alarms_separator);
		textView2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.list_separator, 0, R.drawable.list_separator);

	}


	private void setCycleAlarmVibrate() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_cycle_alarm_vibrate);
		if (settings.getInt("cycleAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[10]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int type = settings.getInt("cycleAlarmVibrate", 0);
			final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
			info.setText(alarms[type]);

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_select);
					TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
					text.setText(R.string.select_vibrate_dialog_title);

					ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
					CycleAlarmVibrateAdapter arrayAdapter = 
						new CycleAlarmVibrateAdapter(context, R.layout.item_initial_listview, alarms);
					lv.setAdapter(arrayAdapter);
					Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int type = settings.getInt("cycleAlarmVibrate", 0);
							myInfo.setText(alarms[type]);
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setCycleAlarmSound() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_cycle_alarm_sound);
		if (settings.getInt("cycleAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[9]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int type = settings.getInt("cycleAlarmSound", 0);
			final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
			info.setText(alarms[type]);

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_select);
					TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
					text.setText(R.string.select_sound_dialog_title);

					ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
					CycleAlarmSoundAdapter arrayAdapter = 
						new CycleAlarmSoundAdapter(context, R.layout.item_initial_listview, alarms);
					lv.setAdapter(arrayAdapter);
					Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int type = settings.getInt("cycleAlarmSound", 0);
							myInfo.setText(alarms[type]);
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setCycleAlarmHour() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_cycle_alarm_time);
		if (settings.getInt("cycleAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[8]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int hour = settings.getInt("cycleHourNotification", -1);
			int minute = settings.getInt("cycleMinuteNotification", -1);
			String hourString = Integer.toString(hour);
			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}
			String minuteString = Integer.toString(minute);
			if (minuteString.length() == 1) {
				minuteString = "0" + minuteString;
			}
			if (hour != -1 && minute != -1)
				info.setText(" " + hourString + ":" + minuteString + " ");

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context, R.style.CenteredDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_time_select);
					dialog.setTitle(R.string.select_alarm_hour_dialog_title);

					final TimePicker tp = (TimePicker) dialog.findViewById(R.id.time_select_picker);
					tp.setIs24HourView(true);
					Button button = (Button) dialog.findViewById(R.id.time_select_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							tp.getCurrentHour();
							int hour = tp.getCurrentHour();
							editor.putInt("cycleHourNotification", hour);
							int minute = tp.getCurrentMinute();
							editor.putInt("cycleMinuteNotification", minute);
							editor.commit();
							String hourString = Integer.toString(hour);
							if (hourString.length() == 1) {
								hourString = "0" + hourString;
							}
							String minuteString = Integer.toString(minute);
							if (minuteString.length() == 1) {
								minuteString = "0" + minuteString;
							}
							myInfo.setText(" " + hourString + ":" + minuteString + " ");
							dialog.dismiss();
							launchAlarm();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setCycleAlarmPrevDays() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_cycle_alarm_prevDays);
		if (settings.getInt("cycleAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[7]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int type = settings.getInt("prevAlarmDays", 0);
			final String[] alarms = getResources().getStringArray(R.array.prev_alarm_array);
			info.setText(alarms[type]);

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_select);
					TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
					text.setText(R.string.select_prevdays_dialog_title);

					ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
					PrevAlarmDaysAdapter arrayAdapter = 
						new PrevAlarmDaysAdapter(context, R.layout.item_initial_listview, alarms);
					lv.setAdapter(arrayAdapter);
					Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int type = settings.getInt("prevAlarmDays", 0);
							myInfo.setText(alarms[type]);
							dialog.dismiss();
							launchAlarm();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setCycleAlarm() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_cycle_alarm);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[6]);
		TextView info = (TextView) textLayout.getChildAt(1);
		int type = settings.getInt("cycleAlarm", 0);
		final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
		info.setText(alarms[type]);

		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
				LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
				final TextView myInfo = (TextView) lay1.getChildAt(1);
				dialog.setContentView(R.layout.dialog_select);
				TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
				text.setText(R.string.select_cyclealarm_dialog_title);

				ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
				CycleAlarmAdapter arrayAdapter = 
					new CycleAlarmAdapter(context, R.layout.item_initial_listview, alarms);
				lv.setAdapter(arrayAdapter);
				Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int type = settings.getInt("cycleAlarm", 0);
						paint();
						myInfo.setText(alarms[type]);
						dialog.dismiss();
						if (type == 1) {
							PatchActivity.tracker.trackEvent(
									"Alarms",  // Category
									"Cycle",  // Action
									"Enabled", // Label
									1);       // Value
							launchAlarm();
						}
						else
							PatchActivity.tracker.trackEvent(
									"Alarms",  // Category
									"Cycle",  // Action
									"Disabled", // Label
									1);       // Value
					}
				});
				dialog.show();
			}
		});
	}

	private void setDiaryAlarmVibrate() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_diary_alarm_vibrate);
		if (settings.getInt("diaryAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[5]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int type = settings.getInt("diaryAlarmVibrate", 0);
			final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
			info.setText(alarms[type]);

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_select);
					TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
					text.setText(R.string.select_vibrate_dialog_title);

					ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
					DiaryAlarmVibrateAdapter arrayAdapter = 
						new DiaryAlarmVibrateAdapter(context, R.layout.item_initial_listview, alarms);
					lv.setAdapter(arrayAdapter);
					Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int type = settings.getInt("diaryAlarmVibrate", 0);
							myInfo.setText(alarms[type]);
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setDiaryAlarmSound() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_diary_alarm_sound);
		if (settings.getInt("diaryAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[4]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int type = settings.getInt("diaryAlarmSound", 0);
			final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
			info.setText(alarms[type]);

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_select);
					TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
					text.setText(R.string.select_sound_dialog_title);

					ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
					DiaryAlarmSoundAdapter arrayAdapter = 
						new DiaryAlarmSoundAdapter(context, R.layout.item_initial_listview, alarms);
					lv.setAdapter(arrayAdapter);
					Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int type = settings.getInt("diaryAlarmSound", 0);
							myInfo.setText(alarms[type]);
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setDiaryAlarmHour() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_diary_alarm_time);
		if (settings.getInt("diaryAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[3]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int hour = settings.getInt("diaryHourNotification", -1);
			int minute = settings.getInt("diaryMinuteNotification", -1);
			String hourString = Integer.toString(hour);
			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}
			String minuteString = Integer.toString(minute);
			if (minuteString.length() == 1) {
				minuteString = "0" + minuteString;
			}
			if (hour != -1 && minute != -1)
				info.setText(" " + hourString + ":" + minuteString + " ");

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context, R.style.CenteredDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_time_select);
					dialog.setTitle(R.string.select_alarm_hour_dialog_title);

					final TimePicker tp = (TimePicker) dialog.findViewById(R.id.time_select_picker);
					tp.setIs24HourView(true);
					Button button = (Button) dialog.findViewById(R.id.time_select_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							tp.getCurrentHour();
							int hour = tp.getCurrentHour();
							editor.putInt("diaryHourNotification", hour);
							int minute = tp.getCurrentMinute();
							editor.putInt("diaryMinuteNotification", minute);
							editor.commit();
							String hourString = Integer.toString(hour);
							if (hourString.length() == 1) {
								hourString = "0" + hourString;
							}
							String minuteString = Integer.toString(minute);
							if (minuteString.length() == 1) {
								minuteString = "0" + minuteString;
							}
							myInfo.setText(" " + hourString + ":" + minuteString + " ");
							dialog.dismiss();
							launchAlarm();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setDiaryAlarm() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_diary_alarm);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[2]);
		TextView info = (TextView) textLayout.getChildAt(1);
		int type = settings.getInt("diaryAlarm", 0);
		final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
		info.setText(alarms[type]);

		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
				LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
				final TextView myInfo = (TextView) lay1.getChildAt(1);
				dialog.setContentView(R.layout.dialog_select);
				TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
				text.setText(R.string.select_diaryalarm_dialog_title);

				ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
				DiaryAlarmAdapter arrayAdapter = 
					new DiaryAlarmAdapter(context, R.layout.item_initial_listview, alarms);
				lv.setAdapter(arrayAdapter);
				Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int type = settings.getInt("diaryAlarm", 0);
						paint();
						myInfo.setText(alarms[type]);
						dialog.dismiss();
						if (type == 1) {
							PatchActivity.tracker.trackEvent(
									"Alarms",  // Category
									"Diary",  // Action
									"Enabled", // Label
									1);       // Value
							launchAlarm();
						}
						else
							PatchActivity.tracker.trackEvent(
									"Alarms",  // Category
									"Diary",  // Action
									"Disabled", // Label
									1);       // Value
					}
				});
				dialog.show();
			}
		});
	}

	private void setCalendarTheme() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_calendar_theme);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[1]);
		final TextView info = (TextView) textLayout.getChildAt(1);
		final String[] themeNames = getResources().getStringArray(R.array.theme_names);
		String theme = settings.getString("actualTheme", themeNames[0]);
		info.setText(theme);
		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context, R.style.CenteredDialog);
				dialog.setContentView(R.layout.dialog_select_theme);
				dialog.setTitle(R.string.select_theme_dialog_title);
				dialog.getWindow().setLayout(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				Gallery gallery = (Gallery) dialog.findViewById(R.id.gallery);
				GalleryImageAdapter galAdapter = new GalleryImageAdapter(context, dialog);
				gallery.setAdapter(galAdapter);
				gallery.setSelection(galAdapter.getCount()/2, true);
				gallery.setOnItemClickListener(new OnItemClickListener() {

					@SuppressWarnings("rawtypes")
					@Override
					public void onItemClick(AdapterView parent, View v, int position, long id) {
						ChangeTheme.changeTheme(position);
						editor.putString("actualTheme", themeNames[position]);
						editor.commit();
						info.setText(themeNames[position]);
						CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
						cal.fillGrid();
						dialog.dismiss();
					}

				});
				dialog.show();
			}
		});

	}

	private void setWeekStartDay() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_week_day);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[0]);
		TextView info = (TextView) textLayout.getChildAt(1);
		boolean firstDay = settings.getBoolean("firstDayofWeek", true); //True = Monday, false,Sunday
		final String[] options = getResources().getStringArray(R.array.start_week_day);
		if (firstDay)
			info.setText(options[0]);
		else
			info.setText(options[1]);

		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
				LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
				final TextView myInfo = (TextView) lay1.getChildAt(1);
				dialog.setContentView(R.layout.dialog_select);
				TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
				text.setText(R.string.select_startday_dialog_title);

				ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
				WeekDayAdapter arrayAdapter = 
					new WeekDayAdapter(context, R.layout.item_initial_listview, options);
				lv.setAdapter(arrayAdapter);
				Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
						cal.fillGrid();
						if (settings.getBoolean("firstDayofWeek", true))
							myInfo.setText(options[0]);
						else
							myInfo.setText(options[1]);
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}
	private void launchAlarm() {
		Intent i = new Intent(context, SetAlarms.class);
		i.setAction("com.jsolutionssp.patch.updateAlarm");
		context.sendBroadcast(i);
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
