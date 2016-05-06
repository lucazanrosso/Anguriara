package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PravolleyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.pravolley));

        View view = inflater.inflate(R.layout.fragment_pravolley, container, false);
        TextView inscriptionsText = (TextView) view.findViewById(R.id.inscriptions_text);
        inscriptionsText.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
