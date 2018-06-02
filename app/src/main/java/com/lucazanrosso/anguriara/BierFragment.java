package com.lucazanrosso.anguriara;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BierFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bier, container, false);

        String[] biers = getContext().getResources().getStringArray(R.array.biers);
        TypedArray biersImages = getContext().getResources().obtainTypedArray(R.array.biers_images);

        Bundle args = this.getArguments();
        int i = args.getInt("index");

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(biers[i]);
        MainActivity.previousFragment = R.id.home;

        ImageView bierImage = view.findViewById(R.id.bier_image);
        int bierImageId = biersImages.getResourceId(i, 0);
        bierImage.setImageResource(bierImageId);

        return view;
    }
}
