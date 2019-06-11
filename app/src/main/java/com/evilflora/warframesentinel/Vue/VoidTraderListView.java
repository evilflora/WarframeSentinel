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
        if (convertView == null && _inflater != null) convertView = _inflater.inflate(R.layout.void_trader_element_view, parent, false);

        if (convertView != null) {
            TextView voidTraderitemName = convertView.findViewById(R.id.void_trader_item_name);
            TextView voidTraderitemDucatPrice = convertView.findViewById(R.id.void_trader_item_ducat_price);
            TextView voidTraderitemCreditPrice = convertView.findViewById(R.id.void_trader_item_credit_price);

            voidTraderitemName.setText(_items.get(position).getItemName());
            voidTraderitemDucatPrice.setText(_items.get(position).getDucatPrice());
            voidTraderitemCreditPrice.setText(_items.get(position).getCreditPrice());
        }


        return convertView;
    }

}