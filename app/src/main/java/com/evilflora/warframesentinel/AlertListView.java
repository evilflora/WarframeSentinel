package com.evilflora.warframesentinel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

class AlertListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<AlertClass> _items;

    AlertListView(Context context, List<AlertClass> alertItems) {
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
        if (convertView == null) if (_inflater != null) {
            convertView = _inflater.inflate(R.layout.alert_element_view, parent, false);
        }
        if (convertView != null) {
            TextView alert_location = convertView.findViewById(R.id.cetus_job_type);
            TextView alert_type = convertView.findViewById(R.id.sortie_type);
            TextView alert_level = convertView.findViewById(R.id.alert_level);
            TextView alert_time = convertView.findViewById(R.id.alert_time);
            TextView alert_reward = convertView.findViewById(R.id.alert_reward);


            String location = _items.get(position).get_location();
            try {
                location = _context.getResources().getString(_context.getResources().getIdentifier(location, "string", _context.getPackageName()));
            }catch (Exception ex) {
                //
            }

            String mission_type = _items.get(position).get_mission_type();
            try {
                mission_type = _context.getResources().getString(_context.getResources().getIdentifier(mission_type, "string", _context.getPackageName()));
            }catch (Exception ex) {
                //
            }

            String faction= _items.get(position).get_faction();
            try {
                faction = _context.getResources().getString(_context.getResources().getIdentifier(faction, "string", _context.getPackageName()));
            }catch (Exception ex) {
                //
            }

            String reward_item_name = _items.get(position).get_reward_item_name();

            try {
                reward_item_name = _context.getResources().getString(_context.getResources().getIdentifier(_items.get(position).get_reward_item_name(), "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            alert_location.setText(location);
            alert_type.setText(String.format("%s - %s", mission_type, faction));
            alert_level.setText(String.format(_context.getString(R.string.level) + ": %s", _items.get(position).get_ennemy_level()));
            alert_time.setText(_items.get(position).get_time_before_expiry());
            alert_reward.setText(String.format("%d %s %s", _items.get(position).get_reward_credits(), _context.getString(R.string.credits), (reward_item_name == null ? "" : " + " + (_items.get(position).get_reward_item_quantity() > 1 ? _items.get(position).get_reward_item_quantity() + " " : "") + reward_item_name)));

            convertView.setOnLongClickListener(v -> {
                Toast.makeText(_context, "Will show all informations collected from worldState", Toast.LENGTH_SHORT).show();
                return true;
            });
        }

        return convertView;
    }
}