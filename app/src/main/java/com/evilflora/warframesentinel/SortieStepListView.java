package com.evilflora.warframesentinel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class SortieStepListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SortieStepClass> sortieStep;

    SortieStepListView(Activity activity, List<SortieStepClass> sortieStep) {
        this.activity = activity;
        this.sortieStep = sortieStep;
    }

    @Override
    public int getCount() {
        return sortieStep.size();
    }

    @Override
    public Object getItem(int location) {
        return sortieStep.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) if (inflater != null) {
            convertView = inflater.inflate(R.layout.sortie_element_view, parent, false);
        }

        if (convertView != null) {
            TextView sortie_location = convertView.findViewById(R.id.sortie_location);
            TextView sortie_type = convertView.findViewById(R.id.sortie_type);
            TextView sortie_level = convertView.findViewById(R.id.sortie_level);
            TextView sortie_condition = convertView.findViewById(R.id.sortie_condition);
            TextView sortie_credits = convertView.findViewById(R.id.sortie_credits);

            sortie_location.setText(sortieStep.get(position).get_location());
            sortie_type.setText(sortieStep.get(position).get_mission_type());
            sortie_level.setText(sortieStep.get(position).get_level());
            sortie_condition.setText(sortieStep.get(position).get_condition());
            sortie_credits.setText(sortieStep.get(position).get_credits());
        }

        return convertView;
    }

}