package io.hackerbros.invite;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import io.hackerbros.invite.R;

public abstract class SimpleFragmentActivity extends Activity {
  public abstract Fragment getFragment();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);
    FragmentManager fm = getFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
     
    if (fragment == null) {
      fragment = getFragment();
      fm.beginTransaction()
        .add(R.id.fragmentContainer, fragment)
        .commit();
    }
  }
}
