package io.hackerbros.invite.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import io.hackerbros.invite.R;

public abstract class SimpleFragmentActivity extends Activity {
   public abstract Fragment getFragment();

   protected FragmentManager fm;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_fragment);
      fm = getFragmentManager();
      Fragment fragment; // = fm.findFragmentById(R.id.fragmentContainer);

      fragment = getFragment();
      fm.beginTransaction()
              .add(R.id.fragment_container, fragment)
              .commit();
   }

   public void swapFragments(Fragment f) {
       fm.beginTransaction()
           .replace(R.id.fragment_container, f)
           .commit();
   }

}
