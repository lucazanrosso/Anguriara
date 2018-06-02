package com.lucazanrosso.anguriara;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(getResources().getString(R.string.container));
        MainActivity.previousFragment = R.id.news;

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.breweries_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        String[] breweries = getContext().getResources().getStringArray(R.array.breweries);
        String[] breweriesRelated = getContext().getResources().getStringArray(R.array.breweries_related);
        String[] biers = getContext().getResources().getStringArray(R.array.biers);
        String[] biersDetails = getContext().getResources().getStringArray(R.array.biers_details);
        TypedArray biersImages = getContext().getResources().obtainTypedArray(R.array.biers_images);

        RecyclerView.Adapter mAdapter = new BreweriesAdapter(getActivity(), getContext(), breweries, breweriesRelated, biers, biersImages, biersDetails);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
