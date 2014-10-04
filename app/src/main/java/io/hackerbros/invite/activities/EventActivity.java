package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;

import io.hackerbros.invite.event.AddEventFragment;

public class EventActivity extends SimpleFragmentActivity {
    public Fragment getFragment() {
        return new AddEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
