package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

public class SettingsFragment extends Fragment {

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.settings));

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        SwitchCompat eveningsSwitch = (SwitchCompat) view.findViewById(R.id.evenings_switch);
        eveningsSwitch.setChecked(MainActivity.sharedPreferences.getBoolean("eveningsAlarmIsSet", false));
        eveningsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.setEveningsAlarm(getContext(), MainActivity.calendar, isChecked, false);
            }
        });
        SwitchCompat firebaseSwitch = (SwitchCompat) view.findViewById(R.id.firebase_switch);
        firebaseSwitch.setChecked(MainActivity.sharedPreferences.getBoolean("firebaseAlarmIsSet", false));
        firebaseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.setFirebaseAlarm(getContext(), isChecked, false);
            }
        });

        return view;
    }
}