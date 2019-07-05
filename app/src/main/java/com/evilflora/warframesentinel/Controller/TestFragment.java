package com.evilflora.warframesentinel.Controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evilflora.warframesentinel.R;

public class TestFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabs;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.bounties));

        viewPager = view.findViewById(R.id.view_pager);
        tabs = view.findViewById(R.id.tabs);
        MenuActivity.getAdapter().setTabTitles(new int[]{R.string.cetus, R.string.orb_vallis});
        viewPager.setAdapter(MenuActivity.getAdapter());
        tabs.setupWithViewPager(viewPager);


        return view;
    }
}
