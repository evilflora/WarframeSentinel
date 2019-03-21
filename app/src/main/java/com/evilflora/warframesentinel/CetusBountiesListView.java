package com.evilflora.warframesentinel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

class CetusBountiesListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<CetusJobClass> _items;

    CetusBountiesListView(Context context, List<CetusJobClass> items) {
        this._context = context;
        this._items = items;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Object getItem(int location) { return _items.get(location); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (_inflater == null) _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) if (_inflater != null) {
            convertView = _inflater.inflate(R.layout.cetus_element_job_view, parent, false);
        }
        if (convertView != null) {
            TextView cetus_job_type = convertView.findViewById(R.id.construction_status);
            TextView cetus_job_level = convertView.findViewById(R.id.cetus_job_level);
            TextView cetus_total_xp_reward = convertView.findViewById(R.id.cetus_total_xp_reward);
            LinearLayout cetus_total_reward = convertView.findViewById(R.id.cetus_rewards);

            String job_type = _items.get(position).get_job_type();
            try {
                job_type = _context.getResources().getString(_context.getResources().getIdentifier(job_type, "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            cetus_job_type.setText(job_type);
            cetus_job_level.setText(String.format(_context.getString(R.string.level) + ": %s", _items.get(position).get_ennemy_level()));
            cetus_total_xp_reward.setText(String.format(_context.getString(R.string.total) + ": %d", _items.get(position).get_total_xp_amounts()));

            for (int i = 0; i < _items.get(position).get_xp_amounts_size(); i++) {
                View view = LayoutInflater.from(_context).inflate(R.layout.cetus_element_reward_view, parent, false);
                TextView cetus_job_xp_amount = view.findViewById(R.id.cetus_job_xp_amount);
                cetus_job_xp_amount.setText(String.valueOf(_items.get(position).get_xp_amounts(i)));
                cetus_total_reward.addView(view);
            }
        }
        return convertView;
    }

}