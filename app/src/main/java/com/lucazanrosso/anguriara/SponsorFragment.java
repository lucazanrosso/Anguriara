package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class SponsorFragment extends Fragment {

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sponsor, container, false);

        LinearLayout sponsorLayout = (LinearLayout) view.findViewById(R.id.sponsor_layout);

        for (LinkedHashMap.Entry<String, LinkedHashMap<String, String>> entry : MainActivity.sponsor.entrySet()) {
            sponsorLayout.addView(addCard(entry.getValue()));
        }
        return view;
    }

    public View addCard(LinkedHashMap<String, String> sponsor) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View sponsorCard = inflater.inflate(R.layout.sponsor_card, null, false);

        TextView sponsorTitle = (TextView) sponsorCard.findViewById(R.id.sponsor_title);
        TextView sponsorSubTitle = (TextView) sponsorCard.findViewById(R.id.sponsor_sub_title);

        sponsorTitle.setText(sponsor.get("sponsor_title"));
        sponsorSubTitle.setText(sponsor.get("sponsor__sub_title"));

        Log.d("dai", "dai");

        return sponsorCard;
    }
}
