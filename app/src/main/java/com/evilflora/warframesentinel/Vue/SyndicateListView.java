package com.evilflora.warframesentinel.Vue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.SyndicateClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class SyndicateListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SyndicateClass> items;

    public SyndicateListView(Activity activity, List<SyndicateClass> items) {
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
        if (convertView == null && inflater != null) convertView = inflater.inflate(R.layout.syndicate_element_view, parent, false);

        if(convertView != null) {
            TextView syndicateLocation = convertView.findViewById(R.id.syndicate_location);
            TextView syndicateType     = convertView.findViewById(R.id.syndicate_type);
            TextView syndicateReward   = convertView.findViewById(R.id.syndicate_reward);
            TextView syndicateLevel    = convertView.findViewById(R.id.syndicate_level);

            syndicateLocation.setText(items.get(position).getNode(position % items.get(position).getNodeSize()));
            syndicateType.setText(items.get(position).getType());
            syndicateReward.setText(items.get(position).getTimeBeforeEnd());
            syndicateLevel.setText(items.get(position).getLevel(position % items.get(position).getNodeSize()));
        }

        return convertView;
    }

}