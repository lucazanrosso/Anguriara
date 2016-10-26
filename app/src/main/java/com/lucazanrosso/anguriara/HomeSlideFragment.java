package com.lucazanrosso.anguriara;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeSlideFragment extends Fragment {

    View view;
    int [] slideImage;
    String [] slideText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_home_slide, container, false);

        TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.slide_image);
        slideImage = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++)
            slideImage[i] = typedArray.getResourceId(i, 0);
        typedArray.recycle();
        slideText = getResources().getStringArray(R.array.slide_text);

        Bundle args = this.getArguments();
        int position = args.getInt("position");
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideTextView = (TextView) view.findViewById(R.id.slide_text);
        slideImageView.setImageResource(slideImage[position]);
        if (slideText[position] == null)
            slideImageView.setVisibility(View.GONE);
        else
            slideTextView.setText(slideText[position]);

        return view;
    }
}