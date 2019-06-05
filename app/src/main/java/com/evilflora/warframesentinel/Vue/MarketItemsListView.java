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
        if (convertView == null) if (_inflater != null) {
            convertView = _inflater.inflate(R.layout.market_item_view, parent, false);
        }

        if (convertView != null) {
            TextView market_item_name = convertView.findViewById(R.id.market_item_name);
            TextView market_item_time_left = convertView.findViewById(R.id.market_item_time_left);
            TextView market_item_reduction = convertView.findViewById(R.id.market_item_reduction);

            market_item_name.setText(_items.get(position).get_item_name());
            market_item_time_left.setText(_items.get(position).get_time_before_expiry());
            // todo
            market_item_reduction.setText( _items.get(position).is_regular_override() ?  String.format("%s %s",_items.get(position).get_regular_override(), _context.getString(R.string.credits)):(_items.get(position).get_discount() > 0 ? String.format("%s % %s %s",_items.get(position).get_discount(), _items.get(position).get_premium_override() , _context.getString(R.string.plats) ): String.format("%s %s",_items.get(position).get_premium_override() , _context.getString(R.string.plats))));
        }

        return convertView;
    }

}