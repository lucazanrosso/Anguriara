package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HomeSlideFragment extends Fragment {

    View view;
    int [] slideId = {R.drawable.slide0, R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_home_slide, container, false);

        Bundle args = this.getArguments();
        int position = args.getInt("position");
        ImageView slideImage = (ImageView) view.findViewById(R.id.slide_image);
        slideImage.setImageResource(slideId[position]);

        return view;
    }
}
