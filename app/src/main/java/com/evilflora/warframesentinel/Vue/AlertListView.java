package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.AlertClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class AlertListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<AlertClass> _items;

    public AlertListView(Context context, List<AlertClass> alertItems) {
        this._context = context;
        this._items = alertItems;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Object getItem(int location) {
        return _items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (_inflater == null) _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null && _inflater != null) convertView = _inflater.inflate(R.layout.alert_element_view, parent, false);

        if (convertView != null) {
            TextView alertLocation = convertView.findViewById(R.id.alert_location);
            TextView alertType = convertView.findViewById(R.id.sortie_type);
            TextView alertLevel = convertView.findViewById(R.id.alert_level);
            TextView alertTime = convertView.findViewById(R.id.alert_time);
            TextView alertRewardCredits = convertView.findViewById(R.id.alert_reward_credits);
            TextView alertRewardItems = convertView.findViewById(R.id.alert_reward_item);

            alertLocation.setText(_items.get(position).getLocation());
            alertLevel.setText(_items.get(position).getEnemyLevel());
            alertType.setText(_items.get(position).getType());
            alertTime.setText(_items.get(position).getTimeBeforeExpiry());
            alertRewardCredits.setText(_items.get(position).getRewardCredits());
            alertRewardItems.setText(_items.get(position).getRewards());
        }

        return convertView;
    }
}