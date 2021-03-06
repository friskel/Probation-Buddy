package com.probationbuddy.probationbuddy.morningalarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;


public class MorningServiceStarter extends IntentService {
    int hour;
    int minute;


    public MorningServiceStarter() {
        super("MorningServiceStarter");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Intent for the MorningReceiver
        Intent intentMorningAlarmStarter = new Intent(getApplicationContext(), MorningReceiver.class);

        // PendingIntent to be triggered when this alarm goes off every morning.  Fires the intent to start MorningReceiver
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MorningReceiver.REQUEST_CODE,
                intentMorningAlarmStarter, PendingIntent.FLAG_UPDATE_CURRENT);

        //get shared prefs
        SharedPreferences prefs = getDefaultSharedPreferences(this);

        //set hour and minute of morning start time
        minute = prefs.getInt("prefStartTime", 123) % 60;
        hour = prefs.getInt("prefStartTime", 123);
        hour = hour - minute;
        hour = hour / 60;

        ///time check


        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(System.currentTimeMillis());

        long nowHours = rightNow.get(Calendar.HOUR_OF_DAY);
        long nowHoursMillis = nowHours * 3600000;

        long nowMinutes = rightNow.get(Calendar.MINUTE);
        long nowMinutesMillis = nowMinutes * 60000;

        long nowSeconds = rightNow.get(Calendar.SECOND);
        long nowSecondsMillis = nowSeconds * 1000;

        long nowMillis = rightNow.get(Calendar.MILLISECOND);

        long nowFullTime = nowHoursMillis + nowMinutesMillis + nowSecondsMillis + nowMillis;


        long fullTimeNumber = prefs.getInt("prefStartTime", 123);

        long fullTimeNumberMillis = fullTimeNumber * 60000;




        /// done time check

        //set the time on a calendar object
        Calendar dailyCalendar = Calendar.getInstance();
        dailyCalendar.setTimeInMillis(System.currentTimeMillis());

        if (nowFullTime > fullTimeNumberMillis) {
            dailyCalendar.add(Calendar.DATE, 1);
        }

        dailyCalendar.set(Calendar.HOUR_OF_DAY, hour);
        dailyCalendar.set(Calendar.MINUTE, minute);
        dailyCalendar.set(Calendar.SECOND, 0);

        //make the alarm
        AlarmManager morningAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //(type, starttime, interval, pintent)
        morningAlarm.setRepeating(AlarmManager.RTC_WAKEUP, dailyCalendar.getTimeInMillis(),
                24*60*60*1000, pIntent);

        //for the boot receiver, clears the wake lock
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}































