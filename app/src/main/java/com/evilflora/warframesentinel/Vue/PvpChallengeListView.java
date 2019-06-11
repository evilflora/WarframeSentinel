package com.evilflora.warframesentinel.Vue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.PvpChallengeClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class PvpChallengeListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<PvpChallengeClass> _items;

    public PvpChallengeListView(Activity activity, List<PvpChallengeClass> fissuresItems) {
        this.activity = activity;
        this._items = fissuresItems;
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

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null && inflater != null) convertView = inflater.inflate(R.layout.pvp_challenge_element_view, parent, false);

        if(convertView != null) {
            TextView category  = convertView.findViewById(R.id.pvp_challenge_category);
            TextView challenge = convertView.findViewById(R.id.pvp_challenge_ref);
            TextView mode      = convertView.findViewById(R.id.pvp_challenge_mode);
            TextView time      = convertView.findViewById(R.id.pvp_challenge_time);

            category.setText(_items.get(position).getCategory());
            challenge.setText(_items.get(position).getChallenge());
            mode.setText(_items.get(position).getMode());
            time.setText(_items.get(position).getTimeBeforeEnd());
        }

        return convertView;
    }

}