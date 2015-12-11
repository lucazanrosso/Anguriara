package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SponsorFragment extends Fragment {

    View view;

    private String[] sponsorTitles;
    private String[] sponsorTexts;
    private int[] sponsorLogos = {R.drawable.sponsor_logo,
            R.drawable.sponsor_logo,
            R.drawable.sponsor_logo};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.sponsor));

        view = inflater.inflate(R.layout.fragment_sponsor, container, false);

        this.sponsorTitles = view.getResources().getStringArray(R.array.sponsor_title);
        this.sponsorTexts = view.getResources().getStringArray(R.array.sponsor_sub_title);

        RecyclerView sponsorRecyclerView = (RecyclerView) view.findViewById(R.id.sponsor_recycler_view);
        RecyclerView.Adapter sponsorAdapter = new SponsorAdapter(sponsorTitles, sponsorTexts, sponsorLogos);
        sponsorRecyclerView.setAdapter(sponsorAdapter);
        RecyclerView.LayoutManager dayLayoutManager = new LinearLayoutManager(getContext());
        sponsorRecyclerView.setLayoutManager(dayLayoutManager);

        return view;
    }
}
