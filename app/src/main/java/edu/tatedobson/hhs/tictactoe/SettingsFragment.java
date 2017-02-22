package edu.tatedobson.hhs.tictactoe;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by hhs-rm51 on 2/22/2017.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}