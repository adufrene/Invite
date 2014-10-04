package io.hackerbros.invite.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import io.hackerbros.invite.R;
import io.hackerbros.invite.data.Event;
import io.hackerbros.invite.network.InviteNetworkObject;

public class AddEventFragment extends Fragment implements View.OnClickListener{
    private Button submitButton;
    private Activity parentActivity;

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        parentActivity = getActivity();

        submitButton = (Button) parentActivity.findViewById(R.id.add_event_submit_button);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_event_submit_button) {
            constructEventsFromUserFields();
        }
    }

    private void constructEventsFromUserFields() {
        Event newEvent = new Event();

        EditText titleField = (EditText) parentActivity.findViewById(R.id.add_event_edit_title);
        EditText descriptionField = (EditText) parentActivity.findViewById(R.id.add_event_edit_description);
        RadioGroup selector = (RadioGroup) parentActivity.findViewById(R.id.add_event_type_selector);

        newEvent.setEventTitle(titleField.getText().toString());
        newEvent.setEventDescription(descriptionField.getText().toString());
        newEvent.setPublicEvent(selector.getCheckedRadioButtonId()
                == R.id.add_event_type_selector_public ? true : false);

        InviteNetworkObject.submitEventObject(newEvent);
    }
}
