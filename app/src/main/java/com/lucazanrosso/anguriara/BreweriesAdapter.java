package com.lucazanrosso.anguriara;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class BreweriesAdapter extends RecyclerView.Adapter<BreweriesAdapter.ViewHolder>{
    private Context context;
    private FragmentActivity mActivity;
    private String[] breweries;
    private String[] breweriesRelated;
    private String[] biers;
    private String[] biersDetails;
    private TypedArray biersImages;
    private TypedArray biersImagesDetails;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private int cardWidth;
    private int biersLayoutPadding;
    private float scale;
    private int letfRightPadding;
    private int topPadding;
    private int bottomPadding;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View breweryLayout;
        ViewHolder(View v) {
            super(v);
            breweryLayout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    BreweriesAdapter(FragmentActivity mActivity, Context context, String[] breweries, String[] breweriesRelated, String[] biers,  String[] biersDetails, TypedArray biersImages, TypedArray biersImagesDetails) {
        this.mActivity = mActivity;
        this.breweries = breweries;
        this.breweriesRelated = breweriesRelated;
        this.biers = biers;
        this.biersDetails = biersDetails;
        this.biersImages = biersImages;
        this.biersImagesDetails = biersImagesDetails;

        Resources r = context.getResources();
        cardWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                150,
                r.getDisplayMetrics()
        );

        biersLayoutPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                r.getDisplayMetrics()
        );

        scale = context.getResources().getDisplayMetrics().density; //altro metodo per dp?
        letfRightPadding = (int) (16*scale + 0.5f);
        topPadding = (int) (8*scale + 0.5f);
        bottomPadding = (int) (14*scale + 0.5f);
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
            biersLayout.removeAllViews();

            biersLayout.setPadding(biersLayoutPadding, biersLayoutPadding, biersLayoutPadding,0);

            for (int i = 0; i < breweriesRelated.length; i++) {

                if (breweries[position - 1].equals(breweriesRelated[i])) {

                    View bierCard = LayoutInflater.from(context)
                            .inflate(R.layout.card_next_evening, null);
                    bierCard.setLayoutParams(new ViewGroup.LayoutParams(cardWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

                    TextView bierTitle = bierCard.findViewById(R.id.next_evening_title);
                    TextView bierText = bierCard.findViewById(R.id.next_evening_text);
                    ImageView bierImage = bierCard.findViewById(R.id.next_evening_image);
                    Button detailsButton = bierCard.findViewById(R.id.details_button);
                    bierTitle.setText(biers[i]);
                    bierText.setText(biersDetails[i]);
                    int bierImageId = biersImages.getResourceId(i, 0);
                    bierImage.setImageResource(bierImageId);
                    int bierImageDetailsId = biersImagesDetails.getResourceId(i, 0);
                    if (!(bierImageDetailsId == 0)) {
                        final Bundle dayArgs = new Bundle();
                        dayArgs.putInt("index", i);
                        detailsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BierFragment bierFragment = new BierFragment();
                                bierFragment.setArguments(dayArgs);
                                mActivity.getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                                        .replace(R.id.frame_container, bierFragment)
                                        .addToBackStack("secondary").commit();
                            }
                        });
                    } else {

                        bierText.setPadding(letfRightPadding, topPadding, letfRightPadding, bottomPadding);
                        ((ViewGroup) detailsButton.getParent()).removeView(detailsButton);
                    }

                    biersLayout.addView(bierCard);
                }
            }
        }
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
