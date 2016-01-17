package com.lucazanrosso.anguriara;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private static final int TYPE_ITEM = 1;

    private String[] titles;
    private String[] menuString;
    private int[] menuIcons;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        public TextView mTitleTextView;
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v, int viewType) {
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.day_title);
            mTextView = (TextView) v.findViewById(R.id.day_text);
            mImageView = (ImageView) v.findViewById(R.id.day_image);
            mImageView.setColorFilter(Color.parseColor("#757575"));
            holderId = 1;
        }
    }

    public DayAdapter(String[] titles, String[] text, int [] icons) {
        this.titles = titles;
        this.menuString = text;
        this.menuIcons = icons;
    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_row, parent, false);
        ViewHolder vh = new ViewHolder(v, viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitleTextView.setText(titles[position]);
        holder.mTextView.setText(menuString[position]);
        holder.mImageView.setImageResource(menuIcons[position]);
    }

    @Override
    public int getItemCount() {
        return menuString.length;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
}