package com.lucazanrosso.anguriara;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedHashMap;

class BreweriesAdapter extends RecyclerView.Adapter<BreweriesAdapter.ViewHolder>{
    private Context context;
    private String[] breweries;
    private String[] breweriesRelated;
    private TypedArray images;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View breweryLayout;
        ViewHolder(View v) {
            super(v);
            breweryLayout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    BreweriesAdapter(Context context, String[] breweries, String[] breweriesRelated, TypedArray images) {
        this.context = context;
        this.breweries = breweries;
        this.breweriesRelated = breweriesRelated;
        this.images = images;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BreweriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v;
        if (viewType == TYPE_HEADER)
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_conteiner, parent, false);
        else
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_brewery, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (position != TYPE_HEADER) {
            View breweryLayout = holder.breweryLayout;

            TextView breweryName = breweryLayout.findViewById(R.id.brewery_name);
            breweryName.setText(breweries[position - 1]);

            LinearLayout biersLayout = breweryLayout.findViewById(R.id.biers_layout);

            for (String breweryRelated : breweriesRelated) {

                if (breweries[position - 1].equals(breweryRelated)) {

                    View nextEveningCard = LayoutInflater.from(context)
                            .inflate(R.layout.next_evening_card, null);



                    TextView nextEveningsTitle = nextEveningCard.findViewById(R.id.next_evening_title);
                    TextView nextEveningsText = nextEveningCard.findViewById(R.id.next_evening_text);
                    nextEveningsTitle.setText("ciao");
                    nextEveningsText.setText("ciao2");

                    CardView cardView = new CardView(context);

                    biersLayout.addView(nextEveningCard);

                    System.out.println("hey");
                }

            }
        }

//        TextView nextEveningsTitle = breweryLayout.findViewById(R.id.next_evening_title);
//        TextView nextEveningsText = breweryLayout.findViewById(R.id.next_evening_text);
//        Button detailsButton = breweryLayout.findViewById(R.id.details_button);
//        nextEveningsTitle.setText(CalendarFragment.setDateTitle(MainActivity.days.get(position)));
//        nextEveningsText.setText(CalendarFragment.setDateText(MainActivity.days.get(position), context));
//        final Bundle dayArgs = new Bundle();
//        dayArgs.putSerializable("date", MainActivity.days.get(position));
//        detailsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
//                dayScreenSlidePagerFragment.setArguments(dayArgs);
//                mActivity.getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
//                        .replace(R.id.frame_container, dayScreenSlidePagerFragment)
//                        .addToBackStack("secondary")
//                        .commit();
//            }
//        });
//        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return breweries.length + 1;
    }
}
