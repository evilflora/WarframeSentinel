package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.MarketItemsClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class MarketItemsListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<MarketItemsClass> _items;

    public MarketItemsListView(Context context, List<MarketItemsClass> items) {
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
        if (convertView == null &&_inflater != null) convertView = _inflater.inflate(R.layout.market_item_view, parent, false);

        if (convertView != null) {
            TextView marketItemName = convertView.findViewById(R.id.market_item_name);
            TextView marketItemTimeLeft = convertView.findViewById(R.id.market_item_time_left);
            TextView marketItemReduction = convertView.findViewById(R.id.market_item_reduction);

            marketItemName.setText(_items.get(position).getItemName());
            marketItemTimeLeft.setText(_items.get(position).getTimeBeforeEnd());
            marketItemReduction.setText( _items.get(position).getReduction());
        }

        return convertView;
    }

}