package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.NewsClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class NewsListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<NewsClass> _items;

    public NewsListView(Context context, List<NewsClass> items) {
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
        if (convertView == null &&_inflater != null) convertView = _inflater.inflate(R.layout.news_view, parent, false);

        if (convertView != null) {
            TextView newsName = convertView.findViewById(R.id.news_name);
            TextView newsDate = convertView.findViewById(R.id.news_date);
            //ImageView news_image = convertView.findViewById(R.id.news_image);

            newsName.setText(_items.get(position).getMessage());
            newsDate.setText(_items.get(position).getTimeAgo());
            //if(!_items.get(position).is_image_downloaded()) _items.get(position).download_image();
            //news_image.setImageBitmap(_items.get(position).get_image());

            convertView.setOnClickListener(v -> {
                Uri uri = Uri.parse(_items.get(position).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                _context.startActivity(intent);
            });
        }

        return convertView;
    }

}