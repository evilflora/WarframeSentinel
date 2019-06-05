package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.InvasionClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class InvasionListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<InvasionClass> _items;

    public InvasionListView(Context context, List<InvasionClass> items) {
        this._context = context;
        this._items = items;
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
        if (convertView == null && _inflater != null) convertView = _inflater.inflate(R.layout.invasion_element_view, parent, false);

        if (convertView != null) {
            TextView invasionLocation           = convertView.findViewById(R.id.invasion_location);
            TextView invasionType               = convertView.findViewById(R.id.invasion_type);
            TextView invasionRewardAttacker     = convertView.findViewById(R.id.invasion_reward_attacker);
            TextView invasionRewardDefender     = convertView.findViewById(R.id.invasion_reward_defender);
            TextView invasionPercentAattacker   = convertView.findViewById(R.id.invasion_percent_attacker);
            TextView invasionPercentDefender    = convertView.findViewById(R.id.invasion_percent_defender);
            ProgressBar invasionProgress        = convertView.findViewById(R.id.invasion_progress);
            String colorName = "color";

            // Dynamic progressBar
            GradientDrawable layerBackground = new GradientDrawable(); // background
            layerBackground.setShape(GradientDrawable.RECTANGLE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // if it's Marshmallow / API 23, getColor call has changed
                layerBackground.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_defender_faction_code(), colorName, _context.getPackageName()), _context.getTheme()));
            } else {
                layerBackground.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_defender_faction_code(), colorName, _context.getPackageName())));
            }

            GradientDrawable layerProgress = new GradientDrawable();
            layerProgress.setShape(GradientDrawable.RECTANGLE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // if it's Marshmallow / API 23, getColor call has changed
                layerProgress.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_attacker_faction_code(), colorName, _context.getPackageName()), _context.getTheme()));
            } else {
                layerProgress.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_attacker_faction_code(), colorName, _context.getPackageName())));
            }

            ClipDrawable progress = new ClipDrawable(layerProgress,11,800005);

            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {layerBackground, progress} );
            layerDrawable.setId(0, android.R.id.background);
            layerDrawable.setId(1, android.R.id.progress);

            // Setter
            _items.get(position).set_progressBar(layerDrawable);

            invasionLocation.setText(_items.get(position).get_location());
            invasionType.setText(String.format("%s vs %s", _items.get(position).get_attacker_faction(), _items.get(position).get_defender_faction()));
            invasionRewardAttacker.setText(String.format("%s %s", _items.get(position).get_attacker_reward_count() > 1 ? _items.get(position).get_attacker_reward_count() : "", _items.get(position).get_reward_defender()));
            invasionRewardDefender.setText(String.format("%s %s", _items.get(position).get_defender_reward_count() > 1 ? _items.get(position).get_defender_reward_count() : "", _items.get(position).get_reward_defender()));

            invasionProgress.setMax((int)_items.get(position).get_goal());
            invasionProgress.setProgress((int)_items.get(position).get_count());
            invasionProgress.setProgressDrawable(_items.get(position).get_progressBar());
            invasionPercentAattacker.setText(String.format("%s%%", _items.get(position).get_percent_attacker()));
            invasionPercentDefender.setText(String.format("%s%%", _items.get(position).get_percent_defender()));
        }

        return convertView;
    }

}