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
    private String[] biersDetails;
    private TypedArray biersImages;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private int cardWidth;
    private int biersLayoutPadding;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View breweryLayout;
        ViewHolder(View v) {
            super(v);
            breweryLayout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    BreweriesAdapter(Context context, String[] breweries, String[] breweriesRelated, String[] biers, TypedArray biersImages, String[] biersDetails) {
//        this.context = context;
        this.breweries = breweries;
        this.breweriesRelated = breweriesRelated;
        this.biers = biers;
        this.biersDetails = biersDetails;
        this.biersImages = biersImages;

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

            biersLayout.setPadding(biersLayoutPadding, biersLayoutPadding, biersLayoutPadding,0);

            for (int i = 0; i < breweriesRelated.length; i++) {

                if (breweries[position - 1].equals(breweriesRelated[i])) {

                    View bierCard = LayoutInflater.from(context)
                            .inflate(R.layout.next_evening_card, null);
                    bierCard.setLayoutParams(new ViewGroup.LayoutParams(cardWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

                    TextView bierTitle = bierCard.findViewById(R.id.next_evening_title);
                    TextView bierText = bierCard.findViewById(R.id.next_evening_text);
                    ImageView bierImage = bierCard.findViewById(R.id.next_evening_image);
                    bierTitle.setText(biers[i]);
                    bierText.setText(biersDetails[i]);
                    int bierImageId = biersImages.getResourceId(i, 0);
                    bierImage.setImageResource(bierImageId);

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
