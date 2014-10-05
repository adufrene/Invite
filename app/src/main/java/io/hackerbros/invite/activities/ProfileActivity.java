package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;

import io.hackerbros.invite.fragments.ProfileFragment;

public class ProfileActivity extends SimpleFragmentActivity {
    @Override
    public Fragment getFragment() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
}
