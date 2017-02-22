package edu.tatedobson.hhs.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hhs-rm51 on 2/22/2017.
 */
public class Settings extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}