package com.evilflora.warframesentinel.Vue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.SortieStepClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class SortieStepListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SortieStepClass> sortieStep;

    public SortieStepListView(Activity activity, List<SortieStepClass> sortieStep) {
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
        if (convertView == null && inflater != null) convertView = inflater.inflate(R.layout.sortie_element_view, parent, false);

        if (convertView != null) {
            TextView sortieLocation = convertView.findViewById(R.id.sortie_location);
            TextView sortieType = convertView.findViewById(R.id.sortie_type);
            TextView sortieLevel = convertView.findViewById(R.id.sortie_level);
            TextView sortieCondition = convertView.findViewById(R.id.sortie_condition);
            TextView sortieCredits = convertView.findViewById(R.id.sortie_credits);

            sortieLocation.setText(sortieStep.get(position).getLocation());
            sortieType.setText(sortieStep.get(position).getMissionType());
            sortieLevel.setText(sortieStep.get(position).getLevel());
            sortieCondition.setText(sortieStep.get(position).getCondition());
            sortieCredits.setText(sortieStep.get(position).getCredits());
        }

        return convertView;
    }

}