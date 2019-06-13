package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.WorldCycleClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class WorldCyclesView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<WorldCycleClass> _items;

    public WorldCyclesView(Context context, List<WorldCycleClass> items) {
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
        if (convertView == null &&_inflater != null) convertView = _inflater.inflate(R.layout.world_cycle_content, parent, false);

        if (convertView != null) {
            TextView textViewCetusTimer = convertView.findViewById(R.id.textView_Cetus_Timer);
            TextView textViewOrbVallisTimer= convertView.findViewById(R.id.textView_Orb_Vallis_Timer);
            TextView textViewEarthTimer= convertView.findViewById(R.id.textView_Earth_Timer);

            textViewCetusTimer.setText(_items.get(0).getWorldStatusCycleStatus(_context.getString(R.string.cetus)));
            textViewOrbVallisTimer.setText(_items.get(1).getWorldStatusCycleStatus(_context.getString(R.string.orb_vallis)));
            textViewEarthTimer.setText(_items.get(0).getWorldStatusCycleStatus(_context.getString(R.string.earth)));
        }

        return convertView;
    }
}