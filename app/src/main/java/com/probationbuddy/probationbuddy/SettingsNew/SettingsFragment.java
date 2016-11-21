package com.probationbuddy.probationbuddy.SettingsNew;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.probationbuddy.probationbuddy.R;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    int minute;
    int hour;
    String time;
    String minuteString;
    boolean am12;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);




//////
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        onSharedPreferenceChanged(sharedPrefs, getString(R.string.prefIntervalKey));
        //////


    }








/////

    @Override
    public void onResume() {
        super.onResume();
        //register the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);







    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }

        if (preference instanceof TimePreference) {
            TimePreference timePreference = (TimePreference) preference;

            //get shared prefs
            SharedPreferences prefs = getDefaultSharedPreferences(getContext());

            //set hour and minute of morning start time
            minute = prefs.getInt("prefStartTime", 123) % 60;
            hour = prefs.getInt("prefStartTime", 123);
            hour = hour - minute;
            hour = hour / 60;

            am12 = false;

            minuteString = String.valueOf(minute);
            if (minute < 10){
                minuteString = "0" + minute;
            }


            if (hour == 0){
                hour = 12;
                am12 = true;

            }

            time = (hour + ":" + minuteString + " am");

            if (hour > 12){
                hour = hour-12;
                time = (hour + ":" + minuteString + " pm");
            }
            if (hour == 12){
                time = (hour + ":" + minuteString + " pm");
                if (am12){
                    time = (hour + ":" + minuteString + " am");
                }
            }

            //fix 12pm with hourString check

            preference.setSummary(time);


        }else {
            preference.setSummary(sharedPreferences.getString(key, ""));

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }




//////








    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the related
            // Preference
            dialogFragment = TimePreferenceDialogFragmentCompat
                    .newInstance(preference.getKey());
        }

        // If it was one of our cutom Preferences, show its dialog
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(),
                    "android.support.v7.preference" +
                            ".PreferenceFragment.DIALOG");
        }
        // Could not be handled here. Try with the super method.
        else {
            super.onDisplayPreferenceDialog(preference);
        }
    }





}
