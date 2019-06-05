package com.evilflora.warframesentinel.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.evilflora.warframesentinel.Modele.AppSettings;
import com.evilflora.warframesentinel.Modele.NotificationServiceClass;
import com.evilflora.warframesentinel.R;

public class SettingFragment extends Fragment {

    AppSettings settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setting_content, container, false);
        getActivity().setTitle(getString(R.string.settings));

        settings = new AppSettings(getContext());

        Spinner spinner_platform_choice = view.findViewById(R.id.spinner_platform_choice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.platform_choice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_platform_choice.setAdapter(adapter);

        spinner_platform_choice.setSelection(adapter.getPosition(settings.getPlatform()));

        spinner_platform_choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                settings.setPlatform(arg0.getItemAtPosition(pos).toString());
                MenuActivity.warframeWorldState.ReloadWarframeWorldSate(settings.getPlatformCode());
               Toast.makeText(getActivity(), "Please restart the app when platform is changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //
            }
        });

        Switch switch_active_notification = view.findViewById(R.id.switch_active_notification);
        switch_active_notification.setChecked(settings.isNotificationEnabled());

        switch_active_notification.setOnCheckedChangeListener((compoundButton, b) ->  {
            settings.setNotificationEnabled(b);
            Intent myService = new Intent(getActivity(), NotificationServiceClass.class);
            if (b) {
                getActivity().startService(myService);
            } else {
                getActivity().stopService(myService);
            }
        });

        return view;
    }
}