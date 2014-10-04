package io.hackerbros.invite.activities;

import android.app.Fragment;

import io.hackerbros.invite.event.AddEventFragment;

public class EventActivity extends SimpleFragmentActivity {
    public Fragment getFragment() {
        return new AddEventFragment();
    }
}
