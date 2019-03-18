package com.evilflora.warframesentinel;

import android.content.Context;
import android.content.SharedPreferences;

class AppSettings {

    private Context _context;
    private String PREFS_NAME = "ConfigFile";
    private String _platform;
    private boolean _activate_notification;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    AppSettings(Context context) {
        this._context = context.getApplicationContext();
        this.loadSettings();
    }

    private void loadSettings() {
        settings = _context.getSharedPreferences(this.PREFS_NAME, 0);
        _platform = settings.getString("Platform", _context.getResources().getStringArray(R.array.platform_choice)[0]);
        _activate_notification = settings.getBoolean("Active_Notification", false);
    }

    String get_platform_code() {
        return (_platform.compareTo(_context.getResources().getStringArray(R.array.platform_choice)[0]) == 0 ? "" : (_platform.compareTo(_context.getResources().getStringArray(R.array.platform_choice)[1]) == 0) ? ".xb1" : ".ps4");
    }

    String get_platform() {
        return _platform;
    }

    void set_platform(String platform) {
        editor = settings.edit();
        editor.putString("Platform", platform);

        // Commit the edits!
        editor.apply();
        editor.commit();
    }

    boolean get_activate_notification() {
        return _activate_notification;
    }

    void set_activate_notification(boolean activate_notification) {
        editor = settings.edit();
        editor.putBoolean("Active_Notification", activate_notification);

        // Commit the edits!
        editor.apply();
        editor.commit();
    }
}
