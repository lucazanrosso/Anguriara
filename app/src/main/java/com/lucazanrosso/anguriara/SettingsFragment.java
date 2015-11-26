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
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        MainActivity.toolbar.setTitle(view.getResources().getString(R.string.settings));

        SwitchCompat notificationSwitch = (SwitchCompat) view.findViewById(R.id.notifications_switch);
        notificationSwitch.setChecked(MainActivity.sharedPreferences.getBoolean("alarmIsSet", false));
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.setAlarm(getContext(), MainActivity.calendar, isChecked, false);
            }
        });

        return view;
    }
}