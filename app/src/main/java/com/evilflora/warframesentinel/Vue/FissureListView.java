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
        if (convertView == null && inflater != null) convertView = inflater.inflate(R.layout.fissure_element_view, parent, false);

        if(convertView != null) {
            TextView fissureLocation = convertView.findViewById(R.id.fissure_location);
            TextView fissureType = convertView.findViewById(R.id.fissure_type);
            TextView fissuretTime = convertView.findViewById(R.id.fissure_time);

            fissureLocation.setText(fissuresItems.get(position).getLocation());
            fissureType.setText(fissuresItems.get(position).getType());
            fissuretTime.setText(fissuresItems.get(position).getTimeBeforeEnd());
        }

        return convertView;
    }

}