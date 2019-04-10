package com.evilflora.warframesentinel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class DarvoDealsListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<DarvoDealsClass> _items;

    DarvoDealsListView(Context context, List<DarvoDealsClass> items) {
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
            convertView = _inflater.inflate(R.layout.darvo_deal_view, parent, false);
        }

            if(convertView != null) {
                TextView darvo_deal_item_name = convertView.findViewById(R.id.darvo_deal_item_name);
                TextView darvo_deal_time_left = convertView.findViewById(R.id.darvo_deal_time_left);
                TextView darvo_deal_reduction = convertView.findViewById(R.id.darvo_deal_reduction);
                TextView darvo_deal_items_left = convertView.findViewById(R.id.darvo_deal_items_left);

                darvo_deal_item_name.setText(_items.get(position).get_item_name());
                darvo_deal_time_left.setText(_items.get(position).get_time_before_expiry());
                darvo_deal_reduction.setText(_items.get(position).get_reduction());
                darvo_deal_items_left.setText(String.format("%s %s", _items.get(position).get_items_left(), _context.getString(R.string.left)));
            }
        return convertView;
    }

}