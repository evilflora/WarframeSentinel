package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.content.SharedPreferences;

import com.evilflora.warframesentinel.R;

public class AppSettings {

    private Context _context;
    private String _platform;
    private boolean _statusNotification;
    private SharedPreferences _settings;
    private SharedPreferences.Editor _editor;

    // todo : need to be verified and completed
    public AppSettings(Context context) {
        this._context = context.getApplicationContext();
        this.loadSettings();
    }

    public void loadSettings() {
        String configFile = "ConfigFile";
        _settings = _context.getSharedPreferences(configFile, 0);
        _platform = _settings.getString("Platform", _context.getResources().getStringArray(R.array.platform_choice)[0]);
        _statusNotification = _settings.getBoolean("Active_Notification", false);
    }

    public String getPlatformCode() {

        return (_platform.compareTo(_context.getResources().getStringArray(R.array.platform_choice)[0]) == 0 ? "" : (_platform.compareTo(_context.getResources().getStringArray(R.array.platform_choice)[1]) == 0) ? ".xb1" : ".ps4");
    }

    public String getPlatform() {
        return _platform;
    }

    public void setPlatform(String platform) {
        _editor = _settings.edit();
        _editor.putString("Platform", platform);

        // Commit the edits!
        _editor.apply();
        _editor.commit();
    }

    public boolean isNotificationEnabled() {
        return _statusNotification;
    }

    public void setNotificationEnabled(boolean statusNotification) {
        _editor = _settings.edit();
        _editor.putBoolean("Active_Notification", statusNotification);

        // Commit the edits!
        _editor.apply();
        _editor.commit();
    }
}
