package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PravolleyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(getResources().getString(R.string.pravolley));
        MainActivity.previousFragment = R.id.pravolley;

        View view = inflater.inflate(R.layout.fragment_pravolley, container, false);
        TextView inscriptionsText = (TextView) view.findViewById(R.id.inscriptions_text);
        inscriptionsText.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
