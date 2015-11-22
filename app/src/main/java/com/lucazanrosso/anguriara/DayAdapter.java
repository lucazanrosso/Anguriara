package com.lucazanrosso.anguriara;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_DIVIDER = 2;

    private String[] titles;
    private String[] menuString;
    private int[] menuIcons;
    private int specialType = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        public TextView mTitleTextView;
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v, int viewType) {
            super(v);
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

    public DayAdapter(String[] titles, String[] text, int [] icons) {
        this.titles = titles;
        this.menuString = text;
        this.menuIcons = icons;
    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.holderId == 1) {
            holder.mTitleTextView.setText(titles[position - specialType]);
            holder.mTextView.setText(menuString[position - specialType]);
            holder.mImageView.setImageResource(menuIcons[position - specialType]);
        }
        if (holder.holderId == 2) {
            specialType++;
        }
    }

    @Override
    public int getItemCount() {
        return menuString.length * 2 - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position % 2) == 1) {
            return TYPE_DIVIDER;
        }
        return TYPE_ITEM;
    }
}