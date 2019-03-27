package com.evilflora.warframesentinel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class SyndicateListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SyndicateClass> items;

    SyndicateListView(Activity activity, List<SyndicateClass> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) { return items.get(location); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) if (inflater != null) {
            convertView = inflater.inflate(R.layout.syndicate_element_view, parent, false);
        }

        if(convertView != null) {
            TextView syndicate_location = convertView.findViewById(R.id.syndicate_location);
            TextView syndicate_reward   = convertView.findViewById(R.id.syndicate_reward);
            TextView syndicate_type     = convertView.findViewById(R.id.syndicate_type);
            TextView syndicate_level    = convertView.findViewById(R.id.syndicate_level);

            syndicate_location.setText(items.get(position).get_node(position % items.get(position).get_nodes_size()));
            syndicate_type.setText(items.get(position).get_type());
            syndicate_reward.setText(items.get(position).get_time_before_expiry());
            syndicate_level.setText(items.get(position).get_level(position % items.get(position).get_nodes_size()));
        }

        return convertView;
    }

}