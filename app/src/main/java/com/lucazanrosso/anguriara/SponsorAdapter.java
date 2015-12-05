package com.lucazanrosso.anguriara;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.ViewHolder> {

    private String[] titles;
    private String[] texts;
    private int[] logos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        public TextView mTitleTextView;
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.sponsor_title);
            mTextView = (TextView) v.findViewById(R.id.sponsor_text);
            mImageView = (ImageView) v.findViewById(R.id.sponsor_logo);
            holderId = 1;
        }
    }

    public SponsorAdapter(String[] titles, String[] texts, int [] logos) {
        this.titles = titles;
        this.texts = texts;
        this.logos = logos;
    }

    @Override
    public SponsorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sponsor_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitleTextView.setText(titles[position]);
        holder.mTextView.setText(texts[position]);
        holder.mImageView.setImageResource(logos[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}