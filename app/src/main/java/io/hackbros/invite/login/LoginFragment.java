package io.hackbros.invite.login;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.app.ProgressDialog;

import io.hackbros.invite.R;
import io.hackbros.invite.util.SharedPrefsUtils;
import io.hackbros.invite.activities.SimpleFragmentActivity;
import io.hackbros.invite.activities.LoadingActivity;

import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.LogInCallback;
import com.parse.SignUpCallback;

import java.util.Arrays;

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
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please Wait...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        ParseFacebookUtils.logIn(Arrays.asList("user_friends"), getActivity(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                pd.dismiss();
                if (user == null) {
                    Log.d(TAG, "User canceled");
                } else {
                    Log.d(TAG, "User logged in");
                    getActivity().finish();
                    Intent i = new Intent(getActivity(), LoadingActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(i);
                }
            }
        });
    }

}
