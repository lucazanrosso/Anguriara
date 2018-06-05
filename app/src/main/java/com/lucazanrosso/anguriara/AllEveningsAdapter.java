package com.lucazanrosso.anguriara;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

class AllEveningsAdapter extends RecyclerView.Adapter<AllEveningsAdapter.ViewHolder>{
    private Context context;
    private FragmentActivity mActivity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View card;
        ViewHolder(View v) {
            super(v);
            card = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    AllEveningsAdapter(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AllEveningsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_all_evenings, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        View card = holder.card;
        TextView nextEveningsTitle = card.findViewById(R.id.next_evening_title);
        TextView nextEveningsText = card.findViewById(R.id.next_evening_text);
        Button detailsButton = card.findViewById(R.id.details_button);
        nextEveningsTitle.setText(CalendarFragment.setDateTitle(MainActivity.days.get(position)));
        nextEveningsText.setText(CalendarFragment.setDateText(MainActivity.days.get(position), context));
        final Bundle dayArgs = new Bundle();
        dayArgs.putSerializable("date", MainActivity.days.get(position));
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
                dayScreenSlidePagerFragment.setArguments(dayArgs);
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                        .replace(R.id.frame_container, dayScreenSlidePagerFragment)
                        .addToBackStack("secondary")
                        .commit();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return MainActivity.days.size();
    }
}
