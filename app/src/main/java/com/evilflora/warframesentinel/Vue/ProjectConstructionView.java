package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.ProjectConstructionClass;
import com.evilflora.warframesentinel.R;

public class ProjectConstructionView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private ProjectConstructionClass _items;

    public ProjectConstructionView(Context context, ProjectConstructionClass items) {
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
        if (convertView == null && _inflater != null) convertView = _inflater.inflate(R.layout.project_construction_fomo_razor_view, parent, false);

        if (convertView != null) {
            ProgressBar progressBarFomorian = convertView.findViewById(R.id.progressBarFomorian);
            ProgressBar progressBarRazorback = convertView.findViewById(R.id.progressBarRazorback);

            TextView textViewPercentFomorian = convertView.findViewById(R.id.textViewPercentFomorian);
            TextView textViewPercentRazorback = convertView.findViewById(R.id.textViewPercentRazorback);

            progressBarFomorian.setProgress(_items.getProjectPct(0));
            progressBarRazorback.setProgress(_items.getProjectPct(1));

            // todo
            textViewPercentFomorian.setText(String.format("%d%%", _items.getProjectPct(0)));
            textViewPercentRazorback.setText(String.format("%d%%", _items.getProjectPct(1)));
        }

        return convertView;
    }
}