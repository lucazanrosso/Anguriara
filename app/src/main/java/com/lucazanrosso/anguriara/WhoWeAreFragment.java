package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WhoWeAreFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        MainActivity.toolbar.setTitle(getResources().getString(R.string.who_we_are));
        MainActivity.toolbar.setLogo(null);

        return inflater.inflate(R.layout.fragment_who_we_are, container, false);
    }
}