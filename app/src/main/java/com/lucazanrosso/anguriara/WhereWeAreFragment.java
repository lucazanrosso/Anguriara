package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WhereWeAreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.where_we_are));
        return inflater.inflate(R.layout.fragment_where_we_are, container, false);
    }
}
