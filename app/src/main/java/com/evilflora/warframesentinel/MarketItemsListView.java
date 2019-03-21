package com.evilflora.warframesentinel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class MarketItemsListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<MarketItemsClass> _items;

    MarketItemsListView(Context context, List<MarketItemsClass> items) {
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

            String item_name = _items.get(position).get_item_name();

            try {
                item_name = _context.getResources().getString(_context.getResources().getIdentifier(item_name, "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            market_item_name.setText(item_name);
            market_item_time_left.setText(_items.get(position).get_time_before_expiry());
            market_item_reduction.setText( _items.get(position).get_is_regular_override() ?  String.format("%s %s",_items.get(position).get_regular_override(), _context.getString(R.string.credits)):(_items.get(position).get_discount() > 0 ? String.format("%s % %s %s",_items.get(position).get_discount(), _items.get(position).get_premium_override() , _context.getString(R.string.plats) ): String.format("%s %s",_items.get(position).get_premium_override() , _context.getString(R.string.plats))));
        }

        return convertView;
    }

}