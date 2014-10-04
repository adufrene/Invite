package io.hackerbros.invite.login;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.util.Log;

import io.hackerbros.invite.R;

import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class LoginFragment extends Fragment {

  private static final String TAG = LoginFragment.class.getSimpleName();

  public static LoginFragment createFragment() {
    return new LoginFragment();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_login, parent, false);

    v.findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        createParseUser();
      }
    });

    return v;
  }

  private void createParseUser() {
    ParseUser user = new ParseUser();
    user.setUsername("my name");
    user.setPassword("my pass");
    user.setEmail("email@example.com");
  
    // other fields can be set just like with ParseObject
    // user.put("phone", "650-555-0000");
    //   
    user.signUpInBackground(new SignUpCallback() {
      public void done(ParseException e) {
        if (e == null) {
          Log.d(TAG, "Success");
        // Hooray! Let them use the app now.
        } else {
          Log.d(TAG, "Error: " + e.getMessage());
        // Sign up didn't succeed. Look at the ParseException
        // to figure out what went wrong
        }
      }
    });
  }
}
