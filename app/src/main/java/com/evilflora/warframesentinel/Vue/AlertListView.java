package com.evilflora.warframesentinel.Vue;

/*
 * Created by guill on 09/06/2019 for WarframeSentinel
 */

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
            TextView alertLocation = convertView.findViewById(R.id.cetus_job_type);
            TextView alertType = convertView.findViewById(R.id.sortie_type);
            TextView alertLevel = convertView.findViewById(R.id.alert_level);
            TextView alertTime = convertView.findViewById(R.id.alert_time);
            TextView alertReward = convertView.findViewById(R.id.alert_reward);

            // todo
            alertLocation.setText(_items.get(position).getLocation());
            alertLevel.setText(_items.get(position).getEnnemyLevel());
            alertType.setText(String.format("%s - %s", _items.get(position).getMissionType(), _items.get(position).getFaction()));
            alertTime.setText(_items.get(position).getTimeBeforeExpiry());
            alertReward.setText(String.format("%d %s %s", _items.get(position).getRewardCredits(), _context.getString(R.string.credits), (_items.get(position).getRewardItemName() == null ? "" : " + " + (_items.get(position).getRewardItemQuantity() > 1 ? _items.get(position).getRewardItemQuantity() + " " : "") + _items.get(position).getRewardItemQuantity())));
        }

        return convertView;
    }
}