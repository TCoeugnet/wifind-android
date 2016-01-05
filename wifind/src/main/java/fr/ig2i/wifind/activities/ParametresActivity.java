package fr.ig2i.wifind.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Thomas on 05/01/2016.
 */
public class ParametresActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.preferences);
    }

}
