package com.evilflora.warframesentinel;

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

import java.util.List;

class InvasionListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<InvasionClass> _items;

    InvasionListView(Context context, List<InvasionClass> items) {
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
        if (convertView == null) if (_inflater != null) {
            convertView = _inflater.inflate(R.layout.invasion_element_view, parent, false);
        }


        if (convertView != null) {
            TextView invasion_location      = convertView.findViewById(R.id.construction_status);
            TextView invasion_type          = convertView.findViewById(R.id.sortie_type);
            TextView invasion_reward_attacker   = convertView.findViewById(R.id.invasion_reward_attacker);
            TextView invasion_reward_defender  = convertView.findViewById(R.id.invasion_reward_defender);
            TextView invasion_percent_attacker       = convertView.findViewById(R.id.invasion_percent_attacker);
            TextView invasion_percent_defender       = convertView.findViewById(R.id.invasion_percent_defender);
            ProgressBar invasion_progress   = convertView.findViewById(R.id.darvo_current);

            // Dynamic progressBar
            GradientDrawable layerBackground = new GradientDrawable(); // background
            layerBackground.setShape(GradientDrawable.RECTANGLE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                layerBackground.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_defender_faction(), "color", _context.getPackageName()), _context.getTheme()));
            }else {
                //noinspection deprecation
                layerBackground.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_defender_faction(), "color", _context.getPackageName())));
            }

            GradientDrawable layerProgress = new GradientDrawable();
            layerProgress.setShape(GradientDrawable.RECTANGLE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                layerProgress.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_attacker_faction(), "color", _context.getPackageName()), _context.getTheme()));
            }else {
                //noinspection deprecation
                layerProgress.setColor(_context.getResources().getColor(_context.getResources().getIdentifier(_items.get(position).get_attacker_faction(), "color", _context.getPackageName())));
            }

            ClipDrawable progress = new ClipDrawable(layerProgress,11,800005);

            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {layerBackground, progress} );
            layerDrawable.setId(0, android.R.id.background);
            layerDrawable.setId(1, android.R.id.progress);

            // Setter
            _items.get(position).set_progressBar(layerDrawable);

            String location = _items.get(position).get_location();
            try {
                location = _context.getResources().getString(_context.getResources().getIdentifier(location, "string", _context.getPackageName()));
            }catch (Exception ex) {
                //
            }

            String attacker = _items.get(position).get_attacker_faction();
            try {
                attacker = _context.getResources().getString(_context.getResources().getIdentifier(attacker, "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            String defender = _items.get(position).get_defender_faction();
            try {
                defender = _context.getResources().getString(_context.getResources().getIdentifier(defender, "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            String reward_defender = _items.get(position).get_reward_defender();

            try {
                reward_defender = _context.getResources().getString(_context.getResources().getIdentifier(reward_defender, "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            String reward_attacker = _items.get(position).get_reward_attacker();

            try {
                reward_attacker = _context.getResources().getString(_context.getResources().getIdentifier(reward_attacker, "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            invasion_location.setText(location);
            invasion_type.setText(String.format("%s vs %s", attacker, defender));
            invasion_reward_attacker.setText((reward_attacker==null?"": (_items.get(position).get_attacker_reward_count()>1?_items.get(position).get_attacker_reward_count() + " ":"") + reward_attacker));
            invasion_reward_defender.setText((reward_defender==null?"": (_items.get(position).get_defender_reward_count()>1?_items.get(position).get_defender_reward_count() + " ":"") + reward_defender));

            invasion_progress.setMax((int)_items.get(position).getGoal());
            invasion_progress.setProgress((int)_items.get(position).getCount());
            invasion_progress.setProgressDrawable(_items.get(position).get_progressBar());
            invasion_percent_attacker.setText(String.format("%s%%", _items.get(position).get_percent_attacker()));
            invasion_percent_defender.setText(String.format("%s%%", _items.get(position).get_percent_defender()));
        }

        return convertView;
    }

}