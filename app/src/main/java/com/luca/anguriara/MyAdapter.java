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
    private static boolean isDrawer = false;

    private String[] titles;
    private String[] menuString;
    private int[] menuIcons;
    private int headerImage;
    private int specialType = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        int holderId;

        public TextView mTitleTextView;
        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mHeaderImageView;

        public ViewHolder(View v, int viewType) {
            super(v);
            if (isDrawer) {
                if (viewType == TYPE_HEADER) {
                    mHeaderImageView = (ImageView) v.findViewById(R.id.drawer_header_image);
                    holderId = 0;
                }
                if (viewType == TYPE_ITEM) {
                    mTextView = (TextView) v.findViewById(R.id.drawer_row_text);
                    mImageView = (ImageView) v.findViewById(R.id.drawer_row_image);
                    //mImageView.setColorFilter(R.color.secondary_text);
                    mImageView.setColorFilter(Color.parseColor("#757575"));
                    holderId = 1;
                }
                if (viewType == TYPE_DIVIDER) {
                    holderId = 2;
                }
            }
            else {
                if (viewType == TYPE_ITEM) {
                    mTitleTextView = (TextView) v.findViewById(R.id.day_title);
                    mTextView = (TextView) v.findViewById(R.id.day_text);
                    mImageView = (ImageView) v.findViewById(R.id.day_image);
                    mImageView.setColorFilter(Color.parseColor("#757575"));
                    holderId = 1;
                }
                if (viewType == TYPE_DIVIDER) {
                    holderId = 2;
                }
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] menuString, int [] menuIcons, int headerImage) {
        this.menuString = menuString;
        this.menuIcons = menuIcons;
        this.headerImage = headerImage;
        this.isDrawer = true;
    }

    public MyAdapter(String[] titles, String[] text, int [] icons) {
        this.titles = titles;
        this.menuString = text;
        this.menuIcons = icons;
        this.isDrawer = false;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isDrawer) {
            if (viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.drawer_header, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);
                return vh;
            }
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.drawer_row, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);
                return vh;
            }
            if (viewType == TYPE_DIVIDER) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.drawer_divider, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);
                return vh;
            }
        }
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.day_row, parent, false);
            ViewHolder vh = new ViewHolder(v, viewType);
            return vh;
        }
        if (viewType == TYPE_DIVIDER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.day_divider, parent, false);
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
            holder.mHeaderImageView.setImageResource(headerImage);
            specialType++;
        }
        if (holder.holderId == 1) {
            if (! isDrawer) {
                holder.mTitleTextView.setText(titles[position - specialType]);
            }
            holder.mTextView.setText(menuString[position - specialType]);
            holder.mImageView.setImageResource(menuIcons[position - specialType]);
        }
        if (holder.holderId == 2) {
            specialType++;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (isDrawer) {
            return menuString.length + 2;
        }
        return menuString.length * 2 - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isDrawer) {
            if (position == 0) {
                return TYPE_HEADER;
            }
            if (position == 4) {
                return TYPE_DIVIDER;
            }
        } else if ((position % 2) == 1) {
            return TYPE_DIVIDER;
        }
        return TYPE_ITEM;
    }
}
