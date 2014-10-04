package io.hackerbros.invite.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import io.hackerbros.invite.R;

public abstract class SimpleFragmentActivity extends Activity {
   public abstract Fragment getFragment();

   private FragmentManager fm;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_fragment);
      fm = getFragmentManager();
      Fragment fragment; // = fm.findFragmentById(R.id.fragmentContainer);

      fragment = getFragment();
      fm.beginTransaction()
              .add(R.id.login_feed_full_fragment_container, fragment)
              .commit();
   }

   public void addFragment(Fragment newFrag) {
      Fragment newFragment = newFrag;
      fm.beginTransaction()
              .add(R.id.login_feed_full_fragment_container, newFragment)
              .commit();
   }

   public void removeFragment(Fragment removeFrag) {
      fm.beginTransaction()
              .remove(removeFrag)
              .commit();
   }
}
