package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.DarvoDealsClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class DarvoDealsListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<DarvoDealsClass> _items;

    public DarvoDealsListView(Context context, List<DarvoDealsClass> items) {
        this._context = context;
        this._items = items;
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

        if (_inflater == null) _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null && _inflater != null) convertView = _inflater.inflate(R.layout.darvo_deal_view, parent, false);

        if(convertView != null) {
            TextView darvoDealItemName = convertView.findViewById(R.id.darvo_deal_item_name);
            TextView darvoDealTimeLeft = convertView.findViewById(R.id.darvo_deal_time_left);
            TextView darvoDealReduction = convertView.findViewById(R.id.darvo_deal_reduction);
            TextView darvoDealItemsLeft = convertView.findViewById(R.id.darvo_deal_items_left);

            darvoDealItemName.setText(_items.get(position).getItemName());
            darvoDealTimeLeft.setText(_items.get(position).getTimeLeft());
            darvoDealReduction.setText(_items.get(position).getPriceAndDiscount());
            darvoDealItemsLeft.setText(_items.get(position).getItemAmountLeft());
        }
        return convertView;
    }

}