package io.hackbros.invite.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import io.hackbros.invite.R;
import io.hackbros.invite.event.AddEventFragment;

public class EventActivity extends FragmentActivity {
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        fm = getSupportFragmentManager();
        AddEventFragment fragment; // = fm.findFragmentById(R.id.fragmentContainer);

        fragment = new AddEventFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


}