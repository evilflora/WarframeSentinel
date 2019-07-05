package com.evilflora.warframesentinel.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.BountyJobClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class BountiesListView extends BaseAdapter {
    private Context _context;
    private LayoutInflater _inflater;
    private List<BountyJobClass> _items;

    public BountiesListView(Context context, List<BountyJobClass> items) {
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
        if (convertView == null && _inflater != null) convertView = _inflater.inflate(R.layout.bounty_job_view, parent, false);

        if (convertView != null) {
            TextView bountyJobType = convertView.findViewById(R.id.bounty_type);
            TextView bountyJobLevel = convertView.findViewById(R.id.bounty_level);
            TextView bountyTotalStanding = convertView.findViewById(R.id.bounty_total_stading_reward);
            LinearLayout bountyLinearLayoutRewardList = convertView.findViewById(R.id.bounty_rewards);

            bountyJobType.setText(_items.get(position).getJobType());
            bountyJobLevel.setText(_items.get(position).getEnemyLevel());
            bountyTotalStanding.setText(_items.get(position).getTotalStanding());

            bountyLinearLayoutRewardList.removeAllViews(); // Fix the infinite addition of standing rewards while scrolling

            for (int i = 0; i < _items.get(position).getNumberOfJobs(); i++) {
                View view = LayoutInflater.from(_context).inflate(R.layout.standing_reward_view, parent, false);
                TextView bountyJobStanding = view.findViewById(R.id.standing_amount);
                bountyJobStanding.setText(String.valueOf(_items.get(position).getStandingForJob(i)));
                bountyLinearLayoutRewardList.addView(view);
            }
        }
        return convertView;
    }

}