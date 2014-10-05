package io.hackerbros.invite.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import com.facebook.widget.ProfilePictureView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.facebook.Session;
import com.facebook.Request;
import com.facebook.UiLifecycleHelper;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.Response;

import org.w3c.dom.Text;

import io.hackerbros.invite.R;
import io.hackerbros.invite.data.Event;

public class ProfileFragment extends Fragment implements TitledFragment {
    private ProfilePictureView profilePicView;
    private TextView userNameView;
    private UiLifecycleHelper uiHelper;
    private static final int REAUTH_ACTIVITY_CODE = 100;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public String getTitle() {
        return "Profile";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

// Find the user's profile picture custom view
        profilePicView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
        profilePicView.setCropped(true);

// Find the user's name view
        userNameView = (TextView) view.findViewById(R.id.selection_user_name);

        // Check for an open session
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            // Get the user's data
            makeMeRequest(session);
        }

        return view;
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            // Get the user's data.
            makeMeRequest(session);
        }
    }

    private void makeMeRequest(final Session session) {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                // Set the id for the ProfilePictureView
                                // view that in turn displays the profile picture.
                                profilePicView.setProfileId(user.getId());
                                // Set the Textview's text to the user's name.
                                userNameView.setText(user.getName());
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAUTH_ACTIVITY_CODE) {
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
}
