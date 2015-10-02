package com.luca.anguriara;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Luca on 19/09/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_DIVIDER = 2;

    private String[] mDataset;
    private int[] icons;
    private int image;
    private int specialType = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        int holderId;

        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mHeaderImageView;
        public View mView;

        public ViewHolder(View v, int viewType) {
            super(v);

            if (viewType == TYPE_HEADER) {
                mHeaderImageView = (ImageView) v.findViewById(R.id.drawer_header_image);
                holderId = 0;
            }
            if (viewType == TYPE_DIVIDER) {
                mView = v.findViewById(R.id.drawer_divider);
                holderId = 2;
            }
            if (viewType == TYPE_ITEM) {
                mTextView = (TextView) v.findViewById(R.id.drawer_row_text);
                mImageView = (ImageView) v.findViewById(R.id.drawer_row_image);
                //mImageView.setColorFilter(R.color.secondary_text);
                mImageView.setColorFilter(Color.parseColor("#757575"));
                holderId = 1;
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, int [] icons, int image) {
        this.mDataset = myDataset;
        this.icons = icons;
        this.image = image;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_header, parent, false);
            ViewHolder vh = new ViewHolder(v, viewType);
            return vh;
        }
        if (viewType == TYPE_DIVIDER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_divider, parent, false);
            ViewHolder vh = new ViewHolder(v, viewType);
            return vh;
        }
        if (viewType == TYPE_ITEM){
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_row, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v, viewType);
            return vh;
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder.holderId == 0) {
            holder.mHeaderImageView.setImageResource(image);
            specialType++;
        }
        if (holder.holderId == 1) {
            holder.mTextView.setText(mDataset[position - specialType]);
            holder.mImageView.setImageResource(icons[position - specialType]);
        }
        if (holder.holderId == 2) {
            specialType++;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position == 4) {
            return TYPE_DIVIDER;
        }
        return TYPE_ITEM;
    }
}
