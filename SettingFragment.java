package com.bignerdranch.android.weather_forecast;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,Preference.OnPreferenceChangeListener{
    SwitchPreference notification;
    Preference temunit;
    ListPreference location;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pre_setting);
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
      final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        notification =(SwitchPreference) findPreference("pre_notification");
         temunit=findPreference("pre_temunit");
         location=(ListPreference) findPreference("pre_location");
        notification.setDefaultValue(sharedPreferences.getBoolean("pre_notification",false));
        temunit.setSummary(sharedPreferences.getString("pre_temunit","Metric"));
        location.setSummary(sharedPreferences.getString("pre_location","长沙"));
        location.setValue(sharedPreferences.getString("pre_location","长沙"));
        notification.setOnPreferenceClickListener(this);
        temunit.setOnPreferenceClickListener(this);
        location.setOnPreferenceChangeListener(this);
    }
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference ==temunit){
            if(preference.getSummary().equals("Metric")) {
                temunit.setSummary("Imperial");
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("pre_temunit", "Imperial");
                editor.apply();
            }else {
                temunit.setSummary("Metric");
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("pre_temunit", "Metric");
                editor.apply();
            }
        }
        return true;
    }

@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference==location){
            location.setSummary(newValue.toString());
        }
        return true;
    }
}
