package com.evilflora.warframesentinel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class SortieRewardListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private String[] sortieReward;
    private double[] dropChance;

    SortieRewardListView(Activity activity, String[] rewards, double[] dropchance) {
        this.activity = activity;
        this.sortieReward = rewards;
        this.dropChance = dropchance;
    }

    @Override
    public int getCount() {
        return sortieReward.length;
    }

    @Override
    public Object getItem(int location) { return sortieReward[location]; }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) if (inflater != null) {
            convertView = inflater.inflate(R.layout.item, parent, false);
        }

        if (convertView != null) {
            TextView item_name = convertView.findViewById(R.id.item_name);
            TextView item_drop_chance = convertView.findViewById(R.id.item_drop_chance);

            item_name.setText(sortieReward[position]);
            item_drop_chance.setText(String.format("%s%%", dropChance[position]));
        }

        return convertView;
    }

}