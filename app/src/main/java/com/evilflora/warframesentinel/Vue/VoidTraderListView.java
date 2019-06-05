package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.VoidTraderItemClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class VoidTraderListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<VoidTraderItemClass> _items;

    public VoidTraderListView(Context context, List<VoidTraderItemClass> items) {
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
            convertView = _inflater.inflate(R.layout.void_trader_element_view, parent, false);
        }

        if (convertView != null) {
            TextView void_trader_item_name = convertView.findViewById(R.id.void_trader_item_name);
            TextView void_trader_item_ducat_price = convertView.findViewById(R.id.void_trader_item_ducat_price);
            TextView void_trader_item_credit_price = convertView.findViewById(R.id.void_trader_item_credit_price);

            String item_name = _items.get(position).get_item_name();
            try {
                item_name = _context.getResources().getString(_context.getResources().getIdentifier(item_name, "string", _context.getPackageName()));
            } catch (Exception ex) {
                //
            }

            void_trader_item_name.setText(item_name);
            void_trader_item_ducat_price.setText(String.format(_context.getString(R.string.ducats) + " %d", _items.get(position).get_ducat_price()));
            void_trader_item_credit_price.setText(String.format(_context.getString(R.string.credits) + " %d", _items.get(position).get_credit_price()));
        }


        return convertView;
    }

}