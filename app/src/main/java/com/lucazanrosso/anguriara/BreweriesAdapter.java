package com.lucazanrosso.anguriara;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class BreweriesAdapter extends RecyclerView.Adapter<BreweriesAdapter.ViewHolder>{
    private Context context;
    private String[] breweries;
    private String[] breweriesRelated;
    private String[] biers;
    private TypedArray biersImages;

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
    BreweriesAdapter(Context context, String[] breweries, String[] breweriesRelated, String[] biers, TypedArray biersImages) {
//        this.context = context;
        this.breweries = breweries;
        this.breweriesRelated = breweriesRelated;
        this.biers = biers;
        this.biersImages = biersImages;
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

        context = parent.getContext();
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

            Resources r = context.getResources();
            int cardWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    150,
                    r.getDisplayMetrics()
            );

            int biersLayoutPadding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    r.getDisplayMetrics()
            );

            biersLayout.setPadding(biersLayoutPadding, biersLayoutPadding, biersLayoutPadding,0);

            for (int i = 0; i < breweriesRelated.length; i++) {

                if (breweries[position - 1].equals(breweriesRelated[i])) {

                    View bierCard = LayoutInflater.from(context)
                            .inflate(R.layout.next_evening_card, null);
                    bierCard.setLayoutParams(new ViewGroup.LayoutParams(cardWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

                    TextView nextEveningsTitle = bierCard.findViewById(R.id.next_evening_title);
                    TextView nextEveningsText = bierCard.findViewById(R.id.next_evening_text);
                    ImageView bierImage = bierCard.findViewById(R.id.next_evening_image);
                    nextEveningsTitle.setText(biers[i]);
                    nextEveningsText.setText("ciao2");
                    int bierImageId = biersImages.getResourceId(i, 0);
                    bierImage.setImageResource(bierImageId);


                    biersLayout.addView(bierCard);

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
