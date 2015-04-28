package com.comp380.csun.comp380;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.widget.ListView;

/**
 * Class that handles the Settings view and activity.
 * This class is called upon the screens that specify the overflow button
 * (three vertical squares).
 */
public class SettingsActivity extends PreferenceActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_settings);
    }
}