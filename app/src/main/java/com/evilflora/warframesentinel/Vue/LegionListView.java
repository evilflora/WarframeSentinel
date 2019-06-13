package com.evilflora.warframesentinel.Vue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.LegionChallengeClass;
import com.evilflora.warframesentinel.R;

import java.util.List;

public class LegionListView extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<LegionChallengeClass> challengesItems;

    public LegionListView(Activity activity, List<LegionChallengeClass> challengesItems) {
        this.activity = activity;
        this.challengesItems = challengesItems;
    }

    @Override
    public int getCount() { return challengesItems.size(); }

    @Override
    public Object getItem(int location) { return challengesItems.get(location); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null && inflater != null) convertView = inflater.inflate(R.layout.legion_element_view, parent, false);

        if(convertView != null) {
            TextView challengeType   = convertView.findViewById(R.id.challenge_type);
            TextView challengeName   = convertView.findViewById(R.id.challenge_name);
            TextView challengeTime   = convertView.findViewById(R.id.challenge_time);
            TextView challengeReward = convertView.findViewById(R.id.challenge_reward);

            challengeType.setText(challengesItems.get(position).getType());
            challengeName.setText(challengesItems.get(position).getName());
            challengeTime.setText(challengesItems.get(position).getTimeBeforeEnd());
            challengeReward.setText(challengesItems.get(position).getStandingReward());
        }

        return convertView;
    }

}