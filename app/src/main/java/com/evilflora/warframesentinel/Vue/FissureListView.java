package com.evilflora.warframesentinel.Vue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.FissureClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class FissureListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FissureClass> fissuresItems;

    public FissureListView(Activity activity, List<FissureClass> fissuresItems) {
        this.activity = activity;
        this.fissuresItems = fissuresItems;
    }

    @Override
    public int getCount() {
        return fissuresItems.size();
    }

    @Override
    public Object getItem(int location) { return fissuresItems.get(location); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) if (inflater != null) {
            convertView = inflater.inflate(R.layout.fissure_element_view, parent, false);
        }

        if(convertView != null) {
            TextView fissure_location = convertView.findViewById(R.id.fissure_location);
            TextView fissure_type = convertView.findViewById(R.id.fissure_type);
            TextView fissuret_time = convertView.findViewById(R.id.fissure_time);

            fissure_location.setText(fissuresItems.get(position).get_location());
            fissure_type.setText(fissuresItems.get(position).get_type());
            fissuret_time.setText(fissuresItems.get(position).get_time_before_expiry());
        }

        return convertView;
    }

}