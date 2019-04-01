package com.evilflora.warframesentinel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class WorldCyclesView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private BountiesClass _items;

    WorldCyclesView(Context context, BountiesClass items) {
        this._context = context;
        this._items = items;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (_inflater == null) _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) if (_inflater != null) {
            convertView = _inflater.inflate(R.layout.world_cycle_content, parent, false);
        }
        if (convertView != null) {

            TextView textViewCetusTimer = convertView.findViewById(R.id.textView_Cetus_Timer);
            TextView textViewOrbVallisTimer= convertView.findViewById(R.id.textView_Orb_Vallis_Timer);
            TextView textViewEarthTimer= convertView.findViewById(R.id.textView_Earth_Timer);

            textViewCetusTimer.setText(String.format("%s: %s in %s, reset at %s", _context.getString(R.string.cetus),_items.cetus_next_world_cycle_state(), _items.cetus_day_night_time(), _items.cetus_next_cycle_date()));
            textViewOrbVallisTimer.setText(String.format("%s: %s in %s, reset at %s", _context.getString(R.string.orb_vallis),_items.cetus_next_world_cycle_state(), _items.cetus_day_night_time(), _items.cetus_next_cycle_date()));
            textViewEarthTimer.setText(String.format("%s: %s in %s, reset at %s", _context.getString(R.string.earth),_items.cetus_next_world_cycle_state(), _items.cetus_day_night_time(), _items.cetus_next_cycle_date()));

        }

        return convertView;
    }
}